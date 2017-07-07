package gyun.bo.voice_of_thousands_1.popup;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import gyun.bo.voice_of_thousands_1.HttpConnectionManager;
import gyun.bo.voice_of_thousands_1.MyPage_Fragment;
import gyun.bo.voice_of_thousands_1.NetworkDefineConstant;
import gyun.bo.voice_of_thousands_1.R;
import gyun.bo.voice_of_thousands_1.Voice_Of_Thousands;
import gyun.bo.voice_of_thousands_1.drawmenu_fragment;

public class MypageDialog extends Dialog {
	Button commit;
	Context mcontext;
	EditText edittxt;
	private static String nick;
	public RecordDialog2 RD2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_mypage);
		edittxt = (EditText) findViewById(R.id.nick_edit);
		edittxt.setText(MyPage_Fragment.nick.getText().toString());
		InputFilter[] filters = new InputFilter[] {new ByteLengthFilter(10,"KSC5601")};
		edittxt.setFilters(filters);
		nick = MyPage_Fragment.nick.getText().toString();
		mcontext = getContext();
		setLayout();
	}

	protected MypageDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public MypageDialog(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		// TODO Auto-generated constructor stub
	}

	public MypageDialog(Context context, int theme) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		// TODO Auto-generated constructor stub
	}

	// layout

	private void setLayout() {

		// ���̾ƿ� ����.
		commit = (Button) findViewById(R.id.nick_commit);
		edittxt = (EditText) findViewById(R.id.nick_edit);

		commit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!edittxt.getText().toString().equalsIgnoreCase(nick) && !edittxt.getText().toString().equalsIgnoreCase(null)) {

			//		Toast.makeText(getContext(), "�г��� ����õ�..", Toast.LENGTH_SHORT).show();
					nick = edittxt.getText().toString();

					new nickchange().execute(NetworkDefineConstant.SERVER_URL_VOT_PROFILENAME_CHANGE, nick);

					// ������ �����ؼ� ����� �г����� �����������.

				} else {
			//		Toast.makeText(getContext(), "������� ����..", Toast.LENGTH_SHORT).show();
					onBackPressed();
				}

			}
		});

	}

	class nickchange extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... paramss) {

			StringBuilder sb = null; // TODO Auto-generated method stub
			String result = null;
			HttpURLConnection Httpconn = null;
			OutputStream OutStream = null;
			InputStream InputStream = null;
			ByteArrayOutputStream bytearrOutStream = null;

			try {
				Httpconn = HttpConnectionManager.postHttpURLConnection(paramss[0], "POST");
				OutStream = Httpconn.getOutputStream();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(OutStream, "UTF-8"));

				sb = new StringBuilder();

				sb.append(URLEncoder.encode("memberid", "UTF-8"));
				sb.append("=");
				sb.append(URLEncoder.encode(Voice_Of_Thousands.member_id + "", "UTF-8"));// ���߿�

				sb.append("&");
				sb.append(URLEncoder.encode("value", "UTF-8"));
				sb.append("=");
				sb.append(URLEncoder.encode(paramss[1], "UTF-8"));

				writer.write(sb.toString());
				writer.flush();
				writer.close();
				OutStream.close();

		//		Log.e("1", "1");
				Httpconn.connect();
		//		Log.e("2", "2");
				String response;

		//		Log.e("3", "3");
				if (OutStream != null) {
					OutStream.close();
				}
		//		Log.e("4", "4");
				int responseCode = Httpconn.getResponseCode();
		//		Log.e("55", responseCode + "good");
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
					response = new String(byteData);// ���⼭ ���� ����

					result = response.toString();/////////////////////////////////////
					////////////// ���Ⱑ
					/////////////////////////////////////////////////// ����
					/////////////////////////////////////////////////// ��Ʈ

					JSONObject jsonobject = new JSONObject(response);

					if (jsonobject.getString("result").equalsIgnoreCase("dupl_yes")) {

						result = "dupl_yes"; // �г� �ߺ���.

					} else if (jsonobject.getString("result").equalsIgnoreCase("success")) {// �г���
																							// //
																							// �ٲٱ⿡
																							// //
																							// ������.

						result = "success";
					} else if (jsonobject.getString("result").equalsIgnoreCase("fail")) { // �г���
																							// //
																							// �ٲٱ⿡
																							// //
																							// ������.
						result = "fail";
					}
				}
			}

			catch (

			Exception e)

			{

			} finally

			{
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
					Httpconn.disconnect(); // �ڿ� �ݳ�

				}
			}

			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result != null) {
				if (result.equalsIgnoreCase("success")) {

				//	Toast.makeText(getContext(), "�г��� �ٲٱ⿡ �����Ͽ����ϴ�.", Toast.LENGTH_SHORT).show();
					MyPage_Fragment.nick.setText(edittxt.getText().toString());
					Voice_Of_Thousands.member_name = edittxt.getText().toString();
					drawmenu_fragment.member_nick.setText(edittxt.getText().toString());
					MypageDialog.this.dismiss();
					
				} else if (result.equalsIgnoreCase("dupl_yes")) {
					Toast.makeText(getContext(), "�г��� �ߺ��Դϴ�. �ٸ� �г������� �õ����ּ���", Toast.LENGTH_SHORT).show();

				} else if (result.equalsIgnoreCase("fail")) {
					Toast.makeText(getContext(), "�г��� ���濡 �����Ͽ����ϴ�. �ٽ� �õ��� �ּ���", Toast.LENGTH_SHORT).show();
				}

			}
		}
	}

	
	public class ByteLengthFilter implements InputFilter {


	    private String mCharset; //���ڵ� ���ڼ�

	    protected int mMaxByte; // �Է°����� �ִ� ����Ʈ ����

	    public ByteLengthFilter(int maxbyte, String charset) {
	        this.mMaxByte = maxbyte;
	        this.mCharset = charset;
	    }

	    /**
	     * �� �޼ҵ�� �Է�/���� �� �ٿ��ֱ�/�߶󳻱��� ������ ����ȴ�.
	     *
	     * - source : ���� �Է�/�ٿ��ֱ� �Ǵ� ���ڿ�(����/�߶󳻱� �ÿ��� "")
	     * - dest : ���� �� ���� ���ڿ�
	     */


	    /**
	     * �Է°����� �ִ� ���� ����(�ִ� ����Ʈ ���̿� �ٸ�!).
	     */
	    protected int calculateMaxLength(String expected) {
	        return mMaxByte - (getByteLength(expected) - expected.length());
	    }    
	    
	    /**
	     * ���ڿ��� ����Ʈ ����.
	     * ���ڵ� ���ڼ¿� ���� ����Ʈ ���� �޶���.
	     * @param str
	     * @return
	     */
	    private int getByteLength(String str) {
	        try {
	            return str.getBytes(mCharset).length;
	        } catch (UnsupportedEncodingException e) {
	            //e.printStackTrace();
	        }
	        return 0;
	    }

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			// TODO Auto-generated method stub
			   // ���� �� ����Ǵ� ���ڿ�
	        String expected = new String();
	        expected += dest.subSequence(0, dstart);
	        expected += source.subSequence(start, end);
	        expected += dest.subSequence(dend, dest.length());

	        int keep = calculateMaxLength(expected) - (dest.length() - (dend - dstart));

	        if (keep <= 0) {
	            return ""; // source �Է� �Ұ�(���� ���ڿ� ���� ����)
	        } else if (keep >= end - start) {
	            return null; // keep original. source �״�� ���
	        } else {
	            return source.subSequence(start, start + keep); // source�� �Ϻθ� �Է� ���
	        }
		}
	}    
	
}
