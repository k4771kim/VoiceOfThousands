package gyun.bo.voice_of_thousands_1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.viewpagerindicator.LinePageIndicator;
import com.viewpagerindicator.PageIndicator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import gyun.bo.voice_of_thousands_1.popup.MypageDialog;

public class MyPage_Fragment extends Fragment {
	ImageView img_change_btn;
	ImageButton nick_change_btn;
	public static ImageView leftK, rightK;
	public static TextView nick, profile_count, name_title, gender_title, count_title;
	ImageView gender;
	public static int total_page;
	AlertDialog.Builder mypagebuilder;
	View v;
	private String fileLocation;
	public static ViewPager mypage_vp;
	PageIndicator vp_indicator;
	mypage_adapter mypage_listadapter;
	public static int current_page;
	private static final int IMAGE_REQUEST_CODE = 1;
	public MypageDialog MD;
	public static DisplayImageOptions options;
	public ImageLoaderConfiguration ILC;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		clickbutton clkb = new clickbutton();
		v = inflater.inflate(R.layout.mypage, container, false);
		current_page = 0;
		leftK = (ImageView) v.findViewById(R.id.leftK);
		rightK = (ImageView) v.findViewById(R.id.rightK);

		leftK.setVisibility(View.INVISIBLE);
		rightK.setVisibility(View.INVISIBLE);

		nick = (TextView) v.findViewById(R.id.nick);
		gender = (ImageView) v.findViewById(R.id.myprofile_gender);

		profile_count = (TextView) v.findViewById(R.id.myprofile_count);
		nick.setText(Voice_Of_Thousands.member_name); // 나중에 로그인정보로 채욺.

		name_title = (TextView) v.findViewById(R.id.name_title);
		gender_title = (TextView) v.findViewById(R.id.gender_title);
		count_title = (TextView) v.findViewById(R.id.count_title);

		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "SEOULHANGANGB.TTF");
		name_title.setTypeface(tf);
		gender_title.setTypeface(tf);
		count_title.setTypeface(tf);
		;
		if (Voice_Of_Thousands.member_gender.equalsIgnoreCase("female")) {
			gender.setBackgroundResource(R.drawable.female);

		} else if (Voice_Of_Thousands.member_gender.equalsIgnoreCase("male")) {
			gender.setBackgroundResource(R.drawable.male);

		}

		profile_count.setText(Voice_Of_Thousands.participation_count + "");
		SlidingMenu sm = Voice_Of_Thousands.sm;
		sm.setSlidingEnabled(false);

		mypage_vp = (ViewPager) v.findViewById(R.id.mypage_pager);

		mypage_listadapter = new mypage_adapter(getFragmentManager());

		mypage_vp.setAdapter(mypage_listadapter);
		mypage_vp.setOffscreenPageLimit(10);

		if (total_page > 1) {
			MyPage_Fragment.rightK.setVisibility(View.VISIBLE);
		}
		ImageLoader.getInstance().clearMemoryCache();
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.null_profile)
				.showImageForEmptyUri(R.drawable.null_profile).showImageOnFail(R.drawable.null_profile)
				.cacheInMemory(false).cacheOnDisk(false).considerExifParams(false)
				.displayer(new RoundedBitmapDisplayer(20)).build();
		ILC = new ImageLoaderConfiguration.Builder(getActivity()).discCacheExtraOptions(300, 300, null)

		.build();

		ImageLoader.getInstance().init(ILC);

		mypage_vp.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

				Mypage_List.current_page = arg0;
				current_page = arg0;
				if (Mypage_List.mMediaPlayer != null) {
					Mypage_List.mMediaPlayer.pause();
					Mypage_List.mMediaPlayer.release();
					Mypage_List.mMediaPlayer = null;
					Mypage_List.mflag = false;
					mypage_vp.getAdapter().notifyDataSetChanged();
				}

				if (arg0 + 1 == total_page) {
					rightK.setVisibility(View.INVISIBLE);
				} else {
					rightK.setVisibility(View.VISIBLE);
				}
				if (arg0 == 0) {
					leftK.setVisibility(View.INVISIBLE);
				} else {
					leftK.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				if (arg1 < 1 && arg0 + arg1 > current_page) {
					rightK.setImageResource(R.drawable.ic_keyboard_arrow_right_blue_48dp);
				} else if (arg1 < 1 && arg0 + arg1 < current_page) {
					leftK.setImageResource(R.drawable.ic_keyboard_arrow_left_blue_48dp); // 블루로
																							// 바꾸기
				} else if (arg1 == 0.0) {
					rightK.setImageResource(R.drawable.ic_keyboard_arrow_right_black_48dp);
					leftK.setImageResource(R.drawable.ic_keyboard_arrow_left_black_48dp);
				}

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		/* mypage_vp.setOffscreenPageLimit(1); */

		LinePageIndicator mIndicator = (LinePageIndicator) v.findViewById(R.id.indicator);

		mIndicator.setViewPager(mypage_vp);

		img_change_btn = (ImageView) v.findViewById(R.id.img_change_btn);

		/*
		 * ImageLoader.getInstance().displayImage(Voice_Of_Thousands.
		 * member_image_URL, img_change_btn,Voice_Of_Thousands.options); Bitmap
		 * profile_Bitmap = ((BitmapDrawable)
		 * img_change_btn.getDrawable()).getBitmap(); Drawable d = new
		 * BitmapDrawable(profile_Bitmap); img_change_btn.setBackground(d);
		 * 
		 * 
		 * img_change_btn.setBackground(drawmenu_fragment.profileImg_drawable);
		 */
		String member_image_URL = Voice_Of_Thousands.member_image_URL;
		ImageLoader.getInstance().displayImage(member_image_URL, img_change_btn, options);
		/*
		 * Bitmap profile_Bitmap =
		 * ((BitmapDrawable)img_change_btn.getDrawable()).getBitmap();
		 */

		nick_change_btn = (ImageButton) v.findViewById(R.id.nick_change_btn);

		img_change_btn.setOnClickListener(clkb);
		nick_change_btn.setOnClickListener(clkb);

		return v;

	}

	class clickbutton implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (v == img_change_btn) {
				/*
				 * Intent intent = new Intent();
				 * intent.setAction(Intent.ACTION_GET_CONTENT);
				 * intent.setType("image/*"); startActivityForResult(intent,
				 * IMAGE_REQUEST_CODE);
				 */
				img_change_btn.setClickable(false);
				Intent intent = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				intent.setType("image/*");
				// intent.putExtra("crop", "true");
				// intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
				startActivityForResult(Intent.createChooser(intent, "사진을 선택하세요"), IMAGE_REQUEST_CODE);

			} else if (v == nick_change_btn) {
				MD = new MypageDialog(getActivity());
				MD.show();
			}
		}

	}

	private boolean isSDCARDMOUNTED() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED))
			return true;

		return false;
	}

	private Uri getTempUri() {
		return Uri.fromFile(getTempFile());
	}

	/** 외장메모리에 임시 이미지 파일을 생성하여 그 파일의 경로를 반환 */
	private File getTempFile() {
		if (isSDCARDMOUNTED()) {
			String TEMP_PHOTO_FILE = String.valueOf(System.currentTimeMillis());
			File f = new File(Environment.getExternalStorageDirectory(), // 외장메모리
																			// 경로
					"/kr/co/jeongdaehee/carfix/images/" + TEMP_PHOTO_FILE + ".jpg");
			try {
				f.createNewFile(); // 외장메모리에 파일 생성
			} catch (IOException e) {
			}

			return f;
		} else
			return null;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Uri returedImgURI = null;
		img_change_btn.setClickable(true);
		if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

			/*
			 * data.putExtra("crop", "true"); data.putExtra("aspectX", 0);
			 * data.putExtra("aspectY", 0); data.putExtra("outputX", 200);
			 * data.putExtra("outputY", 150);
			 */
			returedImgURI = data.getData();
			Bitmap bm = null;
			Bitmap upload_resized = null;
			Cursor cursor = null;
			try {

				/*
				 * ImageLoader.getInstance().displayImage(returedImgURI.toString
				 * (), img_change_btn,options);
				 * 
				 * bm =
				 * Images.Media.getBitmap(getActivity().getContentResolver(),
				 * returedImgURI); upload_resized =
				 * Bitmap.createScaledBitmap(bm, 300, 300, true); Bitmap resized
				 * = Bitmap.createScaledBitmap(upload_resized,
				 * img_change_btn.getWidth(), img_change_btn.getHeight(), true);
				 * 
				 * img_change_btn.setImageBitmap(resized); cursor =
				 * getActivity().getContentResolver().query(returedImgURI, new
				 * String[] { MediaStore.MediaColumns.DATA }, null, null, null);
				 * cursor.moveToFirst(); fileLocation = cursor.getString(0);
				 */
				cursor = getActivity().getContentResolver().query(returedImgURI,
						new String[] { MediaStore.MediaColumns.DATA }, null, null, null);
				cursor.moveToFirst();
				fileLocation = cursor.getString(0); // 절대주소
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;

				bm = BitmapFactory.decodeFile(fileLocation, options);
				Bitmap resize = Bitmap.createScaledBitmap(bm, img_change_btn.getWidth(), img_change_btn.getHeight(),
						true);
				img_change_btn.setImageBitmap(resize);

				Bitmap draw_resize = Bitmap.createScaledBitmap(bm, drawmenu_fragment.profileImg.getWidth(),
						drawmenu_fragment.profileImg.getHeight(), true);

				drawmenu_fragment.profileImg_drawable = new BitmapDrawable(draw_resize);

				if (Build.VERSION.SDK_INT >= 16) {

					drawmenu_fragment.profileImg.setBackground(drawmenu_fragment.profileImg_drawable);

				} else {

					drawmenu_fragment.profileImg.setBackgroundDrawable(drawmenu_fragment.profileImg_drawable);
				}
				
				
				
				
				drawmenu_fragment.profileImg.setImageBitmap(null);

				upload_resized = Bitmap.createScaledBitmap(bm, 300, 300, true);

			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
			String ex_stor = Environment.getExternalStorageDirectory().getAbsolutePath();
			String folder_name = "/" + "VOT" + "/";
			String file_name = "VOT_Thumb.jpg";
			String string_path = ex_stor + folder_name;
			File file_path;

			try {
				file_path = new File(string_path);

				if (!file_path.isDirectory()) {
					file_path.mkdirs();
				}
				FileOutputStream out = new FileOutputStream(string_path + file_name);
				upload_resized.compress(Bitmap.CompressFormat.JPEG, 50, out);
				out.close();
			} catch (Exception e) {

			}

			new ImageUpload().execute(string_path + file_name);

		}

	}

	public class ImageUpload extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... paramss) {
			// TODO Auto-generated method stub

			String resultstring = null;

			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, new ProtocolVersion("HTTP", 1, 1));
			HttpPost httpPost = new HttpPost(NetworkDefineConstant.SERVER_URL_VOT_PROFILEIMAGE_CHANGE);

			File pic = new File(paramss[0]);
			String filename = null;
			ArrayList<BookItemEntityObject> sendresult = new ArrayList<BookItemEntityObject>();
			BookItemEntityObject entity = new BookItemEntityObject();
			if (pic.exists()) {
				MultipartEntity multipartEntity = new MultipartEntity();

				String[] filenameparts = pic.getName().split("\\.");
				filename = "VOT_Thumb.jpg";
				JSONObject jsonObject = new JSONObject();
				try {

					multipartEntity.addPart("memberid", new StringBody(Voice_Of_Thousands.member_id + ""));
					// multipartEntity.addPart("voice", new FileBody(file,
					// filename,
					// "application/octet-stream", null));
					multipartEntity.addPart("value", new FileBody(pic, filename, "image/jpeg", null));
					httpPost.setEntity(multipartEntity);
					HttpResponse httpResponse = httpClient.execute(httpPost);
					Log.d("HTTPRESPONSe", httpResponse.toString());

					HttpEntity responseBody = null;
					StringBuilder sb = new StringBuilder();
					int responseCode = httpResponse.getStatusLine().getStatusCode();
					if (responseCode >= 200 && responseCode < 300) {
						responseBody = httpResponse.getEntity();

						BufferedReader rd = new BufferedReader(
								new InputStreamReader(httpResponse.getEntity().getContent()));

						StringBuffer result = new StringBuffer();
						String line = "";
						while ((line = rd.readLine()) != null) {
							result.append(line);
						}

						JSONObject o = new JSONObject(result.toString());
				//		Log.e("로그", result.toString());
						entity.result = o.getString("result");
						entity.member_image = o.getString("member_image");
						// 여기에 남은 카운트 횟수 추가.
						resultstring = entity.member_image;

					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return resultstring;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {

				if (result.equalsIgnoreCase("success")) {
					Voice_Of_Thousands.member_image_URL = result;
					ImageLoader.getInstance().clearDiskCache();
					ImageLoader.getInstance().clearMemoryCache();

				} else {

				}
			} else {

			}
		}
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub

		/* execute(NetworkDefineConstant.SERVER_URL_VOT_MEMBER_INFO, "GET"); */
		((Voice_Of_Thousands) getActivity()).ml = false;
		if (listfragment.view_current == 0) {
			Voice_Of_Thousands.sm.setSlidingEnabled(true);
		} else {
			Voice_Of_Thousands.sm.setSlidingEnabled(false);
		}

		if (Mypage_List.mMediaPlayer != null) {
			Mypage_List.mMediaPlayer.pause();
			Mypage_List.mMediaPlayer.release();
			Mypage_List.mMediaPlayer = null;
			Mypage_List.mflag = false;
		}
		super.onDestroyView();
	}

}
