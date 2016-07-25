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

package uk.ac.mdx.cs.ie.acontextlib.envir.weather.source;

import java.net.HttpURLConnection;

public class HttpUtils {

    private HttpUtils() {

    }

    public static String getCharset(HttpURLConnection conn) {
        return getCharset(conn.getContentType().toString());
    }

    static String getCharset(String contentType) {
        if (contentType == null) {
            return HttpWeatherSource.ENCODING;
        }
        int charsetPos = contentType.indexOf(HttpWeatherSource.CHARSET);
        if (charsetPos < 0) {
            return HttpWeatherSource.ENCODING;
        }
        charsetPos += HttpWeatherSource.CHARSET.length();
        int endPos = contentType.indexOf(';', charsetPos);
        if (endPos < 0) {
            endPos = contentType.length();
        }
        return contentType.substring(charsetPos, endPos);
    }
}
