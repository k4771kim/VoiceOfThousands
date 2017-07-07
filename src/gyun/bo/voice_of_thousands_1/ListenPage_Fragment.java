package gyun.bo.voice_of_thousands_1;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
/*import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;*/

public class ListenPage_Fragment extends Fragment {
	boolean playflag, mflag = false;
	public MediaPlayer mMediaPlayer;
	ImageView spbtn, forward, backward, listenbookimg;
	TextView title;
	public static int c, contentnum, totalnum, bookid; // ccc -> 나중에 수정. content
														// num ,,, c=> current
														// play 위치
	/* public static int content_num = 1; */
	Button mp3download;
	public static int imgH, imgW;
	public TextView Reader, writer, publisher, listenlocation, title_writer, title_publisher, title_booktitle;
	public TextView currentlocation;
	public static String path, membername;
	public SeekBar listen_seek;
	public VOT_Async VA;
	public VOT_INFO_Async VAI;
	public Bundle extra;
	public static ImageView listen_member_img;
	private long latestId = -1;
	public static FrameLayout listen_circle;
	public static DisplayImageOptions options;
	ImageLoaderConfiguration ILC;
	public DownloadManager dm;
	public DownloadManager.Request request;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		playflag = true;

		View v = inflater.inflate(R.layout.listen_page, container, false);
		extra = getArguments();
		bookid = extra.getInt("bookid");
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "SEOULHANGANGB.TTF");

		/*
		 * options = new
		 * DisplayImageOptions.Builder().showImageOnLoading(R.drawable.
		 * book_loading)
		 * .showImageForEmptyUri(R.drawable.ic_headset_white_24dp).
		 * showImageOnFail(R.drawable.name)
		 * .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
		 * .displayer(new RoundedBitmapDisplayer(20)).build(); ILC = new
		 * ImageLoaderConfiguration.Builder(getActivity()).build();
		 * 
		 * ImageLoader.getInstance().init(ILC);
		 */ VAI = new VOT_INFO_Async();

		listen_book_item.f = true;
		VAI.execute(NetworkDefineConstant.SERVER_URL_VOT_LISTENBOOK_INFO, "GET");
		/*
		 * firstbtn = (Button) v.findViewById(R.id.firstbtn);
		 */
		mp3download = (Button) v.findViewById(R.id.mp3_download);
		mp3download.setTypeface(tf);

		listen_member_img = (ImageView) v.findViewById(R.id.listen_member_img);
		listen_circle = (FrameLayout) v.findViewById(R.id.listen_circle);
		title_writer = (TextView) v.findViewById(R.id.title_writer);
		title_publisher = (TextView) v.findViewById(R.id.title_publisher);
		title_booktitle = (TextView) v.findViewById(R.id.title_booktitle);
		title_writer.setTypeface(tf);
		title_publisher.setTypeface(tf);
		title_booktitle.setTypeface(tf);
		spbtn = (ImageView) v.findViewById(R.id.spbtn);
		backward = (ImageView) v.findViewById(R.id.backward);
		forward = (ImageView) v.findViewById(R.id.forward);

		if (mMediaPlayer == null) {
			mMediaPlayer = new MediaPlayer();
		}

		mp3download.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Toast.makeText(getActivity(), "mp3를 다운로드합니다.", Toast.LENGTH_SHORT).show();
				mp3download.setClickable(false);
				new Mp3Down_Asyn().execute();
			}
		});
		/*
		 * firstbtn.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * Toast.makeText(getActivity(), "처음부터 듣습니다.",
		 * Toast.LENGTH_SHORT).show(); // init // 구현,... c = 0;
		 * mMediaPlayer.pause();
		 * 
		 * if (VA != null) { VA = null; }
		 * 
		 * VA = new VOT_Async();
		 * VA.execute(NetworkDefineConstant.SERVER_URL_VOT_LISTEN_MODE, "GET",
		 * "init");// forward
		 * spbtn.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp)
		 * ; playflag = false;
		 * 
		 * } });
		 */
		forward.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				c = 0;
				mMediaPlayer.pause();

				if (VA != null) {
					VA = null;
				}

				VA = new VOT_Async();
				VA.execute(NetworkDefineConstant.SERVER_URL_VOT_LISTEN_MODE, "GET", "forward");// forward
				spbtn.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
				playflag = false;

			}
		});

		backward.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				c = 0;
				mMediaPlayer.pause();

				if (VA != null) {
					VA = null;
				}
				VA = new VOT_Async();
				VA.execute(NetworkDefineConstant.SERVER_URL_VOT_LISTEN_MODE, "GET", "backward"); // backward
				spbtn.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
				playflag = false;

			}
		});

		spbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				////////////// 다시 구현하자 /////////////////////////

				if (playflag) { // 재생하기 클릭
					VA = new VOT_Async();
					VA.execute(NetworkDefineConstant.SERVER_URL_VOT_LISTEN_MODE, "GET", "play"); // play
					spbtn.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
					playflag = false;

				} else { // 멈춤 클릭
					c = mMediaPlayer.getCurrentPosition();
					mMediaPlayer.pause();
					if (VA != null) {
						VA = null;
					}

					spbtn.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
					playflag = true;

				}

			}
		});

		listen_seek = (SeekBar) v.findViewById(R.id.listen_seekbar); // 시크바
																		// 변경되면..?

		listen_seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

				if (listen_seek.getProgress() + 1 <= totalnum) {
					currentlocation.setHint(listen_seek.getProgress() + 1 + ""); // 그리고
					contentnum = listen_seek.getProgress() + 1;
					VA = new VOT_Async();
					VA.execute(NetworkDefineConstant.SERVER_URL_VOT_LISTEN_MODE, "GET", "play"); // play
																									// //
																									// 재생한다.
					listen_seek.setSecondaryProgress(contentnum - 1); // 그리고
					spbtn.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp); // 세컨더리
					playflag = false; // 프로그래스바의
					// 위치를
					// 변경한다.

				} else if (listen_seek.getProgress() + 1 > totalnum) {
					currentlocation.setHint(listen_seek.getProgress() + ""); // 그리고

					mMediaPlayer.pause();
					mMediaPlayer.reset();

					// 끝낸다.

					spbtn = (ImageView) getView().findViewById(R.id.spbtn);
					spbtn.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
					Toast.makeText(getActivity(), "모든 문장의 재생을 완료하였습니다.", Toast.LENGTH_SHORT).show();

					contentnum = listen_seek.getProgress() - 1;
					listen_seek.setSecondaryProgress(contentnum);
				}

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub

			}
		});

		return v;

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
		if (VA != null) {
			VA.cancel(true);
			VA = null;

		}

	}

	class VOT_INFO_Async extends AsyncTask<String, Void, ArrayList<BookItemEntityObject>> {

		StringBuilder queryStringParams = new StringBuilder();

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			queryStringParams.append("?memberid=" + Voice_Of_Thousands.member_id).append("&bookid=" + bookid);

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

			if (result != null) {
				if (result.get(0).result.equalsIgnoreCase("success")) {
					writer = (TextView) getView().findViewById(R.id.listenbookwriter);
					publisher = (TextView) getView().findViewById(R.id.listenbookpublisher);
					title = (TextView) getView().findViewById(R.id.listenbooktitle);
					listenlocation = (TextView) getView().findViewById(R.id.listen_location);
					listenbookimg = (ImageView) getView().findViewById(R.id.listen_book_img); // 북
																								// //
																								// 이미지
					currentlocation = (TextView) getView().findViewById(R.id.current_location);
					listenlocation = (TextView) getView().findViewById(R.id.listen_location);
					contentnum = result.get(0).book_mark;
					totalnum = result.get(0).total_cnum;
					writer.setText(result.get(0).book_writer.toString());
					title.setText(result.get(0).book_title.toString());
					publisher.setText(result.get(0).book_publisher.toString());
					// listenbookimg.setImageBitmap(result.get(0).bookimg);
					ImageLoader.getInstance().displayImage(result.get(0).book_img_url, listenbookimg, options);
					currentlocation.setHint(contentnum + "");
					listenlocation.setText(" / " + result.get(0).total_cnum);
					ListenPage_Fragment.totalnum = result.get(0).total_cnum;
					listen_seek = (SeekBar) getView().findViewById(R.id.listen_seekbar);
					listen_seek.setMax(totalnum);
					listen_seek.setSecondaryProgress(contentnum - 1);
					listen_seek.setProgress(contentnum - 1);
				}

				else { // fail

					if (getActivity() != null) {
						Toast.makeText(getActivity(), "받아올 책 정보가 없습니다.", Toast.LENGTH_SHORT).show();

						getActivity().onBackPressed();
					}

					VOT_Listen_Activity.location = 0;
				}
			} else {
				if (getActivity() != null) {
					Toast.makeText(getActivity(), "네트워크가 원활하지 않습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();

					getActivity().onBackPressed();
				}
				VOT_Listen_Activity.location = 0;
			}

		}

	}

	class VOT_Async extends AsyncTask<String, Void, ArrayList<BookItemEntityObject>> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			imgH = listen_circle.getHeight();
			imgW = listen_circle.getWidth();
		}

		StringBuilder queryStringParams = new StringBuilder();

		@Override
		protected ArrayList<BookItemEntityObject> doInBackground(String... params) {

			queryStringParams.append("?mode=" + params[2]).append("&bookid=" + bookid)

			.append("&contentnum=" + ListenPage_Fragment.contentnum).append("&memberid=" + Voice_Of_Thousands.member_id)

			; // 추후변수값으로 변경할것.
			return VOTHelperHandler.BookList(params[0], params[1], queryStringParams);

		}

		@Override
		protected void onPostExecute(ArrayList<BookItemEntityObject> result) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.get(0).result.equalsIgnoreCase("success")) {

					path = result.get(0).listenbook_path;
					membername = result.get(0).member_name;

					Reader = (TextView) getView().findViewById(R.id.Reader); // ㅡㅡㅡ의
																				// 목소리입니다.
					TextView current_text = (TextView) getView().findViewById(R.id.listen_current_text);
					ListenPage_Fragment.contentnum = result.get(0).content_num;
					current_text.setText(result.get(0).content_num + "번째 문장");
					Reader.setText(membername + "님의 목소리입니다. ");

					// String reader_image_URL = result.get(0).member_image;
					// ImageLoader.getInstance().displayImage(reader_image_URL,
					// listen_member_img, options);
					listen_member_img.setImageBitmap(result.get(0).memberimg);

					listenlocation.setText(" / " + ListenPage_Fragment.totalnum);
					currentlocation.setHint(ListenPage_Fragment.contentnum + "");
					listen_seek = (SeekBar) getView().findViewById(R.id.listen_seekbar);
					listen_seek.setSecondaryProgress(ListenPage_Fragment.contentnum - 1);
					listen_seek.setProgress(ListenPage_Fragment.contentnum - 1);

					mMediaPlayer.reset();

					try {

						mMediaPlayer.setDataSource(path);
						mMediaPlayer.prepare();
						mMediaPlayer.seekTo(c); // seekTo가 실행되면 자동으로 prepare();가
												// 먼저
												// 실행이
												// 됨

						// But.. setOnSeekto -> setOnPrepardListener XXXX
						mMediaPlayer.start();
					} catch (Exception e) {
						Log.e("ListenPage_Fragment.onPostExecute", e.toString());
					}

					MediaPlayer.OnCompletionListener mCompletion = new OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							// TODO Auto-generated method stub

							if (contentnum != totalnum) {
								if (VA != null) {
									VA.cancel(true);
									VA = null;
								}
								c = 0;
								VA = new VOT_Async();
								VA.execute(NetworkDefineConstant.SERVER_URL_VOT_LISTEN_MODE, "GET", "forward");// forward
								listen_seek.setProgress(listen_seek.getProgress() + 1);
								listen_seek.setSecondaryProgress(listen_seek.getProgress() + 1);
							} else {
								mMediaPlayer.stop();
								spbtn = (ImageView) getView().findViewById(R.id.spbtn);
								spbtn.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
								playflag = true;
							}
						}
					};
					mMediaPlayer.setOnCompletionListener(mCompletion);

				} else { // fail
					Toast.makeText(getActivity(), "받아올 음성이 없습니다", Toast.LENGTH_SHORT).show();
					listen_seek.setProgress(totalnum);
					mMediaPlayer.start();

				}
			} else {
				Toast.makeText(getActivity(), "네트워크 연결이 원활하지 않습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
			}
		}

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		/*
		 * if (mMediaPlayer != null) { mMediaPlayer.pause(); c =0;
		 * spbtn.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
		 * playflag = true; }
		 */
		mMediaPlayer.pause();
		if (VA != null) {
			VA = null;
		}

		spbtn.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
		playflag = true;

	}

	class Mp3Down_Asyn extends AsyncTask<String, Void, ArrayList<BookItemEntityObject>> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected ArrayList<BookItemEntityObject> doInBackground(String... params) {
			// TODO Auto-generated method stub
			StringBuilder sb = new StringBuilder();
			sb.append("?bookid=" + bookid);
			return VOTHelperHandler.BookList(NetworkDefineConstant.SERVER_URL_VOT_MP3_Download, "GET", sb);
		}

		@Override
		protected void onPostExecute(ArrayList<BookItemEntityObject> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				if (result.get(0).result.equalsIgnoreCase("success")) {
					String url = result.get(0).listenbook_path;

					Uri urlToDownload = Uri.parse(url);
					List<String> pathSegments = urlToDownload.getPathSegments();
					request = new DownloadManager.Request(urlToDownload);
					request.setTitle(title.getText().toString());
					request.setDescription("천개의목소리 음성북");
					request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
							pathSegments.get(pathSegments.size() - 1));
					dm = (DownloadManager) getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
					Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
					latestId = dm.enqueue(request);
					// startActivity(new
					// Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));

					/*
					 * IntentFilter completeFiler = new
					 * IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
					 * 
					 * BroadcastReceiver completeReceiver = new
					 * BroadcastReceiver(){
					 * 
					 * @Override public void onReceive(Context context, Intent
					 * intent) { Toast.makeText(context, "다운로드가 완료되었습니다."
					 * ,Toast.LENGTH_SHORT).show(); }
					 * 
					 * };
					 * 
					 * getActivity().registerReceiver(completeReceiver,
					 * completeFiler); startActivity(new
					 * Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
					 */

				} else {
					Toast.makeText(getActivity(), "MP3 다운로드에 실패했습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
				}
				mp3download.setClickable(true);
			}
		}

	}

}
