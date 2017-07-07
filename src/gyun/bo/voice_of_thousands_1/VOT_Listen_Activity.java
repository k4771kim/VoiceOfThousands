package gyun.bo.voice_of_thousands_1;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

public class VOT_Listen_Activity extends ActionBarActivity {
	public View frag;
	/* public DrawerLayout mDrawerLayout; */
	ActionBar actionbar;
	Listenlist_Fragment ListenlistFragment;
	public static int location = 0;
	ListenPage_Fragment ListenPageFragment;
	MyPage_Fragment MyPageFragment;
	public static int naviflag = 0;
	public static Context Listen_Context;

	public static Context getContext() {
		return Listen_Context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.listen_activity);
		actionbar = getSupportActionBar();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		Listen_Context = this;
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setHomeAsUpIndicator(R.drawable.back);

		/* frag = (View) findViewById(R.id.drawer); */
		drawmenu_fragment.actiflag = 1;

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		ListenlistFragment = new Listenlist_Fragment();
		ft.replace(R.id.container, ListenlistFragment);
		ft.commit();
		/* toolbar.setNavigationIcon(R.drawable.back); */

		/*
		 * mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		 * mDrawerLayout.setStatusBarBackground(
		 * getResources().getColor(R.color.
		 * abc_background_cache_hint_selector_material_dark));
		 */

	}

	public boolean onCreateOptionsMenu(Menu menu) { // 옵션메뉴 호출.

		/* getMenuInflater().inflate(R.menu.vot_listen_activity_menu, menu); */
		return true;

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.Back_key) {
			onBackPressed();
		} else if (item.getItemId() == android.R.id.home) {

			/*
			 * if (naviflag == 0){ if (!mDrawerLayout.isDrawerOpen(frag)) {
			 * DrawerControl(true); } else { DrawerControl(false); } }
			 */
			onBackPressed();

			/*
			 * else{ //네비게이션 아이콘이 <-인 경우.
			 * 
			 * onBackPressed();
			 * toolbar.setNavigationIcon(R.drawable.ic_menu_white_48dp); }
			 */

		}
		return true;

	}

	/*
	 * public void DrawerControl(boolean d) {
	 * 
	 * if (d) { mDrawerLayout.openDrawer(frag);
	 * 
	 * } else { mDrawerLayout.closeDrawer(frag);
	 * 
	 * }
	 * 
	 * }
	 */

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		drawmenu_fragment.actiflag = 0;
		super.onDestroy();
	}

	public void FragmentChange(int fragment, Bundle bundle) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		switch (fragment) {

		case 0:
			ListenPageFragment = new ListenPage_Fragment();
			ListenPageFragment.setArguments(bundle);
			ft.add(R.id.container, ListenPageFragment);
			ft.addToBackStack(null);
			/*
			 * toolbar.setNavigationIcon(R.drawable. back);
			 */
			naviflag = 1;
			break;

		case 1:
			if (location == 0) {

				MyPageFragment = new MyPage_Fragment();

				ft.replace(R.id.container, MyPageFragment);
				ft.addToBackStack(
						null);/*
								 * toolbar.setNavigationIcon(R.drawable.back);
								 */
				naviflag = 1;
				location++;
			}

		}
		ft.commit();

	}

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		location = 0;
		if (getFragmentManager().getBackStackEntryCount() == 0) {
			overridePendingTransition(R.anim.hold, R.anim.leave);
		}

		else {

		}

		/*
		 * if (getFragmentManager().getBackStackEntryCount() == 0) {
		 * 
		 * toolbar.setNavigationIcon(R.drawable.ic_menu_white_48dp); naviflag =
		 * 0; }
		 * 
		 * else {
		 * 
		 * toolbar.setNavigationIcon(R.drawable.back ); naviflag = 1; }
		 */

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

		super.finish();
	}

	@Override
	public void finishActivity(int requestCode) {
		// TODO Auto-generated method stub
		super.finishActivity(requestCode);

	}

}
