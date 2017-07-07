package gyun.bo.voice_of_thousands_1;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class drawmenu_fragment extends Fragment {
	private ListView mDrawerList;
	ImageView pushicon;
	ArrayAdapter<String> adapter;
	public static int actiflag = 0;
	Bitmap profile_Bitmap;
	CheckBox push_check;
	public static Drawable profileImg_drawable;
	public static ImageView profileImg;
	public static TextView member_nick, member_partnum;
	ImageView member_gender;
	RelativeLayout sliding_mypage, sliding_notify, sliding_push, sliding_logout;
	DisplayImageOptions options;
	ImageLoaderConfiguration ILC;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View v = inflater.inflate(R.layout.drawer_menu, container,
				false);/*
						 * mDrawerList = (ListView)
						 * v.findViewById(R.id.navdrawer);
						 */
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.book_loading)
				.showImageForEmptyUri(R.drawable.ic_headset_white_24dp).showImageOnFail(R.drawable.name)
				.cacheInMemory(false).cacheOnDisk(false).considerExifParams(false)
				.displayer(new RoundedBitmapDisplayer(20)).build();
		ILC = new ImageLoaderConfiguration.Builder(getActivity()).build();

		ImageLoader.getInstance().init(ILC);
		ImageLoader.getInstance().clearDiskCache();
		profile_Bitmap = null;

		profileImg = (ImageView) v.findViewById(R.id.sliding_profileimg);
		member_nick = (TextView) v.findViewById(R.id.member_name);
		member_gender = (ImageView) v.findViewById(R.id.member_gender);

		ImageLoader.getInstance().displayImage(Voice_Of_Thousands.member_image_URL, profileImg, options);

		profile_Bitmap = ((BitmapDrawable) profileImg.getDrawable()).getBitmap();
		profileImg_drawable = new BitmapDrawable(profile_Bitmap);

		if (Build.VERSION.SDK_INT >= 16) {

			profileImg.setBackground(profileImg_drawable);

		} else {

			profileImg.setBackgroundDrawable(profileImg_drawable);
		}

		profileImg.setImageBitmap(null);
		member_nick.setText(Voice_Of_Thousands.member_name);

		if (Voice_Of_Thousands.member_gender.equalsIgnoreCase("female")) {
			member_gender.setBackgroundResource(R.drawable.female);

		} else if (Voice_Of_Thousands.member_gender.equalsIgnoreCase("male")) {
			member_gender.setBackgroundResource(R.drawable.male);

		}

		/* member_partnum.setText(Voice_Of_Thousands.participation_count); */

		sliding_mypage = (RelativeLayout) v.findViewById(R.id.sliding_mypage);

		sliding_mypage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((Voice_Of_Thousands) getActivity()).FragmentChange(1, null);

			}
		});

		sliding_notify = (RelativeLayout) v.findViewById(R.id.sliding_notify);
		sliding_notify.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((Voice_Of_Thousands) getActivity()).FragmentChange(3, null);
			}
		});
		sliding_push = (RelativeLayout) v.findViewById(R.id.sliding_push);
		push_check = (CheckBox) v.findViewById(R.id.push_check);
		SharedPreferences prefs = getActivity().getSharedPreferences(VOT_GCM_DefineConstant.PREFERENCES_FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = prefs.edit();

		pushicon = (ImageView) v.findViewById(R.id.push_icon);
		if (prefs.getString("push", "on").equalsIgnoreCase("on")) {
			pushicon.setImageResource(R.drawable.push);
			push_check.setChecked(true);
		} else {
			pushicon.setImageResource(R.drawable.pushicon_off);
			push_check.setChecked(false);
		}

		if (push_check.isChecked()) {
			pushicon.setImageResource(R.drawable.push);

		} else {
			pushicon.setImageResource(R.drawable.pushicon_off);

		}

		sliding_push.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!push_check.isChecked())
					push_check.setChecked(true);

				else
					push_check.setChecked(false);

			}
		});
		push_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			SharedPreferences prefs = getActivity().getSharedPreferences(VOT_GCM_DefineConstant.PREFERENCES_FILE_NAME,
					Context.MODE_PRIVATE);
			SharedPreferences.Editor prefsEditor = prefs.edit();

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					pushicon.setImageResource(R.drawable.push);
					prefsEditor.putString("push", "on");

				} else {
					pushicon.setImageResource(R.drawable.pushicon_off);
					prefsEditor.putString("push", "off");
				}
				prefsEditor.commit();
			}
		});
		sliding_logout = (RelativeLayout) v.findViewById(R.id.sliding_logout);

		sliding_logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				VOTApplication.prefsEditor.putString("recent_login_way", "NULL");
				VOTApplication.prefsEditor.putString("login_token", "NULL");

				VOTApplication.prefsEditor.commit();
				getActivity().finish();
				startActivity(new Intent(getActivity(), VOT_Login_Page.class));

			}
		});
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "SEOULHANGANGB.TTF");
		TextView sm = (TextView) v.findViewById(R.id.sl_my);

		TextView sn = (TextView) v.findViewById(R.id.sl_nt);
		TextView sp = (TextView) v.findViewById(R.id.sl_ps);
		TextView sl = (TextView) v.findViewById(R.id.sl_lo);
		sm.setTypeface(tf);
		sn.setTypeface(tf);
		sp.setTypeface(tf);
		sl.setTypeface(tf);
		/*
		 * String[] values = new String[] { "마이 페이지", "이용 안내", "로그아웃", };
		 * 
		 * adapter = new ArrayAdapter<String>(getActivity(),
		 * android.R.layout.simple_list_item_1, values);
		 * mDrawerList.setAdapter(adapter);
		 * 
		 * mDrawerList.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 * int position, long id) { // TODO Auto-generated method stub
		 * 
		 * if (actiflag == 0) {
		 * 
		 * if (position == 0) {
		 * 
		 * Toast.makeText(getActivity(), "마이페이지", Toast.LENGTH_SHORT).show();
		 * 
		 * 
		 * 
		 * } if (position == 1) {
		 * 
		 * Toast.makeText(getActivity(), "이용안내", Toast.LENGTH_SHORT).show();
		 * 
		 * } if (position == 2) {
		 * 
		 * Toast.makeText(getActivity(), "로그아웃", Toast.LENGTH_SHORT).show();
		 * 
		 * } }
		 * 
		 * 
		 * }
		 * 
		 * });
		 */

		return v;

	}

	/*
	 * class VOT_Async extends AsyncTask<String, Void,
	 * ArrayList<BookItemEntityObject>> { ProgressDialog dialog;
	 * 
	 * protected void onPreExecute() { try { dialog =
	 * ProgressDialog.show(getActivity(), "",
	 * "서버에서 회원 정보를 불러오고 있습니다. 잠시만 기다려 주세요.", true);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * super.onPreExecute(); }
	 * 
	 * @Override protected ArrayList<BookItemEntityObject>
	 * doInBackground(String... params) {
	 * 
	 * return VOTHelperHandler.BookList(params[0], params[1], null);
	 * 
	 * }
	 * 
	 * @Override protected void onPostExecute(ArrayList<BookItemEntityObject>
	 * result) { // TODO Auto-generated method stub dialog.dismiss();
	 * 
	 * if (result == null) {
	 * 
	 * Toast.makeText(getActivity(), "서버에서 정보를 받아올 수 없습니다.",
	 * Toast.LENGTH_SHORT).show(); } else if (result != null && result.size() >
	 * 0) {
	 * 
	 * member_nick.setText(result.get(0).member_name);
	 * member_gender.setText(result.get(0).member_gender);
	 * member_partnum.setText(result.get(0).member_id + ""); // partnum은 // 회원
	 * // 지금까지 // 녹음한 // 숫자임.
	 * profileImg.setImageBitmap(result.get(0).memberimg);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * public class draw_item extends FrameLayout {
	 * 
	 * TextView textbody; ImageView icon;
	 * 
	 * public draw_item(Context context) { super(context); init(); // TODO
	 * Auto-generated constructor stub }
	 * 
	 * private void init() {
	 * LayoutInflater.from(getContext()).inflate(R.layout.sliding_listitem,
	 * this); Typeface typeface =
	 * Typeface.createFromAsset(getContext().getAssets(), "nanum.ttf"); textbody
	 * = (TextView) findViewById(R.id.slide_text); icon = (ImageView)
	 * findViewById(R.id.slide_icon); textbody.setTypeface(typeface); // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * }
	 * 
	 */
}
