package gyun.bo.voice_of_thousands_1;

import com.viewpagerindicator.TabPageIndicator;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class listfragment extends Fragment {

	/* public int id; */
	public static boolean barflag;
	public static int view_current;
	public static Button all, essay, poem;

	public static ViewPager VOT_VP;
	TabPageIndicator VOT_TPI;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.read_book_list, container, false);
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "SEOULHANGANGB.TTF");
		all = (Button) v.findViewById(R.id.all);
		essay = (Button) v.findViewById(R.id.essay);
		poem = (Button) v.findViewById(R.id.poem);
		all.setTypeface(tf);
		essay.setTypeface(tf);
		poem.setTypeface(tf);
		VOT_VP = (ViewPager) v.findViewById(R.id.read_pager);

		read_book_list_viewpager_adpater list_viewpager_adpater = new read_book_list_viewpager_adpater(
				getFragmentManager());
		VOT_VP.setAdapter(list_viewpager_adpater);

		VOT_TPI = (TabPageIndicator) v.findViewById(R.id.read_indicator);
		VOT_TPI.setViewPager(VOT_VP);
		listfragment.all.setAlpha(1f);
		listfragment.essay.setAlpha(0.3f);
		listfragment.poem.setAlpha(0.3f);
		Voice_Of_Thousands.sm.setSlidingEnabled(true);
		
		VOT_VP.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				if (position == 0) {
					view_current = 0;
					listfragment.all.setAlpha(1f);
					listfragment.essay.setAlpha(0.3f);
					listfragment.poem.setAlpha(0.3f);
					
					Voice_Of_Thousands.sm.setSlidingEnabled(true);
				} else if (position == 1) {
					view_current = 1;
					Voice_Of_Thousands.sm.setSlidingEnabled(false);
					listfragment.all.setAlpha(0.3f);
					listfragment.essay.setAlpha(1f);
					listfragment.poem.setAlpha(0.3f);
				} else if (position == 2) {
					view_current = 2;
					listfragment.all.setAlpha(0.3f);
					listfragment.essay.setAlpha(0.3f);
					listfragment.poem.setAlpha(1f);
					Voice_Of_Thousands.sm.setSlidingEnabled(false);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		
		all.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				VOT_VP.setCurrentItem(0);
				// TODO Auto-generated method stub
				
				
			}
		});
		essay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				VOT_VP.setCurrentItem(1);
				// TODO Auto-generated method stub
				
				
			}
		});
		poem.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				VOT_VP.setCurrentItem(2);
				// TODO Auto-generated method stub
				
				
			}
		});
		
		return v;
	}
	/*
	 * @Override public void onRestart() { // TODO Auto-generated method stub
	 * read_book_list_fragment.listview.setAdapter(read_book_list_fragment.
	 * sAdapter); super.onRestart(); }
	 */

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
	
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();
	}

	


}
