package nickgao.com.meiyousample.network;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.util.concurrent.TimeUnit;

import nickgao.com.framework.utils.LogUtils;


/**
 * Default implementation of a client-side RSET HTTP connection.
 */
class RestHttpClient {
    /**
     * Define logging tag.
     */
    private static final String TAG = "[RC]RestHttpClient";

    /**
     * Keeps HTTP client.
     */
    private RCMHttpClient client;

    /**
     * Keeps the connection manger for the client.
     */
    private ClientConnectionManager manager;



    /**
     * Keeps logging tag for the client.
     */
    private String logTag;


    private RestHttpClient() {
    }

    /**
     * Returns <code>DefaultHttpClient</code> for making a HTTP request.
     *
     * @return <code>DefaultHttpClient</code> for making a HTTP request
     */
    DefaultHttpClient getHttpClient() {
        cleanup();
        return client;
    }

    /**
     * Defines the timeout in milliseconds until a connection is established.
     */
    private static final int CONNECTION_TIMEOUT = 30 * 1000;

    /**
     * Defines the default socket timeout (SO_TIMEOUT) in milliseconds which is the timeout for waiting for data.
     */
    private static final int DATA_WAITING_TIMEOUT = 120 * 1000;

    /**
     * Defines the idle time in minutes of connections to be closed
     */
    private static final long IDLE_CONNECTIONS_CLEAN_UP = 10 * 60;

    /**
     * Defines the maximum number of connections allowed.
     */
    private static final int MAX_CONNECTIONS = 32;


    static RestHttpClient getClient() {
        try {
            HttpParams params = new BasicHttpParams();
           // params.setParameter(HttpRequestFactory.TRANSFER_ENCODING, HttpRequestFactory.CHUNKED);

            ConnManagerParams.setMaxTotalConnections(params, MAX_CONNECTIONS);

            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

            HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, DATA_WAITING_TIMEOUT);

            ConnManagerParams.setTimeout(params, CONNECTION_TIMEOUT);

            ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRoute() {
                @Override
                public int getMaxForRoute(HttpRoute httproute) {
                    return MAX_CONNECTIONS;
                }
            });

            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

            RestHttpClient restHttpClient = new RestHttpClient();
            restHttpClient.manager = new ThreadSafeClientConnManager(params, schemeRegistry);

            if (restHttpClient.manager == null)  {
                LogUtils.e(TAG, "getClient : manager is null.");
                return null;
            }

            restHttpClient.client = new RCMHttpClient(restHttpClient.manager, params);

            if (restHttpClient.client == null)  {
                LogUtils.e(TAG, "getClient : client is null.");

                return null;
            }

           // setUserAgent(restHttpClient.client);

            return restHttpClient;
        } catch (Throwable th) {
            LogUtils.e(TAG, "getClient : exception : " + th.toString());
        }
        return null;
    }

    /**
     * Set User-Agent header.
     *
     * @param client
     *            the HTTP client to set User-Agent header.
     */

    /**
     * Cleans-up expired and idle (for long time) connections.
     */
    void cleanup() {
        try {
            if (manager != null) {
                manager.closeExpiredConnections();
                manager.closeIdleConnections(IDLE_CONNECTIONS_CLEAN_UP, TimeUnit.SECONDS);
            }
        } catch (Throwable th) {
            LogUtils.e(logTag, "cleanup : exception : " + th.toString());
        }
    }

    /**
     * Shutdown all connections (instances at HttpClient) and release memory.
     */
    void shutdown() {
        LogUtils.d(logTag, "shutdown");
        try {
            if (manager != null) {
                cleanup();
                manager.shutdown();
                manager = null;
                client = null;
            }
        } catch (Throwable th) {
            LogUtils.e(logTag, "shutdown : exception : " + th.toString());
        }
    }
}
