package gyun.bo.voice_of_thousands_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Mypage_List extends Fragment {
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		super.onActivityResult(requestCode, resultCode, data);

		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	ImageButton FirstI, SecondI, ThirdI, ForthI, FifthI;

	ImageButton FirstSI, SecondSI, ThirdSI, ForthSI, FifthSI;
	CallbackManager callbackManager;
	TextView FirstB, SecondB, ThirdB, ForthB, FifthB;
	public static int btnnum;
	public static int current_page = 0; // pageselect에서 얻어온 값.
	public static MediaPlayer mMediaPlayer;
	public Bundle b;
	public int Hnumber;
	public static boolean mflag = false;
	public int current_position; // adapter에서 받아온 값.
	public static RecordListItemData[][] d = new RecordListItemData[mypage_adapter.total_page][5];

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.mypage_list, container, false);
		clickbutton clk = new clickbutton();
		current_page = MyPage_Fragment.current_page;
		FirstB = (TextView) v.findViewById(R.id.mp_first_body);

		SecondB = (TextView) v.findViewById(R.id.mp_second_body);

		ThirdB = (TextView) v.findViewById(R.id.mp_third_body);

		ForthB = (TextView) v.findViewById(R.id.mp_forth_body);

		FifthB = (TextView) v.findViewById(R.id.mp_fifth_body);

		FirstSI = (ImageButton) v.findViewById(R.id.mp_first_share_icon);
		SecondSI = (ImageButton) v.findViewById(R.id.mp_second_share_icon);
		ThirdSI = (ImageButton) v.findViewById(R.id.mp_third_share_icon);
		ForthSI = (ImageButton) v.findViewById(R.id.mp_forth_share_icon);
		FifthSI = (ImageButton) v.findViewById(R.id.mp_fifth_share_icon);
		FirstI = (ImageButton) v.findViewById(R.id.mp_first_icon);
		SecondI = (ImageButton) v.findViewById(R.id.mp_second_icon);
		ThirdI = (ImageButton) v.findViewById(R.id.mp_third_icon);
		ForthI = (ImageButton) v.findViewById(R.id.mp_forth_icon);
		FifthI = (ImageButton) v.findViewById(R.id.mp_fifth_icon);

		FirstI.setOnClickListener(clk);
		SecondI.setOnClickListener(clk);
		ThirdI.setOnClickListener(clk);
		ForthI.setOnClickListener(clk);
		FifthI.setOnClickListener(clk);
		FirstSI.setOnClickListener(clk);
		SecondSI.setOnClickListener(clk);
		ThirdSI.setOnClickListener(clk);
		ForthSI.setOnClickListener(clk);
		FifthSI.setOnClickListener(clk);
		Bundle b = new Bundle();
		b = getArguments();
		current_position = b.getInt("position");
		new VOT_Async().execute(NetworkDefineConstant.SERVER_URL_VOT_MEMBER_INFO, "GET", current_position + "");

		return v;
	}

	class clickbutton implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Log.e("current_position - Adapter로 넘어온 값", current_position + "");
			//Log.e("current_page - ChangeListener로 넘어온 값", current_page + "");
			if (v == FirstI) {
				btnnum = 0;

				if (d[current_page][0].contents_status == 3) {

					if (!mflag) {
						if (mMediaPlayer == null) {
							mMediaPlayer = new MediaPlayer();
						}
						Hnumber = 1;
						new History_Async().execute(NetworkDefineConstant.SERVER_URL_VOT_LISTEN_MODE, "GET", "history",
								d[current_page][0].book_id + "", d[current_page][0].content_num + "");
						FirstI.setImageResource(R.drawable.ic_pause_black_48dp);
						FirstI.setClickable(false);
						SecondI.setClickable(false);

						MyPage_Fragment.mypage_vp.setFocusable(false);
						MyPage_Fragment.mypage_vp.setActivated(false);
						ThirdI.setClickable(false);
						FifthI.setClickable(false);
						ForthI.setClickable(false);
						mflag = true;
						/*
						 * v.setBackgroundResource(R.drawable.
						 * ic_pause_black_48dp);
						 */
					} else {
						mMediaPlayer.pause();
						mMediaPlayer.reset();
						mflag = false;
						FirstI.setImageResource(R.drawable.ic_play_arrow_black_48dp);
						FirstI.setClickable(true);
						SecondI.setClickable(true);
						MyPage_Fragment.mypage_vp.setClickable(true);
						MyPage_Fragment.mypage_vp.setFocusable(true);
						ThirdI.setClickable(true);
						FifthI.setClickable(true);
						ForthI.setClickable(true);
					}
					OCL OCL = new OCL();
					mMediaPlayer.setOnCompletionListener(OCL);

				} else if (d[current_page][0].contents_status == 4) {
					// 딜리트 전송.
					new Delete_Async().execute(NetworkDefineConstant.SERVER_URL_VOT_HISTORY_DELETE, "POST", "0");
					FirstI.setClickable(false);
					SecondI.setClickable(false);
					ThirdI.setClickable(false);
					ForthI.setClickable(false);
					FifthI.setClickable(false);

				}

			} else if (v == SecondI) {
				btnnum = 1;
				if (d[current_page][1].contents_status == 3) {

					if (!mflag) {
						if (mMediaPlayer == null) {
							mMediaPlayer = new MediaPlayer();
						}Hnumber = 2;
						new History_Async().execute(NetworkDefineConstant.SERVER_URL_VOT_LISTEN_MODE, "GET", "history",
								d[current_page][1].book_id + "", d[current_page][1].content_num + "");
						SecondI.setImageResource(R.drawable.ic_pause_black_48dp);
						FirstI.setClickable(false);
						SecondI.setClickable(false);
						ThirdI.setClickable(false);
						FifthI.setClickable(false);
						ForthI.setClickable(false);
						FifthI.setClickable(false);
						mflag = true;
						/*
						 * v.setBackgroundResource(R.drawable.
						 * ic_pause_black_48dp);
						 */
					} else {
						mMediaPlayer.pause();
						mMediaPlayer.reset();
						mflag = false;

						SecondI.setImageResource(R.drawable.ic_play_arrow_black_48dp);
						FirstI.setClickable(true);
						SecondI.setClickable(true);

						ThirdI.setClickable(true);
						FifthI.setClickable(true);
						ForthI.setClickable(true);
					}
					OCL OCL = new OCL();
					mMediaPlayer.setOnCompletionListener(OCL);

				} else if (d[current_page][1].contents_status == 4) {
					// 딜리트 전송.
					new Delete_Async().execute(NetworkDefineConstant.SERVER_URL_VOT_HISTORY_DELETE, "POST", "1");
					FirstI.setClickable(false);
					SecondI.setClickable(false);
					ThirdI.setClickable(false);
					ForthI.setClickable(false);
					FifthI.setClickable(false);
				}

			} else if (v == ThirdI) {
				btnnum = 2;
				if (d[current_page][2].contents_status == 3) {

					if (!mflag) {
						if (mMediaPlayer == null) {
							mMediaPlayer = new MediaPlayer();
						}
						Hnumber = 3;
						new History_Async().execute(NetworkDefineConstant.SERVER_URL_VOT_LISTEN_MODE, "GET", "history",
								d[current_page][2].book_id + "", d[current_page][2].content_num + "");
						ThirdI.setImageResource(R.drawable.ic_pause_black_48dp);
						FirstI.setClickable(false);

						SecondI.setClickable(false);
						ThirdI.setClickable(false);
						FifthI.setClickable(false);
						ForthI.setClickable(false);
						mflag = true;
						/*
						 * v.setBackgroundResource(R.drawable.
						 * ic_pause_black_48dp);
						 */
					} else {
						mMediaPlayer.pause();
						mMediaPlayer.reset();
						mflag = false;

						ThirdI.setImageResource(R.drawable.ic_play_arrow_black_48dp);
						FirstI.setClickable(true);
						SecondI.setClickable(true);

						ThirdI.setClickable(true);
						FifthI.setClickable(true);
						ForthI.setClickable(true);
					}
					OCL OCL = new OCL();
					mMediaPlayer.setOnCompletionListener(OCL);

				} else if (d[current_page][2].contents_status == 4) {
					// 딜리트 전송.
					new Delete_Async().execute(NetworkDefineConstant.SERVER_URL_VOT_HISTORY_DELETE, "POST", "2");
					FirstI.setClickable(false);
				
					SecondI.setClickable(false);
					ThirdI.setClickable(false);
					ForthI.setClickable(false);
					FifthI.setClickable(false);

				}

			} else if (v == ForthI) {
				btnnum = 3;
				if (d[current_page][3].contents_status == 3) {

					if (!mflag) {
						if (mMediaPlayer == null) {
							mMediaPlayer = new MediaPlayer();
						}
						Hnumber = 4;
						new History_Async().execute(NetworkDefineConstant.SERVER_URL_VOT_LISTEN_MODE, "GET", "history",
								d[current_page][3].book_id + "", d[current_page][3].content_num + "");
						ForthI.setImageResource(R.drawable.ic_pause_black_48dp);
						FirstI.setClickable(false);
						ForthI.setClickable(false);
						ThirdI.setClickable(false);
						SecondI.setClickable(false);
						FifthI.setClickable(false);
						mflag = true;
						/*
						 * v.setBackgroundResource(R.drawable.
						 * ic_pause_black_48dp);
						 */
					} else {
						mMediaPlayer.pause();
						mMediaPlayer.reset();
						mflag = false;

						ForthI.setImageResource(R.drawable.ic_play_arrow_black_48dp);
						FirstI.setClickable(true);
						SecondI.setClickable(true);

						ThirdI.setClickable(true);
						FifthI.setClickable(true);
						ForthI.setClickable(true);
					}
					OCL OCL = new OCL();
					mMediaPlayer.setOnCompletionListener(OCL);

				} else if (d[current_page][3].contents_status == 4) {
					// 딜리트 전송.
					new Delete_Async().execute(NetworkDefineConstant.SERVER_URL_VOT_HISTORY_DELETE, "POST", "3");
					FirstI.setClickable(false);
					SecondI.setClickable(false);
					ThirdI.setClickable(false);
					ForthI.setClickable(false);
					FifthI.setClickable(false);
				}

			} else if (v == FifthI) {
				btnnum = 4;
				if (d[current_page][4].contents_status == 3) {

					if (!mflag) {
						if (mMediaPlayer == null) {
							mMediaPlayer = new MediaPlayer();
						}
						Hnumber = 5;
						new History_Async().execute(NetworkDefineConstant.SERVER_URL_VOT_LISTEN_MODE, "GET", "history",
								d[current_page][4].book_id + "", d[current_page][4].content_num + "");
						FifthI.setImageResource(R.drawable.ic_pause_black_48dp);
						FirstI.setClickable(false);

						ThirdI.setClickable(false);
						SecondI.setClickable(false);
						ForthI.setClickable(false);
						FifthI.setClickable(false);
						mflag = true;
						/*
						 * v.setBackgroundResource(R.drawable.
						 * ic_pause_black_48dp);
						 */
					} else {
						mMediaPlayer.pause();
						mMediaPlayer.reset();
						mflag = false;

						FifthI.setImageResource(R.drawable.ic_play_arrow_black_48dp);
						FirstI.setClickable(true);
						SecondI.setClickable(true);

						ThirdI.setClickable(true);
						FifthI.setClickable(true);
						ForthI.setClickable(true);
					}
					OCL OCL = new OCL();
					mMediaPlayer.setOnCompletionListener(OCL);

				} else if (d[current_page][4].contents_status == 4) {
					// 딜리트 전송.
					new Delete_Async().execute(NetworkDefineConstant.SERVER_URL_VOT_HISTORY_DELETE, "POST", "4");
					FirstI.setClickable(false);
					SecondI.setClickable(false);
					ThirdI.setClickable(false);
					ForthI.setClickable(false);
					FifthI.setClickable(false);
				}

			} else if (v == FirstSI) {

				/*
				 * ContentValues content = new ContentValues(4);
				 * content.put(Video.VideoColumns.DATE_ADDED,
				 * System.currentTimeMillis() / 1000);
				 * content.put(Video.Media.MIME_TYPE, "video/mp4");
				 * content.put(MediaStore.Video.Media.DATA,
				 * "http://s3-ap-northeast-1.amazonaws.com/votsounds/book_53/BOOK_53_CONTENT_2_MEMBER_1.wav"
				 * ); ContentResolver resolver =
				 * getActivity().getContentResolver(); Uri uri =
				 * resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				 * content); Intent sharingIntent = new Intent();
				 * sharingIntent.setAction(Intent.ACTION_SEND);
				 * sharingIntent.setType("text/plain");
				 * sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				 * "Title");
				 * sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM,
				 * uri);
				 * startActivityForResult(Intent.createChooser(sharingIntent,
				 * "Facebook"), 0);
				 * 
				 * ; Intent sharingIntent = new Intent(); ShareLinkContent
				 * content = new ShareLinkContent.Builder()
				 * .setContentUrl(Uri.parse(
				 * "http://s3-ap-northeast-1.amazonaws.com/votsounds/book_53/BOOK_53_CONTENT_2_MEMBER_1.wav"
				 * )) .setImageUrl(Uri.parse(
				 * "http://img.naver.net/static/www/u/2013/0731/nmms_224940510.gif"
				 * )).build();
				 * sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
				 * content); sharingIntent.setAction(Intent.ACTION_SEND);
				 * sharingIntent.setType("image/*");
				 * startActivity(Intent.createChooser(sharingIntent,
				 * "천개의목소리 음성 공유하기"));
				 */
				//////////////////////////////////////////////////////////////////////////
				/*
				 * String mySharedLink =
				 * "http://s3-ap-northeast-1.amazonaws.com/votsounds/book_53/BOOK_53_CONTENT_2_MEMBER_1.wav";
				 * String mySubject = "천개의 목소리"; int mySharedLinkport = 1001;
				 * Intent intent = new Intent();
				 * intent.setAction(Intent.ACTION_SEND); //
				 * intent.setType("text/plain"); intent.setType("audio/*");
				 * intent.setType("video/*"); intent.setType("text/plain");
				 */
				// intent.putExtra(Intent.EXTRA_SUBJECT, mySubject); // 페이스북은
				// 무시하는 정보이지만 트위터나 Mail 등등에서는 필요한 항목
				// intent.putExtra(Intent.EXTRA_TEXT, mySharedLink);
				/* intent.putExtra(name, value) */

				// 공유할 대상을 고르는 리스트를 출력

				/*
				 * Uri videoFileUri = null; Video ShareVideo = new
				 * ShareVideo.Builder() .setLocalUrl(videoFileUri) .build();
				 * ShareVideoContent content = new ShareVideoContent.Builder()
				 * .setVideo(video) .build();
				 */

				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

				shareIntent.setType("text/plain");

				shareIntent.putExtra(Intent.EXTRA_TEXT,d[current_page][0].path);
				startActivity(Intent.createChooser(shareIntent, "천개의목소리 음성 공유하기"));

				///////////////////////////////////////////////////////////////
				/*
				 * FacebookSdk.sdkInitialize(getActivity());
				 * 
				 * callbackManager = CallbackManager.Factory.create();
				 * List<String> permissionNeeds =
				 * Arrays.asList("publish_actions"); LoginManager manager =
				 * LoginManager.getInstance();
				 * 
				 * manager.logInWithPublishPermissions(getActivity(),
				 * permissionNeeds);
				 * LoginManager.getInstance().logInWithPublishPermissions(
				 * getActivity(), Arrays.asList("publish_actions"));
				 * manager.registerCallback(callbackManager, new
				 * FacebookCallback<LoginResult>() {
				 * 
				 * @Override public void onSuccess(LoginResult result) {
				 * sendMessage(); }
				 * 
				 * @Override public void onError(FacebookException error) {
				 * 
				 * }
				 * 
				 * @Override public void onCancel() {
				 * 
				 * } });
				 * 
				 * AccessToken accessToken =
				 * AccessToken.getCurrentAccessToken();
				 * 
				 * if (accessToken != null) { if
				 * (accessToken.getPermissions().contains("publish_actions")) {
				 * sendMessage(); return; } }
				 * LoginManager.getInstance().logInWithPublishPermissions(
				 * getActivity(), Arrays.asList("publish_actions"));
				 */
			} else if (v == SecondSI) {
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_TEXT,d[current_page][1].path);
				startActivity(Intent.createChooser(shareIntent, "천개의목소리 음성 공유하기"));

			} else if (v == ThirdSI) {
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_TEXT,d[current_page][2].path);
				startActivity(Intent.createChooser(shareIntent, "천개의목소리 음성 공유하기"));

			} else if (v == ForthSI) {
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_TEXT,d[current_page][3].path);
				startActivity(Intent.createChooser(shareIntent, "천개의목소리 음성 공유하기"));

			} else if (v == FifthSI) {
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_TEXT,d[current_page][4].path);
				startActivity(Intent.createChooser(shareIntent, "천개의목소리 음성 공유하기"));

			}
		}

		private void sendMessage() {
			// TODO Auto-generated method stub
			/*
			 * String message = "음성을 공유합니다.";
			 * 
			 * AccessToken accessToken = AccessToken.getCurrentAccessToken();
			 * String graphPath = "/me/feed"; Bundle parameters = new Bundle();
			 * parameters.putString("message", message);
			 * parameters.putString("link",
			 * "http://developers.facebook.com/docs/android");
			 * parameters.putString("picture",
			 * "http://s3-ap-northeast-1.amazonaws.com/votimages/book/BOOK_54.jpg"
			 * ); parameters.putString("name", "VOT");
			 * parameters.putString("description", "아이고"); GraphRequest request
			 * = new GraphRequest(accessToken, graphPath, parameters,
			 * HttpMethod.POST, new Callback() {
			 * 
			 * @Override public void onCompleted(GraphResponse response) { //
			 * TODO Auto-generated method stub
			 * 
			 * JSONObject data = response.getJSONObject(); String id = (data ==
			 * null) ? null : data.optString("id");
			 * Toast.makeText(getActivity(), "Post Object Id : " + id,
			 * Toast.LENGTH_SHORT).show();
			 * 
			 * } }); request.executeAsync();
			 */
			Uri videoFileUri = Uri
					.parse("http://s3-ap-northeast-1.amazonaws.com/votsounds/book_53/BOOK_53_CONTENT_2_MEMBER_1.wav");
			ShareVideo ShareVideo = new ShareVideo.Builder().setLocalUrl(videoFileUri).build();
			ShareVideoContent content = new ShareVideoContent.Builder().setVideo(ShareVideo).build();
			/*
			 * ShareLinkContent content = new
			 * ShareLinkContent.Builder().setContentUrl(Uri.parse())
			 * .setImageUrl(Uri.parse(
			 * "http://img.naver.net/static/www/u/2013/0731/nmms_224940510.gif")
			 * ) .setContentTitle("배고파")
			 * 
			 * .build();
			 */
			ShareApi.share(content, null);

		}

	}

	public class VOT_Async extends AsyncTask<String, Void, ArrayList<BookItemEntityObject>> {
		StringBuilder SB = new StringBuilder();
		int cu;

		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected ArrayList<BookItemEntityObject> doInBackground(String... params) {
			cu = Integer.parseInt(params[2]);
			SB.append("?memberid=" + Voice_Of_Thousands.member_id + "").append("&page=" + cu + ""); // 추후
																									// 변경
			return VOTHelperHandler.BookList(NetworkDefineConstant.SERVER_URL_VOT_MEMBER_INFO, "GET", SB);

		}

		@Override
		protected void onPostExecute(ArrayList<BookItemEntityObject> result) {
			// TODO Auto-generated method stub

			if (result != null && result.size() > 0) {

				if (result.get(0).result.equalsIgnoreCase("success")) {
					for (int i = 0; i < result.size(); i++) {

						RecordListItemData da = new RecordListItemData();
						da.contents_status = result.get(i).content_status;
						da.book_id = result.get(i).book_id;
						da.historyid = result.get(i).history_id;
						da.content_num = result.get(i).content_num;
						if (result.get(i).content_status == 0) {
							// 대기

						} else if (result.get(i).content_status == 1) {
							// 선택
						} else if (result.get(i).content_status == 2) {
							/*
							 * d.release =
							 * R.drawable.ic_new_releases_black_48dp;
							 */
							da.release = R.drawable.ic_more_horiz_black_24dp;
							da.body = result.get(i).book_title + "의 " + result.get(i).content_num + "번째 문장이 승인 대기중입니다.";

						} else if (result.get(i).content_status == 3) {

							da.statusimg = R.drawable.share;
							da.path = result.get(i).listenbook_path;
							da.body = result.get(i).book_title + "의 " + result.get(i).content_num + "번째 문장을 읽으셨습니다.";
							da.release = R.drawable.ic_play_arrow_black_48dp;

						} else if (result.get(i).content_status == 4) {
							da.release = R.drawable.ic_delete_black_48dp;
							da.body = result.get(i).book_title + "의 " + result.get(i).content_num + "번째 문장이 반려되었습니다."
									+ "\n" + result.get(i).reject_reason;

						}
						d[cu][i] = da;
					}
					switch (result.size()) {
					case 5:
						FifthB.setText(d[cu][4].body);
						FifthI.setImageResource(d[cu][4].release);
						FifthSI.setImageResource(d[cu][4].statusimg);
				//		Log.e("d [cu][4]", cu + ",4 생성함.");
					case 4:
						ForthB.setText(d[cu][3].body);
						ForthI.setImageResource(d[cu][3].release);
						ForthSI.setImageResource(d[cu][3].statusimg);
					//	Log.e("d [cu][3]", cu + ",3 생성함.");
					case 3:
						ThirdB.setText(d[cu][2].body);
						ThirdI.setImageResource(d[cu][2].release);
						ThirdSI.setImageResource(d[cu][2].statusimg);
						//Log.e("d [cu][2]", cu + ",2 생성함.");
					case 2:
						SecondB.setText(d[cu][1].body);
						SecondI.setImageResource(d[cu][1].release);
						SecondSI.setImageResource(d[cu][1].statusimg);
				//		Log.e("d [cu][1]", cu + ",1 생성함.");
					case 1:
						FirstB.setText(d[cu][0].body);
						FirstI.setImageResource(d[cu][0].release);
						FirstSI.setImageResource(d[cu][0].statusimg);
					//	Log.e("d [cu][0]", cu + ",0 생성함.");

					default:
						//Log.e("여기까지 --- d [cu][]", cu + "생성함. -----");
					}

				} else if (result.get(0).result.equalsIgnoreCase("fail")) {
					FirstB.setText("녹음 이력이 없습니다.");
					Log.e("adfasd", "녹음 이력 없음.");

				}
			}
		}

	}

	class History_Async extends AsyncTask<String, Void, ArrayList<BookItemEntityObject>> {
		StringBuilder queryStringParams = new StringBuilder();

		@Override
		protected ArrayList<BookItemEntityObject> doInBackground(String... params) {

			queryStringParams.append("?mode=" + params[2]).append("&bookid=" + params[3])

			.append("&contentnum=" + params[4]).append("&memberid=" + Voice_Of_Thousands.member_id) // 추후변수값으로
			// 변경할것.
			; // 추후변수값으로 변경할것.
			return VOTHelperHandler.BookList(params[0], params[1], queryStringParams);

		}

		@Override
		protected void onPostExecute(ArrayList<BookItemEntityObject> result) {
			// TODO Auto-generated method stub

			if (result.get(0).result.equalsIgnoreCase("success")) {

				String path = result.get(0).listenbook_path;

				ListenPage_Fragment.contentnum = result.get(0).content_num;
				if (mMediaPlayer == null) {
					mMediaPlayer = new MediaPlayer();
					mMediaPlayer.reset();
				}
				try {

					mMediaPlayer.setDataSource(path);
					mMediaPlayer.prepare();
					// seekTo가 실행되면 자동으로 prepare();가 먼저
					// 실행이
					// 됨

					// But.. setOnSeekto -> setOnPrepardListener XXXX
					mMediaPlayer.start();
					if (Hnumber ==1){
						FirstI.setClickable(true);
					}else if (Hnumber ==2){
						SecondI.setClickable(true);
					}else if (Hnumber ==3){
						ThirdI.setClickable(true);
					}else if (Hnumber ==4){
						ForthI.setClickable(true);
					}else if (Hnumber ==5){
						FifthI.setClickable(true);
					}

				} catch (Exception e) {
					Log.e("MyPage_Fragment.onPostExecute", e.toString());
				}

			} else {
				Toast.makeText(getActivity(), "서버에서 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();

			}

		}

	}

	class Delete_Async extends AsyncTask<String, Void, ArrayList<BookItemEntityObject>> {
		StringBuilder queryStringParams = new StringBuilder();
		int cuu;

		@Override
		protected ArrayList<BookItemEntityObject> doInBackground(String... params) {

			int a = Integer.parseInt(params[2]);

			queryStringParams.append("historyid=" + d[cuu][a].historyid); // 추후변수값으로
			// 변경할것.
			return VOTHelperHandler.BookList(params[0], params[1], queryStringParams);

		}

		@Override
		protected void onPostExecute(ArrayList<BookItemEntityObject> result) {
			// TODO Auto-generated method stub

			if (result != null) {
				if (result.get(0).result.equalsIgnoreCase("success")) {

			//		Toast.makeText(VOTApplication.getContext(), "이력 삭제에 성공하였습니다.", Toast.LENGTH_SHORT).show();
					MyPage_Fragment.mypage_vp.getAdapter().notifyDataSetChanged();
					// MyPage_Fragment.mypage_vp.notify();
					/*
					 * MyPage_Fragment.mypage_vp.notify(); MyPage_Fragment.
					 */
				} else {
					Toast.makeText(VOTApplication.getContext(), "이력 삭제에 실패하였습니다.다시 시도해 주세요", Toast.LENGTH_SHORT).show();

				}
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			cuu = current_position;
			super.onPreExecute();
		}

	}

	public class OCL implements MediaPlayer.OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer mp) {
			// TODO Auto-generated method stub
			mp.pause();
			mp.reset();
			if (btnnum == 0)
				FirstI.setImageResource(R.drawable.ic_play_arrow_black_48dp);
			else if (btnnum == 1)
				SecondI.setImageResource(R.drawable.ic_play_arrow_black_48dp);
			else if (btnnum == 2)

				ThirdI.setImageResource(R.drawable.ic_play_arrow_black_48dp);
			else if (btnnum == 4)

				FifthI.setImageResource(R.drawable.ic_play_arrow_black_48dp);
			else if (btnnum == 3)

				ForthI.setImageResource(R.drawable.ic_play_arrow_black_48dp);
			mflag = false;
			FirstI.setClickable(true);
			SecondI.setClickable(true);

			ThirdI.setClickable(true);
			FifthI.setClickable(true);
			ForthI.setClickable(true);
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		/*
		 * FragmentTransaction ft; ft = getFragmentManager().beginTransaction();
		 * MyPage_Fragment MF = new MyPage_Fragment();
		 * ft.replace(R.id.container, MF); ft.commit();
		 */
		setRetainInstance(true);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		// current_page = 0;
		super.onResume();
	}
}
