package gyun.bo.voice_of_thousands_1;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RecordListItem extends FrameLayout {
	public RecordListItem(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
	}

	public RecordListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		// TODO Auto-generated constructor stub
	}

	TextView list_body;
	ImageView contentstatus, release;
	int history_id;

	private void setimg(int id){
		release = (ImageView)findViewById(R.id.list_new);
		release.setImageResource(id);
	}
	private void init() {
		// TODO Auto-generated method stub

		LayoutInflater.from(getContext()).inflate(R.layout.recordlistitem, this);
		list_body = (TextView) findViewById(R.id.list_body);
		contentstatus = (ImageView) findViewById(R.id.list_icon);
		release = (ImageView) findViewById(R.id.list_new);

	}

	RecordListItemData mData;

	public void setItemData(RecordListItemData data) {

		mData = data;

		history_id = data.historyid;
		contentstatus.setImageResource(data.statusimg); // »óÅÂ
		release.setImageResource(data.release); // New?
		release.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mData.release == R.drawable.ic_delete_black_48dp) {
				//	Toast.makeText(v.getContext(), "²ó²ó²¥²¥" + mData.historyid, Toast.LENGTH_SHORT).show();
					new VOT_Async().execute(NetworkDefineConstant.SERVER_URL_VOT_HISTORY_DELETE, "DELETE");
				}
			}
		});
		list_body.setText(data.body);

	}

	class VOT_Async extends AsyncTask<String, Void, String> {
		ProgressDialog dialog;
		StringBuilder SB = new StringBuilder();
		String result;

		protected void onPreExecute() {

			SB.append("historyid=" + history_id);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {

			try {
				HttpURLConnection urlConn = HttpConnectionManager
						.DeleteHttpURLConnection(NetworkDefineConstant.SERVER_URL_VOT_HISTORY_DELETE, "DELETE");
				OutputStream toServer = null;
				toServer = urlConn.getOutputStream();
				toServer.write(SB.toString().getBytes("UTF-8"));
				toServer.flush();
				urlConn.connect();
				result = "¼º°ø";
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = "½ÇÆÐ";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = "½ÇÆÐ";
			}

			return result;

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
		}

	}

}
