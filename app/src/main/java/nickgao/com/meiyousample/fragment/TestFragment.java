package nickgao.com.meiyousample.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nickgao.com.meiyousample.R;

/**
 * 个人主页内容的adapter
 *
 * @author kahn chaisen@xiaoyouzi.com
 * @version 1.0
 * @ClassName
 * @date 2017/2/24
 * @Description
 */
public class TestFragment extends Fragment {

    protected Activity mActivity;
    private View rootView;
    private TextView mTextView;
    private String content;


    public static Fragment newInstance(int id, int type, String name, int position, int currentSelectedPage, String url) {
        TestFragment classifyFragment = create(name);

        Bundle bundle = new Bundle();
        bundle.putInt("classifyId", id);
        bundle.putInt("classifyType", type);
        bundle.putString("classifyName", name);
        bundle.putInt("position", position);
        bundle.putInt("currentSelectedPage", currentSelectedPage);
        bundle.putString("url", url);
        classifyFragment.setArguments(bundle);
        return classifyFragment;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.test_fragment,null);
        mTextView = (TextView) rootView.findViewById(R.id.text1);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }



    private static TestFragment create(String name) {
        TestFragment fragment = new TestFragment();
        fragment.setContent(name);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTextView.setText(this.content);

    }


}
