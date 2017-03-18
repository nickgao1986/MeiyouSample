package nickgao.com.meiyousample.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.skin.ViewFactory;

import java.util.List;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.model.NewsDetailRecommendModel;

/**
 * Created by gaoyoujian on 2017/3/18.
 */

public class NewsTextRecommendAdapter extends BaseAdapter {
    private Activity mActivity;
    private List<NewsDetailRecommendModel> mModels;

    public NewsTextRecommendAdapter(Activity activity, List<NewsDetailRecommendModel> models) {
        mActivity = activity;
        mModels = models;
    }

    @Override
    public int getCount() {
        return mModels == null ? 0 : mModels.size();
    }

    @Override
    public Object getItem(int position) {
        return mModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //因为是给LinearListView用的，不复用
        convertView = ViewFactory.from(mActivity).getLayoutInflater().inflate(R.layout.layout_news_text_recommend_item, null);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
        ImageView ivDivider = (ImageView) convertView.findViewById(R.id.iv_divider);

        final NewsDetailRecommendModel model = mModels.get(position);
        tvTitle.setText(model.title);
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        if (position == getCount() - 1) {
            ivDivider.setVisibility(View.GONE);
        } else {
            ivDivider.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
}
