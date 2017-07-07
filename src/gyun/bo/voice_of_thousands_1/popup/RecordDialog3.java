package gyun.bo.voice_of_thousands_1.popup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.Voice;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import gyun.bo.voice_of_thousands_1.R;
import gyun.bo.voice_of_thousands_1.Record_Fragment;
import gyun.bo.voice_of_thousands_1.VOTApplication;
import gyun.bo.voice_of_thousands_1.Voice_Of_Thousands;

public class RecordDialog3 extends Dialog {
	public static TextView tb, count;
	public Context context1;

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
		setContentView(R.layout.dialog_record3);

		tb = (TextView) findViewById(R.id.textbd);

		count = (TextView) findViewById(R.id.dia3_count);
		count.setText("오늘 이 책은 " + (--Record_Fragment.readbook_count) + "회 더 녹음 가능합니다.");

		setLayout3();

	}

	protected RecordDialog3(Context context, boolean cancelable, OnCancelListener cancelListener) {

		super(context, cancelable, cancelListener);
		context1 = context;
		// TODO Auto-generated constructor stub
	}

	public RecordDialog3(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		context1 = context;
		// TODO Auto-generated constructor stub
	}

	public RecordDialog3(Context context, int theme) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		context1 = context;
		// TODO Auto-generated constructor stub
	}

	// layout

	Button share, confirm;
	ImageButton exitbtn3;

	private void setLayout3() {

		confirm = (Button) findViewById(R.id.dia3_confirm);
		share = (Button) findViewById(R.id.dia3_share);
		/*
		 * exitbtn3 = (ImageButton) findViewById(R.id.dia3_exit);
		 * 
		 * exitbtn3.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Toast.makeText(getContext(), "X버튼 클릭했음",
		 * Toast.LENGTH_SHORT).show(); onBackPressed();
		 * 
		 * } });
		 */
		share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				  Intent shareIntent = new	  Intent(android.content.Intent.ACTION_SEND);
				  
				  shareIntent.setType("text/plain");
				  shareIntent.putExtra(Intent.EXTRA_TEXT, "음성북 제작 프로젝트 천개의목소리의 \""+ Record_Fragment.Book_title + "\" 프로젝트에 참여하였습니다"); 
				  getContext().startActivity(Intent.createChooser(shareIntent, "천개의목소리"));

			}
		});
		confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
				Fragment mRecodeFragment = ((FragmentActivity) Voice_Of_Thousands.getContext())
						.getSupportFragmentManager().findFragmentByTag("RecodeFragment");
				FragmentTransaction ft = ((FragmentActivity) Voice_Of_Thousands.getContext())
						.getSupportFragmentManager().beginTransaction();
				ft.remove(mRecodeFragment);
				ft.commit();
				
		//		Log.e("Location",Voice_Of_Thousands.location +"");
				
				Voice_Of_Thousands.location = 0;
				Voice_Of_Thousands.naviflag = 0;
				Voice_Of_Thousands.actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

				/* ((Voice_Of_Thousands)f).FragmentChange(2, bundle); */

			}
		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		super.onBackPressed();
		Fragment mRecodeFragment = ((FragmentActivity) Voice_Of_Thousands.getContext()).getSupportFragmentManager()
				.findFragmentByTag("RecodeFragment");
		FragmentTransaction ft = ((FragmentActivity) Voice_Of_Thousands.getContext()).getSupportFragmentManager()
				.beginTransaction();
		ft.remove(mRecodeFragment);
		ft.commit();

		Voice_Of_Thousands.location = 0;
		Voice_Of_Thousands.naviflag = 0;
		Voice_Of_Thousands.actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
	}
}
