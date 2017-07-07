package gyun.bo.voice_of_thousands_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Login_Auth_Activity extends Activity {

	public String login1, login2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		login1 = intent.getStringExtra("login_way");
		login2 = intent.getStringExtra("login_token");
		
		if (login1 == null){
			Toast.makeText(getApplication(), "로그인 정보가 없음", Toast.LENGTH_SHORT).show();
		}
		else{
		new Login_Async().execute(NetworkDefineConstant.SERVER_URL_VOT_LOGIN, login1, login2);
		}
	}

	class Login_Async extends AsyncTask<String, Void, ArrayList<BookItemEntityObject>> {

		@Override
		protected ArrayList<BookItemEntityObject> doInBackground(String... params) {
			StringBuilder sb = null; 
			String result = null;
			HttpURLConnection Httpconn = null;
			OutputStream OutStream = null;
			InputStream InputStream = null;
			ByteArrayOutputStream bytearrOutStream = null;
			ArrayList<BookItemEntityObject> resultArray = null;
			try {
				Httpconn = HttpConnectionManager.postHttpURLConnection(params[0], "POST");
				OutStream = Httpconn.getOutputStream();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(OutStream, "UTF-8"));

				sb = new StringBuilder();

				sb.append(URLEncoder.encode("mode", "UTF-8"));
				sb.append("=");
				sb.append(URLEncoder.encode(params[1], "UTF-8"));// 나중에

				sb.append("&");
				sb.append(URLEncoder.encode("value", "UTF-8"));
				sb.append("=");
				sb.append(URLEncoder.encode(params[2], "UTF-8"));

				sb.append("&");
				sb.append(URLEncoder.encode("registrationid", "UTF-8"));
				sb.append("=");
				sb.append(URLEncoder.encode(VOTApplication.prefs.getString("GCM", "NULL"), "UTF-8"));

				writer.write(sb.toString());
				writer.flush();
				writer.close();
				OutStream.close();

				//Log.e("1", "1");
				Httpconn.connect();
				//Log.e("2", "2");
				String response;

			//	Log.e("3", "3");
				if (OutStream != null) {
					OutStream.close();
				}
				//Log.e("4", "4");
				int responseCode = Httpconn.getResponseCode();
				//Log.e("55", responseCode + "good");
				if (responseCode >= 200 && responseCode < 300) {

					//Log.e(responseCode + "", responseCode + "");

					InputStream = Httpconn.getInputStream();
					bytearrOutStream = new ByteArrayOutputStream();

					byte[] byteBuffer = new byte[3024];
					byte[] byteData = null;
					int nLength = 0;

					while ((nLength = InputStream.read(byteBuffer, 0, byteBuffer.length)) != -1) {

						bytearrOutStream.write(byteBuffer, 0, nLength);
					}

					byteData = bytearrOutStream.toByteArray();
					response = new String(byteData);// 여기서 부터 시작

					JSONObject jsonobject = new JSONObject(response);

					String Result1 = jsonobject.getString("result");

					resultArray = new ArrayList<BookItemEntityObject>();

					BookItemEntityObject entity = new BookItemEntityObject();

					entity.result = Result1;

					if (Result1.equalsIgnoreCase("success")) { // result ==
																// success 일 경우.
						JSONObject member_info = jsonobject.getJSONObject("member_info");
						entity.member_id = member_info.getInt("member_id");
						entity.member_name = member_info.getString("member_name");
						entity.member_gender = member_info.getString("member_gender");
						entity.member_image = member_info.getString("member_image");
						entity.participation_count = member_info.getInt("participation_count");
					}

					else if (Result1.equalsIgnoreCase("join")) {/// Result1 ==
																/// join일 경우.
						entity.member_id = jsonobject.getInt("member_id");
					}

					else if (Result1.equalsIgnoreCase("fail")) { // fail
						entity.reject_reason = jsonobject.getString("result_msg");
					}

					resultArray.add(entity);

				}
			}

			catch (Exception e) {

			} finally {
				if (OutStream != null) {
					try {
						OutStream.close();
					} catch (IOException ioe) {
					}
				}
				if (InputStream != null) {
					try {
						InputStream.close();
					} catch (IOException ioe) {
					}
				}
				if (Httpconn != null) {
					Httpconn.disconnect(); // 자원 반납

				}
			}

			return resultArray;
		}

		@Override
		protected void onPostExecute(ArrayList<BookItemEntityObject> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				String Result = result.get(0).result;

				if (Result.equalsIgnoreCase("success")) {

					Intent intent = new Intent(Login_Auth_Activity.this, Voice_Of_Thousands.class);
					intent.putExtra("member_id", result.get(0).member_id);
					intent.putExtra("member_name", result.get(0).member_name);
					intent.putExtra("member_gender", result.get(0).member_gender);
					intent.putExtra("member_image", result.get(0).member_image);
					intent.putExtra("participation_count", result.get(0).participation_count);
					// intent.putExtra("registrationid",
					// VOTApplication.prefs.getString("GCM", "NULL"));
					startActivity(intent);

				} else if (Result.equalsIgnoreCase("join")) {

					Intent intent = new Intent(Login_Auth_Activity.this, VOT_join_page.class);
					intent.putExtra("member_id", result.get(0).member_id);

					intent.putExtra("login_way", login1);
					intent.putExtra("login_token", login2);
					startActivity(intent);

				} else if (Result.equalsIgnoreCase("fail")) {

					Toast.makeText(getApplication(), "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();

					startActivity(new Intent(Login_Auth_Activity.this, VOT_Login_Page.class));
				}else{

					Toast.makeText(getApplication(), "서버에서 정보를 받아올수 없습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
					startActivity(new Intent(Login_Auth_Activity.this, VOT_Login_Page.class));
				}
				finish();
			}else{

				Toast.makeText(getApplication(), "서버에서 정보를 받아올수 없습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(Login_Auth_Activity.this, VOT_Login_Page.class));
			}
		}
	}

}
