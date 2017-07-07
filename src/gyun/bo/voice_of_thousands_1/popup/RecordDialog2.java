package gyun.bo.voice_of_thousands_1.popup;

import java.io.File;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import gyun.bo.voice_of_thousands_1.BookItemEntityObject;
import gyun.bo.voice_of_thousands_1.R;
import gyun.bo.voice_of_thousands_1.Record_Fragment;

public class RecordDialog2 extends Dialog {
	RecordDialog3 RD3;
	RecordDialog RD1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/*
		 * WindowManager.LayoutParams lpWindow = new
		 * WindowManager.LayoutParams(); lpWindow.flags =
		 * WindowManager.LayoutParams.FLAG_DIM_BEHIND; lpWindow.dimAmount =
		 * 0.8f; getWindow().setAttributes(lpWindow);
		 */
		setContentView(R.layout.dialog_record2);

		setLayout2();

	}

	protected RecordDialog2(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public RecordDialog2(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		// TODO Auto-generated constructor stub
	}

	public RecordDialog2(Context context, int theme) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		// TODO Auto-generated constructor stub
	}

	// layout

	Button confirm, cancel;
	ImageButton exitbtn2;
	public static MediaPlayer player;

	private void setLayout2() {

		confirm = (Button) findViewById(R.id.dialog_confirm);
		cancel = (Button) findViewById(R.id.dialog_cancel);
		/*
		 * exitbtn2 = (ImageButton) findViewById(R.id.dia2exitbtn);
		 * 
		 * exitbtn2.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Toast.makeText(getContext(), "X버튼 클릭했음",
		 * Toast.LENGTH_SHORT).show(); onBackPressed();
		 * 
		 * } });
		 */
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
				RD1 = new RecordDialog(getContext());
				RD1.show();

			}
		});
		confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				RecordBookEntity entity = new RecordBookEntity();
				new InsertRecord().execute("T.T");

				

			}
		});

	}

	class InsertRecord extends AsyncTask<String, String, ArrayList<BookItemEntityObject>> {
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected ArrayList<BookItemEntityObject> doInBackground(String... params) {

			File rec = new File(Record_Fragment.RECORDED_FILE);

			return RecordHandler.InserRecord(rec);
		}

		@Override
		protected void onPostExecute(ArrayList<BookItemEntityObject> result) {
			// TODO Auto-generated method stub

			// 전송이 성공하면
			if (result != null) {
				String r = result.get(0).result;
				if (r.equalsIgnoreCase("success")) {
					onBackPressed();
					Record_Fragment.BookContent.remove(Record_Fragment.bookindex);
					RD3 = new RecordDialog3(getContext());
					RD3.show();
					RecordDialog3.tb.setText("전송에 성공하였습니다.");
					
				}

				else {
					
		/*			RecordDialog2 rd2 = new RecordDialog2(getContext());
					rd2.show();
					RecordDialog3.tb.setText("전송에 실패하였습니다. 다시 시도해 주세요..");*/
					Toast.makeText(getContext(), "전송에 실패하였습니다. 다시 시도해 주세요..", Toast.LENGTH_SHORT).show();
				}
				super.onPostExecute(result);
			} else {
			/*	onBackPressed();
				RecordDialog2 rd2 = new RecordDialog2(getContext());
				rd2.show();
				RecordDialog3.tb.setText("네트워크가 원활하지 않습니다. 다시 시도해 주세요.");*/
				Toast.makeText(getContext(), "네트워크가 원활하지 않습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
			}
		}

	}
}