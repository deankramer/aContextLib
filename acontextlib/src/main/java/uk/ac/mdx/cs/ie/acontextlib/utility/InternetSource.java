/*
 * Copyright 2016 Middlesex University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.mdx.cs.ie.acontextlib.utility;

import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import uk.ac.mdx.cs.ie.acontextlib.ContextException;

/**
 * Abstract class for dealing with internet based data sources
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */

public abstract class InternetSource {

    public static final String ENCODING = "UTF-8";

    private static final String CHARSET = "charset=";

    private static final int HTTP_STATUS_OK = 200;

    static final String USER_AGENT = "aContextLib";

    private HttpURLConnection client;

    protected static final String LOG_TAG = "InternetSource";


    protected String readToString(String url) throws ContextException {
        StringBuilder result = new StringBuilder();
        InputStreamReader reader = getReaderForURL(url);
        char[] buf = new char[8192];
        try {
            int read = reader.read(buf);
            while (read >= 0) {
                result.append(buf, 0, read);
                read = reader.read(buf);
            }
        } catch (IOException e) {
            throw new ContextException("Error reading stream", e);
        }
        return result.toString();
    }


    protected InputStreamReader getReaderForURL(String urlString) throws ContextException {
        Log.d(LOG_TAG, "requesting " + urlString);
        try {
            URL url = new URL(urlString);
            client = (HttpURLConnection) url.openConnection();

            client.setRequestProperty("Accept-Encoding", "identity");
            client.addRequestProperty("User-Agent", USER_AGENT);

            prepareRequest(client);
        } catch (Exception e) {
            throw new ContextException("Having difficulty preparing HTTP Request", e);
        }

        String charset = ENCODING;
        try {
            client.connect();

            int status = client.getResponseCode();
            if (status != HTTP_STATUS_OK) {
                throw new ContextException("Invalid response from server: " +
                        String.valueOf(status));
            }

            charset = getCharset(client);

            InputStreamReader inputStream = new InputStreamReader(client.getInputStream(), charset);

            return inputStream;
        } catch (UnsupportedEncodingException uee) {
            throw new ContextException("Unsupported charset: " + charset, uee);
        } catch (IOException e) {
            throw new ContextException("Problem communicating with API", e);
        }
    }

    public static String getCharset(HttpURLConnection conn) {
        return getCharset(conn.getContentType().toString());
    }

    public static String getCharset(String contentType) {
        if (contentType == null) {
            return ENCODING;
        }
        int charsetPos = contentType.indexOf(CHARSET);
        if (charsetPos < 0) {
            return ENCODING;
        }
        charsetPos += CHARSET.length();
        int endPos = contentType.indexOf(';', charsetPos);
        if (endPos < 0) {
            endPos = contentType.length();
        }
        return contentType.substring(charsetPos, endPos);
    }

    protected abstract void prepareRequest(HttpURLConnection request);

}
