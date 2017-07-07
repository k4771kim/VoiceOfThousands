package gyun.bo.voice_of_thousands_1.popup;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.util.Log;
import gyun.bo.voice_of_thousands_1.BookItemEntityObject;
import gyun.bo.voice_of_thousands_1.NetworkDefineConstant;
import gyun.bo.voice_of_thousands_1.Record_Fragment;
import gyun.bo.voice_of_thousands_1.Voice_Of_Thousands;

public class RecordHandler {

	public static ArrayList<BookItemEntityObject> InserRecord(File rec) {
		String resultstring = null;

		HttpClient httpClient = new DefaultHttpClient();
		HttpParams params = httpClient.getParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, new ProtocolVersion("HTTP", 1, 1));
		HttpPost httpPost = new HttpPost(NetworkDefineConstant.SERVER_URL_VOT_UPLOAD_RECORD);

		File file = rec;
		String filename = null;
		ArrayList<BookItemEntityObject> sendresult = new ArrayList<BookItemEntityObject>();
		BookItemEntityObject entity = new BookItemEntityObject();
		if (file.exists()) {
			MultipartEntity multipartEntity = new MultipartEntity();

			String[] filenameparts = file.getName().split("\\.");
			filename = "VOT_Record.wav";
			JSONObject jsonObject = new JSONObject();
			try {

				multipartEntity.addPart("memberid", new StringBody(Voice_Of_Thousands.member_id+""));
				multipartEntity.addPart("bookid", new StringBody(Record_Fragment.bookid + ""));
				multipartEntity.addPart("contentnum", new StringBody(Record_Fragment.currentpart + ""));
				// multipartEntity.addPart("voice", new FileBody(file, filename,
				// "application/octet-stream", null));
				multipartEntity.addPart("voice", new FileBody(file, filename, "audio/x-wav", null));
				httpPost.setEntity(multipartEntity);
				HttpResponse httpResponse = httpClient.execute(httpPost);
			//	Log.d("HTTPRESPONSe", httpResponse.toString());

				HttpEntity responseBody = null;
				StringBuilder sb = new StringBuilder();
				int responseCode = httpResponse.getStatusLine().getStatusCode();
				if (responseCode >= 200 && responseCode < 300) {
					responseBody = httpResponse.getEntity();

					BufferedReader rd = new BufferedReader(
							new InputStreamReader(httpResponse.getEntity().getContent()));

					StringBuffer result = new StringBuffer();
					String line = "";
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}

					JSONObject o = new JSONObject(result.toString());
				//	Log.e("로그", result.toString());
					entity.result = o.getString("result");
					//여기에 남은 카운트 횟수 추가.
					sendresult.add(entity);

				}

			} catch (Exception e) {
				e.printStackTrace();
				entity.result = "fail";
				sendresult.add(entity);
			}
		}

		return sendresult;

	}

}
