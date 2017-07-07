package gyun.bo.voice_of_thousands_1;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import gyun.bo.voice_of_thousands_1.popup.RecordDialog;
import gyun.bo.voice_of_thousands_1.popup.VOT_Dialog;

public class Record_Fragment extends Fragment {
	ImageButton record_btn;
	public static String Book_title;
	ImageView example_btn;
	public static int bookindex, currentindex, currentpart, bookid, c;
	AlertDialog.Builder postrecordbuild, posttransmitbuild, sharebuild;
	public MediaPlayer player;
	Boolean sampleflag = false;
	Boolean recflag = false;
	public static Boolean setflag;
	public static String samplepath;
	Typeface tf;
	MediaRecorder recorder;
	public static View v;
	public static int readbook_count;
	public static ArrayList<ContentData> BookContent;
	/*
	 * public static HashSet bookset; public static HashMap bookmap;
	 *//*
		 * public static boolean completeflag;
		 */
	public static Bundle extra;

	final public static String RECORDED_FILE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/VOT/"
			+ "VOT_Record.wav";
	public RecordDialog RD;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
	
		tf = Typeface.createFromAsset(getActivity().getAssets(), "SEOULHANGANGB.TTF");
		samplepath = null;
		v = inflater.inflate(R.layout.record_page, container, false);
		/* read_book_list_fragment.listview.setClickable(false); */
		Voice_Of_Thousands.sm.setSlidingEnabled(false);
		recflag = false;
		setflag = false;
		extra = getArguments();

		if (BookContent == null) {
			BookContent = new ArrayList<ContentData>(10); // 책의 총 갯수..
		}
		for (bookindex = 0; bookindex < BookContent.size(); bookindex++) {
			if (BookContent.get(bookindex).bookid == extra.getInt("bookid")) {
				setflag = true;
				break;
			}
		}
		if (setflag) { // 받아온 bookdata가 있는경우

			TextView record_content = (TextView) v.findViewById(R.id.record_content);
			TextView record_title = (TextView) v.findViewById(R.id.Recode_title);
			record_title.setTypeface(tf);
			TextView record_part = (TextView) v.findViewById(R.id.Recode_part);
			record_title.setText(BookContent.get(bookindex).title.toString());
			record_part.setText(BookContent.get(bookindex).current + " / " + BookContent.get(bookindex).total);
			bookid = BookContent.get(bookindex).bookid;
			readbook_count = BookContent.get(bookindex).count;
			currentpart = BookContent.get(bookindex).current;
			record_content.setText(BookContent.get(bookindex).content.toString());
			/*
			 * Toast.makeText(getActivity(), "이미 가져온 데이터가 있습니다.",
			 * Toast.LENGTH_SHORT).show();
			 */

		} else { // 없는경우
			new VOT_Async().execute(NetworkDefineConstant.SERVER_URL_VOT_BOOKING, "GET",
					null);/*
							 * Toast.makeText(getActivity(),
							 * "이미 가져온 데이터가 없습니다. 서버에 접속합니다..",
							 * Toast.LENGTH_SHORT).show();
							 */
		}

		/*
		 * 
		 * bookset = new HashSet<Integer>(10);
		 * 
		 * Iterator<Integer> iterator = bookset.iterator(); while
		 * (bookset.iterator().hasNext()) { int it = iterator.next(); if (it ==
		 * extra.getInt("bookid")) { setflag = true; } } }
		 */

		example_btn = (ImageView) v.findViewById(R.id.listen_example);
		example_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!sampleflag) { // sample버튼 play버튼일때.

					if (player == null) {
						player = new MediaPlayer();
					}
					if (samplepath == null) {
						new VOT_Example().execute(NetworkDefineConstant.SERVER_URL_VOT_SAMPLE, "GET");

					} else {
						try {
							example_btn.setImageResource(R.drawable.ic_headset_white_48dp);
							player.seekTo(c);
							player.start();
							sampleflag = true;

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					MediaPlayer.OnCompletionListener mCompletion = new OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							// TODO Auto-generated method stub
							c = 0;
							player.pause();
							example_btn.setImageResource(R.drawable.ic_headset_black_48dp);
							sampleflag = false;
						}

					};
					player.setOnCompletionListener(mCompletion);
				} else {// sample버튼 stop버튼일때.
					example_btn.setImageResource(R.drawable.ic_headset_black_48dp);
					c = player.getCurrentPosition();
					player.pause();

					sampleflag = false;
				}

			}
		});
		((Voice_Of_Thousands) getActivity()).listen_mode_icon.setVisible(false);
		postrecordbuild = new AlertDialog.Builder(container.getContext());
		posttransmitbuild = new AlertDialog.Builder(container.getContext());
		sharebuild = new AlertDialog.Builder(container.getContext());

		record_btn = (ImageButton) v.findViewById(R.id.recode_btn);

		record_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!recflag) {
					if (recorder != null) {
						recorder.stop();
						recorder.release();
						recorder = null;
					}
					if (player != null) {
						player.release();
						player = null;
						// Log.e("player", "종료됨");
					}
					record_btn.setBackgroundResource(R.drawable.ic_mic_none_black_48dp);
					recorder = new MediaRecorder();
					recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
					recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
					//recorder.setAudioEncodingBitRate(196600);
					recorder.setAudioEncodingBitRate(65500);
					
					recorder.setOutputFile(RECORDED_FILE);

					try {
						// Toast.makeText(getActivity(), "녹음을 시작합니다.",
						// Toast.LENGTH_LONG).show();
						example_btn.setClickable(false);
						recflag = true;

						recorder.prepare();
						recorder.start();

					} catch (Exception ex) {
						Log.e("Recode_Fragment.recode_btn.setOnClickListener", ex.toString());
					}
				} else {

					if (recorder == null)
						return;

					recorder.stop();
					recorder.release();
					recorder = null;

					// Toast.makeText(getActivity(), "녹음이 중지되었습니다.",
					// Toast.LENGTH_LONG).show();
					record_btn.setBackgroundResource(R.drawable.ic_mic_black_48dp);
					recflag = false;
					/* AlertDialog dialog = postrecordbuild.create(); */

					/* dialog.show(); */

					RD = new RecordDialog(getActivity());
					RD.show();
				}

			}
		});

		return v;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub

		if (!((Voice_Of_Thousands) getActivity()).listen_mode_icon.isVisible()) {

			((Voice_Of_Thousands) getActivity()).listen_mode_icon.setVisible(true);
		}

		super.onDestroyView();
	}

	class VOT_Example extends AsyncTask<String, Void, ArrayList<BookItemEntityObject>> {
		StringBuilder queryStringParams = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			queryStringParams = new StringBuilder();
			queryStringParams.append("?bookid=" + bookid) // 추후변수값으로
															// 변경할것.
					.append("&contentnum=" + currentpart);

			super.onPreExecute();
		}

		@Override
		protected ArrayList<BookItemEntityObject> doInBackground(String... params) {
			// TODO Auto-generated method stub
			return VOTHelperHandler.BookList(params[0], params[1], queryStringParams);
		}

		@Override
		protected void onPostExecute(ArrayList<BookItemEntityObject> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result.get(0).result.equalsIgnoreCase("success")) {
				samplepath = result.get(0).listenbook_path;
				try {
					example_btn.setImageResource(R.drawable.ic_headset_white_24dp);
					player.setDataSource(samplepath);
					player.prepare();
					player.seekTo(c);
					player.start();
					sampleflag = true;

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (result.get(0).result.equalsIgnoreCase("fail")) {
				Toast.makeText(getActivity(), "앞서 녹음된 샘플이 없습니다. 가장 먼저 이 책을 읽어주세요.", Toast.LENGTH_SHORT).show();
			} else {

			}

		}

	}

	class VOT_Async extends AsyncTask<String, Void, ArrayList<BookItemEntityObject>> {
		ProgressDialog dialog;
		StringBuilder queryStringParamsBOOK = new StringBuilder();
		StringBuilder queryStringParams = null;
		String server;
		String r;

		protected void onPreExecute() {
			try {
				dialog = ProgressDialog.show(getActivity(), "", "서버에서 책 정보를 불러오고 있습니다. 잠시만 기다려 주세요.", true);

			} catch (Exception e) {
				e.printStackTrace();
			}

			queryStringParamsBOOK.append("?memberid=" + Voice_Of_Thousands.member_id)
					.append("&bookid=" + extra.getInt("bookid"));
			super.onPreExecute();

		}

		@Override
		protected ArrayList<BookItemEntityObject> doInBackground(String... params) {

			if (params[0] == NetworkDefineConstant.SERVER_URL_VOT_BOOKING) {
				queryStringParams = queryStringParamsBOOK;
			} /*
				 * else if (params[0] ==
				 * NetworkDefineConstant.SERVER_URL_VOT_CONNECTIONEND)
				 * queryStringParams = queryStringParamsCONN;
				 */
			server = params[0];
			return VOTHelperHandler.BookList(params[0], params[1], queryStringParams);

		}

		@Override
		protected void onPostExecute(ArrayList<BookItemEntityObject> result) {
			// TODO Auto-generated method stub
			dialog.dismiss();

			if (result == null) {

				Toast.makeText(getActivity(), "서버와의 연결에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();

				Fragment mRecodeFragment = ((FragmentActivity) Voice_Of_Thousands.getContext())
						.getSupportFragmentManager().findFragmentByTag("RecodeFragment");
				FragmentTransaction ft = ((FragmentActivity) Voice_Of_Thousands.getContext())
						.getSupportFragmentManager().beginTransaction();
				ft.remove(mRecodeFragment);
				ft.commit();
				Voice_Of_Thousands.location = 0;
				Voice_Of_Thousands.naviflag = 0;
				Voice_Of_Thousands.actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

			} else if (result != null && result.size() > 0) {
				r = result.get(0).result;
				if (r.equalsIgnoreCase("success")) {
					TextView record_content = (TextView) getView().findViewById(R.id.record_content);

					TextView record_title = (TextView) getView().findViewById(R.id.Recode_title);
					record_title.setTypeface(tf);
					TextView record_part = (TextView) getView().findViewById(R.id.Recode_part);
					record_title.setText(result.get(0).book_title.toString());
					Book_title = result.get(0).book_title.toString();
					record_part.setText(result.get(0).content_num + " / " + result.get(0).total_cnum);
					record_content.setText(result.get(0).content_text.toString());
					ContentData data = new ContentData();
					/* data.bookid = extra.getInt("bookid"); */
					data.bookid = result.get(0).book_id;
					data.content = result.get(0).content_text.toString();
					data.title = result.get(0).book_title.toString();
					data.current = result.get(0).content_num;
					data.count = result.get(0).book_count;
					currentpart = result.get(0).content_num;
					bookid = result.get(0).book_id;
					readbook_count = result.get(0).book_count;
					data.total = result.get(0).total_cnum;
					extra.putInt("contentnum", result.get(0).content_num);
					BookContent.add(data);
					SharedPreferences prefs = getActivity()
							.getSharedPreferences(VOT_GCM_DefineConstant.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);

					if (!prefs.getBoolean("record_dialog", false)) {
						VOT_Dialog vd = new VOT_Dialog(getActivity());
						vd.show();
					}
					;
					bookindex = BookContent.size() - 1;
				} else if (r.equalsIgnoreCase("fail")) {
					Toast.makeText(getActivity(), result.get(0).reject_reason.toString(), Toast.LENGTH_SHORT).show();
					Voice_Of_Thousands.location = 0;
					Voice_Of_Thousands.naviflag = 0;
					Voice_Of_Thousands.actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
					Fragment mRecodeFragment = ((FragmentActivity) Voice_Of_Thousands.getContext())
							.getSupportFragmentManager().findFragmentByTag("RecodeFragment");
					FragmentTransaction ft = ((FragmentActivity) Voice_Of_Thousands.getContext())
							.getSupportFragmentManager().beginTransaction();
					ft.remove(mRecodeFragment);
					ft.commit();

				}
				/* 
				 * */

			}
			/*
			 * else if (server ==
			 * NetworkDefineConstant.SERVER_URL_VOT_CONNECTIONEND) { if (result
			 * == null) { Toast.makeText(getActivity(), "서버로 전송을 실패했다요...",
			 * Toast.LENGTH_SHORT).show(); } else if (result != null &&
			 * result.size() > 0) { if (result.get(0).content_text.toString() ==
			 * "sucess") { Log.e( "서버로 Connection End", "성공이다요."); } else {
			 * 
			 * }
			 * 
			 * } }
			 */

		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		if (player != null) {
			player.release();
			player = null;
			// Log.e("player", "종료됨");
		}
		if (recorder != null) {
			recorder.release();
			recorder = null;
			// Log.e("recorder", "종료됨");
		}
		Voice_Of_Thousands.sm.setSlidingEnabled(true);
		super.onDestroy();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		example_btn.setClickable(true);
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		/*
		 * if (player != null) { player.release(); player = null;
		 * Log.e("player", "종료됨"); } if (RecordDialog2.player != null) {
		 * RecordDialog2.player.release(); RecordDialog2.player = null;
		 * Log.e("RecordDialog2.player", "종료됨");
		 * 
		 * }
		 */

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (player != null) {
			player.release();
			player = null;

			// Log.e("player", "종료됨");
		}

		if (RecordDialog.player != null) {
			RecordDialog.player.stop();
			RecordDialog.player.release();
			RecordDialog.player = null;
		}
		example_btn.setImageResource(R.drawable.ic_headset_black_48dp);
		samplepath = null;
		sampleflag = false;

	}
}