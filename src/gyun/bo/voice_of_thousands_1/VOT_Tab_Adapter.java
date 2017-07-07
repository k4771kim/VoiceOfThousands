package gyun.bo.voice_of_thousands_1;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class VOT_Tab_Adapter extends FragmentPagerAdapter
		implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

	//TabsAdapter : TabHost와 Pager를 연동하여 관리할 수 있도록 만든 class
	
	private final Context context;
	
	private final TabHost tabHost;
	private final ViewPager viewPager;
	private final FragmentManager fragmentManager;
	private final ArrayList<TabInfo> Tabs = new ArrayList<TabInfo>();
	private final static String FIELD_KEY_PREFIX = "tabInfo";

	//Tab의 정보를 담고 있는 class
	static final class TabInfo {

		private final String tag;
		private final Class<?> clss;
		private final Bundle args;
		private Fragment fragment;

		TabInfo(String _tag, Class<?> _clss, Bundle _args) {
			tag = _tag;
			clss = _clss;
			args = _args;

		}

	}

	static class DummyTabFactory implements TabHost.TabContentFactory {

		private final Context context;

		public DummyTabFactory(Context context) {
			this.context = context;
		}

		@Override
		public View createTabContent(String tag) {
			// TODO Auto-generated method stub
			View v = new View(context);
			v.setMinimumHeight(0);
			v.setMinimumWidth(0);
			return v;

		}

	}

	public VOT_Tab_Adapter(FragmentActivity activity, TabHost tabHost, ViewPager pager) {
		// TODO Auto-generated constructor stub
		this(activity, activity.getSupportFragmentManager(), tabHost, pager);
	}

	public VOT_Tab_Adapter(Context context, FragmentManager fragmentManager, TabHost tabHost, ViewPager pager) {
		// TODO Auto-generated constructor stub
		super(fragmentManager);
		this.context = context;
		this.fragmentManager = fragmentManager;
		this.viewPager = pager;
		this.tabHost = tabHost;
		
		
		this.viewPager.setAdapter(this);
		this.viewPager.setOnPageChangeListener(this);
		tabHost.setOnTabChangedListener(this);
	}

	public void onRestoreInstanceState(Bundle savedInstanceState) {

		for (TabInfo info : Tabs) {
			String keyfield = FIELD_KEY_PREFIX + info.tag;
			info.fragment = this.fragmentManager.getFragment(savedInstanceState, keyfield);
		}
	}

	public void onSaveInstanceState(Bundle outState) {

		for (TabInfo info : Tabs) {

			String keyfield = FIELD_KEY_PREFIX + info.tag;
			if (info.fragment != null) {

				this.fragmentManager.putFragment(outState, keyfield, info.fragment);

			}

		}

	}

	public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {

		tabSpec.setContent(new DummyTabFactory(context));
		String tag = tabSpec.getTag();
		TabInfo info = new TabInfo(tag, clss, args);
		Tabs.add(info);
		notifyDataSetChanged();
		tabHost.addTab(tabSpec);

	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		TabInfo info = Tabs.get(position);
		info.fragment = Fragment.instantiate(context, info.clss.getName(), info.args);
		return info.fragment;
	}

	OnTabChangeListener tabChangeListener;

	public void setOnTabChangedListener(OnTabChangeListener listener) {



		tabChangeListener = listener;

	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub
		if (pageChangeListener != null) {

			pageChangeListener.onPageScrollStateChanged(state);
		}

	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// TODO Auto-generated method stub
		if (pageChangeListener != null) {
			pageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
		}

	}

	@Override
	public void onPageSelected(int position) {


		// TODO Auto-generated method stub
		TabWidget widget = tabHost.getTabWidget();
		int oldFocusability = widget.getDescendantFocusability();
		widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		tabHost.setCurrentTab(position);
		widget.setDescendantFocusability(oldFocusability);
		if (pageChangeListener != null) {
			pageChangeListener.onPageSelected(position);
		}

	}

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub

		int position = tabHost.getCurrentTab();
		viewPager.setCurrentItem(position);
		if (tabChangeListener != null)
			tabChangeListener.onTabChanged(tabId);

	}

	public Fragment getCurrentTabFragment() {

		return getTabFragment(tabHost.getCurrentTab());

	}

	public Fragment getTabFragment(int position) {



		TabInfo info = Tabs.get(position);
		if (info != null)
			return info.fragment;

		return null;

	}

	OnPageChangeListener pageChangeListener;

	public void setOnPageChangeListener(OnPageChangeListener listener) {

		pageChangeListener = listener;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Tabs.size();
	}

}
