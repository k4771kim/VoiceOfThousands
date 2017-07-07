/*
 *  다양한 HTTP 통신을 관리하는 객체
 *  author PYO IN SOO
 */
package gyun.bo.voice_of_thousands_1;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class HttpConnectionManager {
	private static final String DEBUG_TAG = "HttpConnectionManager";

	/*
	 * 요청에 쿼리스특림이 없고 응답메세지만 받을 때...
	 */
	public static HttpURLConnection getHttpURLConnection(String targetURL, String reqMethod) {
		HttpURLConnection httpConnection = null;
		try {
			URL url = new URL(targetURL);

			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setRequestMethod(reqMethod);
			httpConnection.setDoInput(true);
			httpConnection.setConnectTimeout(15000);

		} catch (Exception e) {
			Log.e(DEBUG_TAG, "getHttpURLConnection() -- 에러 발생 -- ", e);
		}
		return httpConnection;
	}

	public static HttpURLConnection postHttpURLConnection(String targetURL, String reqMethod) {
		HttpURLConnection httpConnection = null;
		try {
			URL url = new URL(targetURL);

			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setRequestMethod(reqMethod);
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setReadTimeout(15000);

			httpConnection.setConnectTimeout(15000);

			httpConnection.setRequestProperty("Connection", "Keep-Alive");
			// httpConnection.setRequestProperty("Content-Type",
			// "application/json; charset=utf-8");
			/*
			 * httpConnection.setRequestProperty("Content-Type",
			 * "application/x-www-form-urlencoded");
			 */

		} catch (Exception e) {
			Log.e(DEBUG_TAG, "getHttpURLConnection() -- 에러 발생 -- ", e);
		}
		return httpConnection;
	}

	public static HttpURLConnection DeleteHttpURLConnection(String targetURL, String reqMethod) {
		HttpURLConnection httpConnection = null;
		try {
			URL url = new URL(targetURL);

			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpConnection.setRequestMethod(reqMethod);
			httpConnection.setConnectTimeout(15000);

		} catch (Exception e) {
			Log.e(DEBUG_TAG, "getHttpURLConnection() -- 에러 발생 -- ", e);
		}
		return httpConnection;
	}

	public static void setDismissConnection(HttpURLConnection returnedConn, Reader inR, Writer outW) {

		if (inR != null) {
			try {
				inR.close();
			} catch (IOException ioe) {

			}
		}
		if (outW != null) {
			try {
				outW.close();
			} catch (IOException ioe) {

			}
		}
		if (returnedConn != null) {

			returnedConn.disconnect();
		}
	}
}
