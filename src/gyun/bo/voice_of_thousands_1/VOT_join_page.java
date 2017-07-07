package gyun.bo.voice_of_thousands_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class VOT_join_page extends Activity {

	RadioButton gender_M, gender_W;
	RadioGroup gendergroup;
	public String gender, membername, login_token, login_way;
	Button join_commit, join_cancel, duple_check_btn;
	public EditText join_nick;
	String bString;
	boolean bFlag;
	public int memberid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.join_page);

		Intent intent = getIntent();
		memberid = intent.getIntExtra("member_id", 0);
		login_token = intent.getStringExtra("login_token");
		login_way = intent.getStringExtra("login_way");
		join_nick = (EditText) findViewById(R.id.join_nick);
		InputFilter[] filters = new InputFilter[] { new ByteLengthFilter(10, "KSC5601") };
		join_nick.setFilters(filters);
		// join_nick.setText(VOT_Login_Page.SNS_Name);//
		duple_check_btn = (Button) findViewById(R.id.duple_check_btn);

		gendergroup = (RadioGroup) findViewById(R.id.joing_gender);

		gendergroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == R.id.join_gender_M) {
					gender = "male";
				} else if (checkedId == R.id.join_gender_W) {
					gender = "female";
				}
			}

		});

		duple_check_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new VOT_Dupl_Async().execute();

			}
		});
		join_commit = (Button) findViewById(R.id.join_commit);
		join_cancel = (Button) findViewById(R.id.join_cancel);
		join_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(VOT_join_page.this, VOT_Login_Page.class);
				startActivity(intent);
				finish();

			}
		});
		join_commit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (join_nick.getText() != null) {

					if (gender == null) {
						Toast.makeText(getApplication(), "성별을 입력해 주세요.", Toast.LENGTH_SHORT).show();
					} else if (bFlag && join_nick.getText().toString().equalsIgnoreCase(bString)) {

						membername = join_nick.getText().toString();
						new join_Async().execute(NetworkDefineConstant.SERVER_URL_VOT_JOIN, membername, gender,
								memberid + "");
					} else {

						Toast.makeText(getApplication(), "중복 확인이 필요합니다.", Toast.LENGTH_SHORT).show();

					}
				}

			}
		});

	}

	class join_Async extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = null;
			StringBuilder sb = null; // TODO Auto-generated method stub
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

				sb.append(URLEncoder.encode("name", "UTF-8"));
				sb.append("=");
				sb.append(URLEncoder.encode(params[1], "UTF-8"));// 나중에

				sb.append("&");
				sb.append(URLEncoder.encode("gender", "UTF-8"));
				sb.append("=");
				sb.append(URLEncoder.encode(params[2], "UTF-8"));

				sb.append("&");
				sb.append(URLEncoder.encode("memberid", "UTF-8"));
				sb.append("=");
				sb.append(URLEncoder.encode(params[3], "UTF-8"));

				writer.write(sb.toString());
				writer.flush();
				writer.close();
				OutStream.close();

			//	Log.e("1", "1");
				Httpconn.connect();
			//	Log.e("2", "2");
				String response;

			//	Log.e("3", "3");
				if (OutStream != null) {
					OutStream.close();
				}
			//	Log.e("4", "4");
				int responseCode = Httpconn.getResponseCode();
			//	Log.e("55", responseCode + "good");
				if (responseCode >= 200 && responseCode < 300) {

				//	Log.e(responseCode + "", responseCode + "");

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

					result = jsonobject.getString("result");

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

			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result!=null){
				if (result.equalsIgnoreCase("success")) {
				Toast.makeText(getApplication(), "회원가입에 성공하였습니다", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(VOT_join_page.this, Login_Auth_Activity.class);

				intent.putExtra("login_way", login_way);
				intent.putExtra("login_token", login_token);

				startActivity(intent);
				finish();

			} else {

				Toast.makeText(getApplication(), "회원가입에 실패하였습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();

			}}
			else{
				Toast.makeText(getApplication(), "네트워크 연결이 원활하지 않습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
			}

		}

	}

	public class ByteLengthFilter implements InputFilter {

		private String mCharset; // 인코딩 문자셋

		protected int mMaxByte; // 입력가능한 최대 바이트 길이

		public ByteLengthFilter(int maxbyte, String charset) {
			this.mMaxByte = maxbyte;
			this.mCharset = charset;
		}

		/**
		 * 이 메소드는 입력/삭제 및 붙여넣기/잘라내기할 때마다 실행된다.
		 *
		 * - source : 새로 입력/붙여넣기 되는 문자열(삭제/잘라내기 시에는 "") - dest : 변경 전 원래 문자열
		 */

		/**
		 * 입력가능한 최대 문자 길이(최대 바이트 길이와 다름!).
		 */
		protected int calculateMaxLength(String expected) {
			return mMaxByte - (getByteLength(expected) - expected.length());
		}

		/**
		 * 문자열의 바이트 길이. 인코딩 문자셋에 따라 바이트 길이 달라짐.
		 * 
		 * @param str
		 * @return
		 */
		private int getByteLength(String str) {
			try {
				return str.getBytes(mCharset).length;
			} catch (UnsupportedEncodingException e) {
				// e.printStackTrace();
			}
			return 0;
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			// TODO Auto-generated method stub
			// 변경 후 예상되는 문자열
			String expected = new String();
			expected += dest.subSequence(0, dstart);
			expected += source.subSequence(start, end);
			expected += dest.subSequence(dend, dest.length());

			int keep = calculateMaxLength(expected) - (dest.length() - (dend - dstart));

			if (keep <= 0) {
				return ""; // source 입력 불가(원래 문자열 변경 없음)
			} else if (keep >= end - start) {
				return null; // keep original. source 그대로 허용
			} else {
				return source.subSequence(start, start + keep); // source중 일부만
																// 입력 허용
			}
		}
	}

	public class VOT_Dupl_Async extends AsyncTask<String, Void, String> {

		String nickname;
		StringBuilder sb;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			nickname = join_nick.getText().toString();

			if (!nickname.equalsIgnoreCase("")) {
				sb = new StringBuilder();
				try {
					sb.append("?value=");
					sb.append(URLEncoder.encode(nickname, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// sb.append("?value=" + nickname);
			} else {
				cancel(true);
				Toast.makeText(getApplication(), "닉네임을 입력해 주세요.", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String server = NetworkDefineConstant.SERVER_URL_VOT_DUPL_CHECK;

			final String SV = server;
			HttpURLConnection urlConn = null;
			String dupresult = null;
			BufferedReader jsonStreamData = null;
			InputStream fromServer = null;
			server = server + sb.toString();
			try {

				urlConn = HttpConnectionManager.getHttpURLConnection(server, "GET");

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

				JSONObject mainobj = new JSONObject(buf.toString());

				dupresult = mainobj.getString("result");

			} catch (IOException ioe)

			{

				Log.e("VOTHelperHandler.BookList", ioe.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally

			{
				HttpConnectionManager.setDismissConnection(urlConn, jsonStreamData, null);
			}
			return dupresult;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.equalsIgnoreCase("dupl_yes")) {

					Toast.makeText(getApplication(), "사용이 불가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
					bFlag = false;

				} else if (result.equalsIgnoreCase("dupl_no")) {
					Toast.makeText(getApplication(), "사용이 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
					bFlag = true;
					bString = nickname;
				} else {
					Toast.makeText(getApplication(), "요청이 올바르지 않습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();

				}
			} else {
				Toast.makeText(getApplication(), "서버와의 연결이 원활하지 않습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
			}

			super.onPostExecute(result);
		}

	}

}
