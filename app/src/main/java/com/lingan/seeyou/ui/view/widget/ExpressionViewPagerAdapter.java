package com.lingan.seeyou.ui.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.lingan.seeyou.ui.view.MeasureGridView;
import com.lingan.seeyou.ui.view.skin.ViewFactory;

import java.util.List;

import nickgao.com.meiyousample.ExpressionController;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.adapter.ExpressionGridViewAdapter;
import nickgao.com.meiyousample.utils.model.ExpressionModel;
import nickgao.com.meiyousample.utils.model.ExpressionTabType;
import nickgao.com.meiyousample.utils.model.ExpressionType;

/**
 *
 * Created by lwh on 2015/7/29.
 */
public class ExpressionViewPagerAdapter extends PagerAdapter {

    private String TAG = "ExpressionPagerAdapter";
    private Context mContext;
    private EmojiLayout emojiLayout;
    private int mTotalCount;
    public ExpressionViewPagerAdapter(EmojiLayout emojiLayout, int totalCount) {

        this.mContext = emojiLayout.getContext();//.getApplicationContext();
        this.emojiLayout = emojiLayout;
        this.mTotalCount = totalCount;

    }

    @Override
    public int getCount() {
        return mTotalCount;
    }
    //每次都会先执行 0 1 60 59 61
    @Override
    public Object instantiateItem(final View paramView, final int position) {
        //LogUtils.d(TAG, "-->instantiateItem:" + position);
        //构造View
        View view =getView(position);
        // 加入ViewPager
        ((ViewGroup) paramView).addView(view);
        view.setTag(position);
        return view;
        //return paramView;
    }

    private View getView(final int position) {
        View view =  ViewFactory.from(mContext).getLayoutInflater().inflate(R.layout.custom_face_view, null);
        MeasureGridView faceGv = (MeasureGridView) view.findViewById(R.id.chatCustomFaceGv);
        faceGv.setHorizontalSpacing(0);
        faceGv.setVerticalSpacing(0);
        int tabType = ExpressionController.getInstance().getGridViewTabType(position);
        if(tabType== ExpressionTabType.DEFAULT){
            faceGv.setNumColumns(6);
        }else{
            faceGv.setNumColumns(4);
        }
        faceGv.setTouch(true);
        faceGv.setBackgroundColor(Color.TRANSPARENT);
        faceGv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        List<ExpressionModel> listDatas = ExpressionController.getInstance().getGridViewDatas(position);
        //List<ExpressionModel> listDatas = new ArrayList<>();
        //listDatas.addAll(list);
        ExpressionGridViewAdapter adapter = new ExpressionGridViewAdapter(mContext,tabType,listDatas,0,0,true);
        faceGv.setAdapter(adapter);
        setListener(faceGv, tabType, listDatas);
        return view;
    }

    /**
     * 设置gridview所有监听
     * @param gridView
     * @param tabType
     * @param listDats
     */
    private void setListener(MeasureGridView gridView,final int tabType, final List<ExpressionModel> listDats){
        try {
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //LogUtils.d(TAG,"--onItemClick:"+i);
                    ExpressionModel expressionModel = listDats.get(i);
                    switch (expressionModel.type){
                        case ExpressionType.EMOJI:{
                            emojiLayout.handleOnItemClick(expressionModel.emojiModel);
                            break;
                        }
                        case ExpressionType.GIF:{
                            emojiLayout.handleOnItemClick(expressionModel.expressionSubModel);
                            break;
                        }
                        case ExpressionType.DELETE:{
                            emojiLayout.handlePressDelete();
                            break;
                        }
                        case ExpressionType.NONE:{
                            break;
                        }
                    }
                }
            });
            gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                   /* LogUtils.d(TAG,"--onItemLongClick:"+i);
                    ExpressionModel expressionModel = listDats.get(i);
                    Rect rect = new Rect();
                    view.getGlobalVisibleRect(rect);
                    switch (expressionModel.type){
                        case ExpressionType.EMOJI:{
                            //emojiLayout.handleOnItemClick(expressionModel.emojiModel);
                            emojiLayout.setIsShowExpressionPreview(true);
                            emojiLayout.showExpressionPreview(expressionModel,rect);
                            break;
                        }
                        case ExpressionType.GIF:{
                            emojiLayout.setIsShowExpressionPreview(true);
                            emojiLayout.showExpressionPreview(expressionModel, rect);
                            //emojiLayout.handleOnItemClick(expressionModel.expressionSubModel);
                            break;
                        }
                        case ExpressionType.DELETE:{
                            //emojiLayout.handlePressDelete();
                            break;
                        }
                        case ExpressionType.NONE:{
                            break;
                        }
                    }*/

                    return true;
                }
            });
            gridView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    try{
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                //LogUtils.d(TAG,"--ACTION_UP ACTION_CANCEL");
                                emojiLayout.setIsShowExpressionPreview(false);
                                emojiLayout.dismissExpressionPreview();
                                break;
                            case MotionEvent.ACTION_DOWN:
                            case MotionEvent.ACTION_MOVE:
                                //LogUtils.d(TAG,"--ACTION_DOWN ACTION_MOVE");
                                /*if (emojiLayout.canShowExpressionPreview()) {
                                    Rect rect = new Rect();
                                    view.getGlobalVisibleRect(rect);
                                    if (view instanceof GridView) {
                                        int column,row;
                                        switch (tabType){
                                            case ExpressionTabType.DEFAULT:{
                                                column = EmojiConversionUtil.PAGE_COLUMN_COUNT;
                                                row = EmojiConversionUtil.PAGE_LINE_COUNT;
                                                break;
                                            }
                                            case ExpressionTabType.GIF:
                                            default:{
                                                column = ExpressionEngine.PAGE_COLUMN_COUNT;
                                                row = ExpressionEngine.PAGE_LINE_COUNT;
                                                break;
                                            }
                                        }
                                        if (motionEvent.getRawY() > rect.top && motionEvent.getRawY() < rect.bottom) {
                                            int xOff = (int) (motionEvent.getRawX() / (rect.right / column));
                                            int yOff = (int) ((motionEvent.getRawY() - rect.top) / ((rect.bottom - rect.top) / row));
                                            int position = xOff + column* yOff;
                                            //LogUtils.d(TAG, "--tabType :"+tabType+"-->position:"+position);
                                            View viewSub = ((GridView) view).getChildAt(position);
                                            if(viewSub==null)
                                                break;
                                            viewSub.getGlobalVisibleRect(rect);
                                            ExpressionModel expressionModel = listDats.get(position);
                                            emojiLayout.showExpressionPreview(expressionModel,rect);
                                        }
                                    }
                                }*/
                                break;
                            default:
                                break;
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    return false;
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void destroyItem(View collection, int position, Object paramObject) {
        // TODO Auto-generated method stub
        ((ViewGroup) collection).removeView((View)paramObject);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public void finishUpdate(View arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public Parcelable saveState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
        // TODO Auto-generated method stub

    }

}