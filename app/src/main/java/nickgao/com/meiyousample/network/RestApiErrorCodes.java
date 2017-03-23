package nickgao.com.meiyousample.network;

/**
 * Created by gaoyoujian on 2017/3/23.
 */

public final class RestApiErrorCodes {

    /**
     * Generic success code.
     */
    public static final int OK       = 0;
    public static final int NO_ERROR = 0;

    /**
     * Defines invalid request.
     */
    public static final int INVALID_REQUEST = -201;
    public static final int CLIENT_INVALID_ERROR = 999;
    public static final int INVALID_SESSION_STATE = -202;

    public static final int NETWORK_NOT_AVAILABLE = -1;

    /**
     * Defines invalid REST Session state.
     */
    public static final int RESPONSE_INVALID = -203;
    public static final int UNKNOWN_STATUS_CODE = -999;
    public static final int RESPONSE_INVALID_FORMAT = -203;

    /**
     * Defines status code when an error happened during HTTP response processing.
     */
    public static final int RESPONSE_PROCESSING_ERROR = -205;

    public static final int RESPONSE_HTTP_STATUS_ERROR = -204;

    public static final int OUT_OF_MEMORY_ERROR = -208;

    public static final int CONNECTION_ERROR = -206;


}
