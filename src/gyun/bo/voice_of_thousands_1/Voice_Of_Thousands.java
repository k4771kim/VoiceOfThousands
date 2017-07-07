package gyun.bo.voice_of_thousands_1;

import java.util.ArrayList;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

public class Voice_Of_Thousands extends SlidingFragmentActivity implements SlidingActivityBase {
	public View frag;

	/* Toolbar toolbar; */

	public static ActionBar actionbar;
	public static FragmentTransaction ft;
	public static int location;
	public static MenuItem listen_mode_icon;
	listfragment ReadingBookListFragment; // 프래그먼트 선언
	VOT_CreditPage CP;
	MyPage_Fragment MyPageFragment;
	ViewPager pager;
	private BackPressCloseHandler backpresshandler;
	Record_Fragment RecodeFragment; // 여기까지
	VOT_Tab_Adapter madapter;
	public static boolean ml = false;
	public static int naviflag = 0;
	public static int menuflag = 0;
	public static SlidingMenu sm;
	// 회원가입정보
	public static String member_name, member_gender, member_image_URL;
	public static int member_id, participation_count;
	public static DisplayImageOptions options;
	ImageLoaderConfiguration ILC;
	private static Context mContext;

	public static Context getContext() {
		return mContext;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		location = 0;
		mContext = this;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.book_loading)
				.showImageForEmptyUri(R.drawable.ic_headset_white_24dp).showImageOnFail(R.drawable.name)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();
		ILC = new ImageLoaderConfiguration.Builder(getContext()).build();

		ImageLoader.getInstance().init(ILC);
		backpresshandler = new BackPressCloseHandler(this);
		Intent intent = getIntent();
		member_id = intent.getIntExtra("member_id", 0);
		member_name = intent.getStringExtra("member_name");
		member_gender = intent.getStringExtra("member_gender");
		member_image_URL = intent.getStringExtra("member_image");
		participation_count = intent.getIntExtra("participation_count", 0);

		setContentView(R.layout.activity_voice_of_thousands);
		setBehindContentView(R.layout.menu_main);

		ft = getSupportFragmentManager().beginTransaction();

		if (savedInstanceState == null) {
			ft.replace(R.id.container, new listfragment());
			ft.add(R.id.menu_container, new drawmenu_fragment());
			ft.commit();

		}

		sm = getSlidingMenu();
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		/* sm.setBehindOffset(240); */
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

		/*
		 * actionbar.addTab(actionbar.newTab().setText("ㅠ.ㅠ"));
		 */
		/*
		 * if (actionbar.getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS)
		 * { actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD); }
		 * else { actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); }
		 */
		new VOT_Async().execute(NetworkDefineConstant.SERVER_URL_VOT_CONNECTIONEND, "POST");
		Record_Fragment.setflag = false;
		if (Record_Fragment.BookContent != null)
			Record_Fragment.BookContent.clear();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) { // 옵션메뉴 호출.
		getMenuInflater().inflate(R.menu.voice__of__thousands, menu);
		listen_mode_icon = menu.findItem(R.id.Listen_mode);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.Listen_mode) { // 감상모드 버튼 클릭시

			Intent intent = new Intent(getApplication(), VOT_Listen_Activity.class);

			startActivity(intent);
			overridePendingTransition(R.anim.enter, R.anim.hold);

			return true;
		}

		else if (item.getItemId() == android.R.id.home) {

			/* if (!mDrawerLayout.isDrawerOpen(frag) */
			if (location == 0) {
				/* DrawerControl(true); */
				toggle();
				listfragment.all.performClick();

				FrameLayout container = (FrameLayout) findViewById(R.id.container);
				/*
				 * container.setFocusable(false); container.setClickable(false);
				 */

			} else { // 네비게이션 아이콘이 <-인 경우.

				onBackPressed();/*
								 * actionbar.setHomeAsUpIndicator(R.drawable.
								 * ic_menu_white_48dp);
								 */
				/* toolbar.setNavigationIcon(R.drawable.ic_menu_white_48dp); */
			}
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * public void DrawerControl(boolean d) {
	 * 
	 * drawmenu_fragment.actiflag = 0; if (d) { mDrawerLayout.openDrawer(frag);
	 * 
	 * } else { mDrawerLayout.closeDrawer(frag);
	 * 
	 * }
	 * 
	 * }
	 */
	public void FragmentChange(int fragment, Bundle bundle) {
		ft = getSupportFragmentManager().beginTransaction();
		switch (fragment) {
		case 0:
			ReadingBookListFragment = new listfragment();

			ft.add(R.id.container, ReadingBookListFragment);
			location = 0;
			naviflag = 0;
			break;

		case 1:
			if (ml == false) {
				MyPageFragment = new MyPage_Fragment();
				ft.add(R.id.container, MyPageFragment);
				ft.addToBackStack(null);

				actionbar.setHomeAsUpIndicator(R.drawable.back);

				naviflag = 1;
				location++;
				ml = true;

			}
			toggle();

			break;
		case 2:

			RecodeFragment = new Record_Fragment();
			ft.add(R.id.container, RecodeFragment, "RecodeFragment");

			RecodeFragment.setArguments(bundle);

			ft.addToBackStack(null);
			actionbar.setHomeAsUpIndicator(R.drawable.back);
			location++;
			naviflag = 1;
			break;

		case 3:

			CP = new VOT_CreditPage();
			ft.add(R.id.container, CP, "creditPage");

			ft.addToBackStack(null);
			actionbar.setHomeAsUpIndicator(R.drawable.back);
			location++;
			naviflag = 1;
			break;
		}

		ft.commit();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		if (location != 0) {
			super.onBackPressed();
			location = 0;
			/* if (getFragmentManager().getBackStackEntryCount()==0){ */
			// if (location > 0) {
			// actionbar.setHomeAsUpIndicator(R.drawable.back);
			/*
			 * toolbar.setNavigationIcon(R.drawable.
			 * ic_keyboard_backspace_white_24dp);
			 */
			// naviflag = 1;

			/* tabHost.setVisibility(View.VISIBLE); */
			// }

			// else {
		
			actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
			/*
			 * toolbar.setNavigationIcon(R.drawable.ic_menu_white_48dp);
			 */
			naviflag = 0;

		} else {
		backpresshandler.onBackPressed();
		}
		/* tabHost.setVisibility(View.GONE); */
		// }

	}

	private class TabContentFragment extends Fragment {
		private String mText;

		public TabContentFragment(String text) {
			mText = text;
		}

		public String getText() {
			return mText;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View fragView = inflater.inflate(R.layout.read_book_list, container, false);

			return fragView;
		}

	}

	class VOT_Async extends AsyncTask<String, Void, ArrayList<BookItemEntityObject>> {
		public StringBuilder queryStringParams = new StringBuilder();

		protected void onPreExecute() {

			queryStringParams.append("memberid=" + Voice_Of_Thousands.member_id); // 멤버는
																					// 추후
																					// 바꿀것.

			super.onPreExecute();

		}

		@Override
		protected ArrayList<BookItemEntityObject> doInBackground(String... params) {

			return VOTHelperHandler.BookList(params[0], params[1], queryStringParams);

		}

		@Override
		protected void onPostExecute(ArrayList<BookItemEntityObject> result) {
			// TODO Auto-generated method stub

			if (result == null) {

				Toast.makeText(getBaseContext(), "서버에서 정보를 받아올 수 없습니다.", Toast.LENGTH_SHORT).show();

			} else if (result != null && result.size() > 0) {

				if (result == null) {
					Toast.makeText(getBaseContext(), "서버로 전송을 실패했습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
				}

				else if (result != null && result.size() > 0) {

					if (result.get(0).content_text.toString() == "sucess") {
						// Log.e("석세스", "성공");
					} else {

					}

				}
			}
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		new VOT_Async().execute(NetworkDefineConstant.SERVER_URL_VOT_CONNECTIONEND, "POST");
		Record_Fragment.setflag = false;
		if (Record_Fragment.BookContent != null)
			Record_Fragment.BookContent.clear();
		super.onDestroy();
	}

}
