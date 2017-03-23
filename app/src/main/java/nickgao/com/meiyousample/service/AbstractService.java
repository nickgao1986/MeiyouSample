package nickgao.com.meiyousample.service;

/**
 * Created by gaoyoujian on 2017/3/23.
 */
public abstract class AbstractService {

    protected String TAG;

    protected IRequestFactory mRequestFactory;
    protected RestRequestListener mListener;

    public void setListener(RestRequestListener listener) {
        this.mListener = listener;
    }

    public AbstractService(IRequestFactory requestFactory) {
        TAG = this.getClass().getSimpleName();
        mRequestFactory = requestFactory;
    }

}
