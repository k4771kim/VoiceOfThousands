package gyun.bo.voice_of_thousands_1;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class Listenlist_Fragment extends Fragment {

	Bundle bundle;

	ImageButton RB, LB;
	public static int pagenum;
	public static int totalpage;
	public int scrool_current;
	public static int current_page;
	public Listen_List_Adapter mAdapter;
	public ViewPager listen_pager;
	public static DisplayImageOptions options;
	public ImageLoaderConfiguration ILC;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View v = inflater.inflate(R.layout.listen_book_list, container, false);

		// listen_list = (GridView) v.findViewById(R.id.listen_book_list);

		pagenum = 0;

		// new
		// VOT_Async().execute(NetworkDefineConstant.SERVER_URL_VOT_LISTEN_LIST);

		RB = (ImageButton) v.findViewById(R.id.right_btn);
		LB = (ImageButton) v.findViewById(R.id.left_btn);
		LB.setVisibility(View.INVISIBLE);
		RB.setVisibility(View.INVISIBLE);

		listen_pager = (ViewPager) v.findViewById(R.id.listenbook_pager);
		mAdapter = new Listen_List_Adapter(getFragmentManager());
		listen_pager.setAdapter(mAdapter);
		listen_pager.setOffscreenPageLimit(10);

		ImageLoader.getInstance().clearMemoryCache();
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.book_loading)
				.showImageForEmptyUri(R.drawable.ic_headset_white_24dp).showImageOnFail(R.drawable.name)
				.cacheInMemory(false).cacheOnDisk(false).considerExifParams(false)
				.displayer(new RoundedBitmapDisplayer(20)).build();
		ILC = new ImageLoaderConfiguration.Builder(getActivity()).discCacheExtraOptions(300, 300, null)

		.build();
		if (totalpage > 1) {
			RB.setVisibility(View.VISIBLE);
		}

		ImageLoader.getInstance().init(ILC);

		listen_pager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				listen_book_item.current_page = arg0;
				current_page = arg0;

				if (arg0 + 1 == totalpage) {
					RB.setVisibility(View.INVISIBLE);
				} else {
					RB.setVisibility(View.VISIBLE);
				}
				if (arg0 == 0) {
					LB.setVisibility(View.INVISIBLE);
				} else {
					LB.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			//	Log.e("onPageScrolled", arg0 + "/" + arg1 + "/" + arg2);

				if (arg1 < 1 && arg0 + arg1 > current_page) {
					RB.setImageResource(R.drawable.ic_keyboard_arrow_right_blue_48dp);
				} else if (arg1 < 1 && arg0 + arg1 < current_page) {
					LB.setImageResource(R.drawable.ic_keyboard_arrow_left_blue_48dp); // 블루로
																						// 바꾸기
				} else if (arg1 == 0.0) {
					RB.setImageResource(R.drawable.ic_keyboard_arrow_right_black_48dp);
					LB.setImageResource(R.drawable.ic_keyboard_arrow_left_black_48dp);
				}

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			//	Log.e("OnPageScroolStateChanged", arg0 + "");
			}
		});

		/*
		 * RB.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * if (totalpage <= pagenum) {
		 * 
		 * } else { pagenum++; mAdapter.clear(); new VOT_Async().execute(1);
		 * 
		 * }
		 * 
		 * if(pagenum>1){ LB.setVisibility(View.VISIBLE); } if(totalpage ==
		 * pagenum){ RB.setVisibility(View.INVISIBLE); }
		 * 
		 * } });
		 * 
		 * LB.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * if (pagenum > 1) { pagenum--; mAdapter.clear(); new
		 * VOT_Async().execute(1);
		 * 
		 * } else {
		 * 
		 * } if(pagenum==1){ LB.setVisibility(View.INVISIBLE); } if(totalpage >
		 * pagenum){ RB.setVisibility(View.VISIBLE); } } });
		 *//*
			 * listen_list.setOnItemClickListener(new OnItemClickListener() {
			 * 
			 * @Override public void onItemClick(AdapterView<?> parent, View
			 * view, int position, long id) { // TODO Auto-generated method stub
			 * 
			 * Toast.makeText(getActivity(), mAdapter.items.get(position).title
			 * + "를 보자!!!!", Toast.LENGTH_SHORT) .show(); bundle = new Bundle();
			 * bundle.putInt("bookid", mAdapter.items.get(position).bookid);
			 * bundle.putInt("position", position); bundle.putString("title",
			 * mAdapter.items.get(position).title.toString());
			 * 
			 * // Listen_Mode로 보낼 번들. ((VOT_Listen_Activity)
			 * getActivity()).FragmentChange(0, bundle);
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
	 * protected void onPreExecute() {
	 * 
	 * try { dialog = ProgressDialog.show(getActivity(), "",
	 * "서버에서 책 정보를 불러오고 있습니다. 잠시만 기다려 주세요.", true);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } super.onPreExecute(); }
	 * 
	 * @Override protected ArrayList<BookItemEntityObject>
	 * doInBackground(String... params) { return
	 * VOTHelperHandler.BookList(params[0], "GET", null);
	 * 
	 * }
	 * 
	 * 
	 * @Override protected void onPostExecute(ArrayList<BookItemEntityObject>
	 * result) { // TODO Auto-generated method stub dialog.dismiss(); totalpage
	 * = (result.size() / 9) + 1;
	 * 
	 * int lastcon = (pagenum * 9); if (lastcon > result.size()) { lastcon =
	 * result.size();
	 * 
	 * } if (result != null && result.size() > 0) {
	 * 
	 * for (int i = (pagenum - 1) * 9; i <= lastcon - 1; i++) { for (int i = 0;
	 * i < result.size(); i++) { listen_book_item_data d = new
	 * listen_book_item_data(); d.imageId = R.drawable.kbgseolhyun; d.bookid =
	 * result.get(i).book_id; d.title = result.get(i).book_title; d.LB_Img =
	 * result.get(i).bookimg;
	 * 
	 * mAdapter.add(d);
	 * 
	 * } } else {
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 * }
	 */
	/*
	 * class VOT_listenlist extends AsyncTask<String, Void,
	 * ArrayList<BookItemEntityObject>> {
	 * 
	 * StringBuilder SB = new StringBuilder();
	 * 
	 * @Override protected void onPreExecute() { // TODO Auto-generated method
	 * stub super.onPreExecute(); SB.append("?page=0");
	 * 
	 * }
	 * 
	 * @Override protected ArrayList<BookItemEntityObject>
	 * doInBackground(String... params) { // TODO Auto-generated method stub
	 * return VOTHelperHandler.BookList(NetworkDefineConstant.
	 * SERVER_URL_VOT_LISTEN_LIST, "GET", SB); }
	 * 
	 * }
	 */
}

/*
 * private void initData() { // TODO Auto-generated method stub
 * 
 * listen_book_item_data d = new listen_book_item_data();
 * 
 * d.imageId = R.drawable.ic_launcher; d.title = "안드로이드";
 * 
 * mAdapter.add(d);
 * 
 * d.imageId = R.drawable.ic_launcher; d.title = "안드로이드";
 * 
 * mAdapter.add(d);
 * 
 * d.imageId = R.drawable.ic_launcher; d.title = "안드로이드";
 * 
 * mAdapter.add(d);
 * 
 * }
 */
