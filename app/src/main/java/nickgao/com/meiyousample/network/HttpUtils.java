package nickgao.com.meiyousample.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;

/**
 * Created by gaoyoujian on 2017/3/23.
 */

public class HttpUtils {


    public static final String CONTENT_TYPE_NAME       		= "Content-Type";
    public static final String JSON_CONTENT_TYPE       		= "application/json";
    public static final String MIXED_CONTENT_TYPE       	= "multipart/mixed";
    public static final String PLAIN_TEXT_CONTENT_TYPE 		= "text/plain";
    public static final String XML_TEXT_CONTENT_TYPE   		= "text/xml";
    public static final String XML_APP_CONTENT_TYPE    		= "application/xml";
    public static final String STREAM_CONTENT_TYPE      	= "application/octet-stream";
    public static final String TRANSFER_ENCODING 			= "Transfer-Encoding";

    /**
     * Helper reader for logging.
     */
    public static class HttpResponseLogger extends InputStreamReader {
        /**
         * Keeps builder for accumulating log.
         */
        private StringBuilder sBuilder = new StringBuilder();

        /**
         * Defines max. length of the builder.
         */
        private long maxLen;

        /**
         * Constructs a new reader.
         *
         * @param in
         *            the InputStream from which to read characters.
         * @param enc
         *            identifies the character converter to use
         * @param maxLen
         *            defines max. length to be logged
         * @throws UnsupportedEncodingException
         *             if the encoding specified by enc cannot be found.
         */
        public HttpResponseLogger(InputStream in, String enc, long maxLen) throws UnsupportedEncodingException {
            super(in, enc);
            this.maxLen = maxLen;
        }

        /**
         * Constructs a new reader.
         *
         * @param in
         *            the InputStream from which to read characters.
         * @param maxLen
         *            defines max. length to be logged
         */
        public HttpResponseLogger(InputStream in, long maxLen) {
            super(in);
            this.maxLen = maxLen;
        }

        /**
         * Retrieves logged content.
         *
         * @return the logged content.
         */
        public String getContent() {
            return sBuilder.toString();
        }

        @Override
        public void close() throws IOException {
            super.close();
        }

        @Override
        public String getEncoding() {
            return super.getEncoding();
        }

        @Override
        public int read() throws IOException {
            int r = super.read();
            if (r != -1 && (sBuilder.length() < maxLen)) {
                sBuilder.append((char)r);
            }
            return r;
        }

        @Override
        public int read(char[] buffer, int offset, int length) throws IOException {
            int r = super.read(buffer, offset, length);
            if (r != -1 && (sBuilder.length() < maxLen)) {
                sBuilder.append(buffer, offset, r);
            }
            return r;
        }

        @Override
        public boolean ready() throws IOException {
            return super.ready();
        }

        @Override
        public void mark(int readLimit) throws IOException {
            super.mark(readLimit);
        }

        @Override
        public boolean markSupported() {
            return super.markSupported();
        }

        @Override
        public int read(char[] buf) throws IOException {
            int r = super.read(buf);
            if (r != -1 && (sBuilder.length() < maxLen)) {
                sBuilder.append(buf, 0, r);
            }
            return r;
        }

        @Override
        public int read(CharBuffer target) throws IOException {
            int r = super.read(target);
            if (r != -1 && (sBuilder.length() < maxLen)) {
                sBuilder.append(target, 0, r);
            }
            return r;
        }

        @Override
        public void reset() throws IOException {
            super.reset();
        }

        @Override
        public long skip(long charCount) throws IOException {
            return super.skip(charCount);
        }
    }
}
