package nickgao.com.meiyousample.network;

import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 * Created by gaoyoujian on 2017/3/23.
 */

public class RCMHttpClient extends DefaultHttpClient {

    public RCMHttpClient(final ClientConnectionManager conman, final HttpParams params) {
        super(conman, params);
    }

    @Override
    protected HttpContext createHttpContext() {

        HttpContext context = new BasicHttpContext();
        context.setAttribute(ClientContext.AUTHSCHEME_REGISTRY, super.getAuthSchemes());
        context.setAttribute(ClientContext.COOKIESPEC_REGISTRY, super.getCookieSpecs());
        context.setAttribute(ClientContext.CREDS_PROVIDER, 		super.getCredentialsProvider());

        return context;

    }

}
