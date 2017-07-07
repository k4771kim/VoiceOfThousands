package gyun.bo.voice_of_thousands_1.popup;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import gyun.bo.voice_of_thousands_1.R;
import gyun.bo.voice_of_thousands_1.Record_Fragment;

public class RecordDialog extends Dialog {

	public RecordDialog2 RD2;
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
		setContentView(R.layout.dialog_record1);

		setLayout();

	}

	protected RecordDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public RecordDialog(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		// TODO Auto-generated constructor stub
	}

	public RecordDialog(Context context, int theme) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		// TODO Auto-generated constructor stub
	}

	// layout

	Button relisten, transmit, rerecord;
	ImageButton exitbtn;
	public static MediaPlayer player;

	private void setLayout() {
		rerecord = (Button) findViewById(R.id.rerecord);
		relisten = (Button) findViewById(R.id.relisten);
		transmit = (Button) findViewById(R.id.transmit);
		exitbtn = (ImageButton) findViewById(R.id.dia1exitbtn);

		exitbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(getContext(), "X버튼 클릭했음", Toast.LENGTH_SHORT).show();
				onBackPressed();

			}
		});
		transmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//	Toast.makeText(getContext(), "전송하기클릭했음", Toast.LENGTH_SHORT).show();

			onBackPressed();
			RD2 = new RecordDialog2(getContext());
			RD2.show();
			}
		});
		relisten.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		//		Toast.makeText(getContext(), "들어보기클릭했음", Toast.LENGTH_SHORT).show();

				if (player != null) {
					player.stop();
					player.release();
					player = null;
				}

			//	Toast.makeText(getContext(), "녹음된 파일을 재생합니다.", Toast.LENGTH_LONG).show();
				try {
					player = new MediaPlayer();  

					player.setDataSource(Record_Fragment.RECORDED_FILE);
					player.prepare();
					player.start();
				} catch (Exception e) {
					Log.e("PostRecordBuilder.ListenButton", "Audio play failed.", e);
				}

			}
		});

		rerecord.setOnClickListener(new View.OnClickListener() {

			
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			//	Toast.makeText(getContext(), "재녹음클릭했음", Toast.LENGTH_SHORT).show();
				onBackPressed();
			}
		});

	}

	@Override
	public void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		if (player != null) {
			player.stop();
			player.release();
			player = null;
		}
		super.onDetachedFromWindow();
	}

}
