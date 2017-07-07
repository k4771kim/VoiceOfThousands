package gyun.bo.voice_of_thousands_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import android.accounts.NetworkErrorException;
import android.util.Log;

public class VOTHelperHandler {

	public static ArrayList<BookItemEntityObject> BookList(String server, String reqMethod,
			StringBuilder queryStringParams) {
		final String SV = server;
		HttpURLConnection urlConn = null;
		ArrayList<BookItemEntityObject> list = null;
		BufferedReader jsonStreamData = null;	
		InputStream fromServer = null;
		OutputStream toServer = null;
		// 서버로 정보 요청.

		if (reqMethod == "GET" && queryStringParams != null) {

			server = server + queryStringParams.toString();

		}

		try {

			urlConn = HttpConnectionManager.getHttpURLConnection(server, reqMethod);

			if (reqMethod == "POST" && queryStringParams != null) { // T.T
				urlConn.setDoOutput(true);
				toServer = urlConn.getOutputStream();
				toServer.write(queryStringParams.toString().getBytes("UTF-8"));
				toServer.flush();

			}

			/*
			 * toServer = urlConn.getOutputStream();
			 * toServer.write(queryStringParams.toString().getBytes("UTF-8"));
			 * toServer.flush();
			 */

			/*
			 * 응답보디부분을 스트림으로 연결하여 바이트의 제이슨을 문자스트림으로 변환한다.
			 */
			jsonStreamData = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			int responseCode = urlConn.getResponseCode();
			if (responseCode >= 200 && responseCode < 300) {

				jsonStreamData = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			}

			else {
				return null;
			}
			String line = "";
			StringBuilder buf = new StringBuilder();
			while ((line = jsonStreamData.readLine()) != null) {

				buf.append(line);

			}
			//Log.e("VOTHelsadfasdfasdst", buf + "");

			if (SV == NetworkDefineConstant.SERVER_URL_VOT_READ_LIST) {
				list = ParseDataParseHandler.getJSONReadBookList(buf);
			} else if (SV == NetworkDefineConstant.SERVER_URL_VOT_LISTEN_LIST) {
				list = ParseDataParseHandler.getJSONListenBookList(buf);
			} else if (SV == NetworkDefineConstant.SERVER_URL_VOT_MEMBER_INFO) {
				list = ParseDataParseHandler.getJSONMemberInfo(buf);

			} else if (SV == NetworkDefineConstant.SERVER_URL_VOT_LISTEN_MODE) { // 음성듣기
																					// 요청.
				list = ParseDataParseHandler.getJSONListenMode(buf);
			}

			else if (SV == NetworkDefineConstant.SERVER_URL_VOT_LOGIN) {
				list = ParseDataParseHandler.getJSONMemberLOGIN(buf);
			} else if (SV == NetworkDefineConstant.SERVER_URL_VOT_BOOKING) {
				list = ParseDataParseHandler.getJSONBooking(buf);
			} else if (SV == NetworkDefineConstant.SERVER_URL_VOT_CONNECTIONEND) {
				if (responseCode >= 200 && responseCode < 300) {
					list = new ArrayList<BookItemEntityObject>(1);
					BookItemEntityObject ab = new BookItemEntityObject();
					ab.content_text = "success";
					list.add(ab);

				}
			} else if (SV == NetworkDefineConstant.SERVER_URL_VOT_LISTENBOOK_INFO) {

				list = ParseDataParseHandler.getJSONListenBookInfo(buf);
			} else if (SV == NetworkDefineConstant.SERVER_URL_VOT_SAMPLE) {
				list = ParseDataParseHandler.getJSONSample(buf);
			} else if (SV == NetworkDefineConstant.SERVER_URL_VOT_HISTORY_DELETE) {

				list = ParseDataParseHandler.DeleteHistory(buf);
			}else if (SV == NetworkDefineConstant.SERVER_URL_VOT_DUPL_CHECK){
				list = ParseDataParseHandler.getJsonDupcheck(buf);
				
			}else if (SV == NetworkDefineConstant.SERVER_URL_VOT_MP3_Download){
					list = ParseDataParseHandler.getJsonMP3Download(buf);
			}

		} catch (IOException ioe)

		{

			Log.e("VOTHelperHandler.BookList", ioe.toString());
			if (SV == NetworkDefineConstant.SERVER_URL_VOT_LISTEN_LIST || SV == NetworkDefineConstant.SERVER_URL_VOT_MEMBER_INFO){
			list= new ArrayList<BookItemEntityObject>();
			BookItemEntityObject entity = new BookItemEntityObject();
			entity.total_cnum = 0;
			entity.result = "fail";
			list.add(entity);
			
				
			}
		} finally

		{
			HttpConnectionManager.setDismissConnection(urlConn, jsonStreamData, null);
		}

		/*
		 * public static ArrayList<RecordEntityObject> RecUpload(int server) {
		 * 
		 * ArrayList<RecordEntityObject> Rec = null; HttpURLConnection urlConn =
		 * null; File file = new
		 * File(Environment.getExternalStorageDirectory().getAbsolutePath() +
		 * "/VOT_Record.mp4"); FileInputStream fromStreamData = new
		 * FileInputStream(file); FileOutputStream toStreamData = null;
		 * 
		 * try {
		 * 
		 * urlConn =
		 * HttpConnectionManager.getHttpURLConnection(NetworkDefineConstant.
		 * SERVER_URL_VOT_READ_LIST, "GET");
		 * 
		 * } catch (Exception e) { Log.e("VOTHelperHandler.RecUpload",
		 * e.toString()); } finally {
		 * HttpConnectionManager.setDismissConnection(urlConn, fromStreamData,
		 * toStreamData); }
		 * 
		 * return Rec; }
		 */
		return list;

	}

}