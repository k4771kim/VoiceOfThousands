package gyun.bo.voice_of_thousands_1;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class listen_book_item extends Fragment {
	TextView first_title_txt, second_title_txt, third_title_txt, forth_title_txt;
	ImageView first_listen_img, second_listen_img, third_listen_img, forth_listen_img;
	LinearLayout first_book, second_book, third_book, forth_book;
	public static int current_page = 0, current_position;
	public static listen_book_item_data[][] d = new listen_book_item_data[Listen_List_Adapter.total_page][4];
	public static DisplayImageOptions options;
	public ImageLoaderConfiguration ILC;
	public static boolean f = true, s, t, fo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.listen_item, container, false);
		clickbutton clk = new clickbutton();

		ImageLoader.getInstance().clearMemoryCache();
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.book_loading)
				.showImageForEmptyUri(R.drawable.ic_headset_white_24dp).showImageOnFail(R.drawable.name)
				.cacheInMemory(false).cacheOnDisk(false).considerExifParams(false)
				.displayer(new RoundedBitmapDisplayer(20)).build();
		ILC = new ImageLoaderConfiguration.Builder(getActivity()).discCacheExtraOptions(300, 300, null).build();

		ImageLoader.getInstance().init(ILC);

		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "SEOULHANGANGB.TTF");

		first_title_txt = (TextView) v.findViewById(R.id.first_listen_title);
		second_title_txt = (TextView) v.findViewById(R.id.second_listen_title);
		third_title_txt = (TextView) v.findViewById(R.id.third_listen_title);
		forth_title_txt = (TextView) v.findViewById(R.id.forth_listen_title);
		first_title_txt.setTypeface(tf);
		second_title_txt.setTypeface(tf);
		third_title_txt.setTypeface(tf);
		forth_title_txt.setTypeface(tf);

		first_listen_img = (ImageView) v.findViewById(R.id.first_listen_Img);
		second_listen_img = (ImageView) v.findViewById(R.id.second_listen_Img);
		third_listen_img = (ImageView) v.findViewById(R.id.third_listen_Img);
		forth_listen_img = (ImageView) v.findViewById(R.id.forth_listen_Img);
		first_book = (LinearLayout) v.findViewById(R.id.first_listen_Book);
		second_book = (LinearLayout) v.findViewById(R.id.Second_listen_Book);
		third_book = (LinearLayout) v.findViewById(R.id.Third_listen_Book);
		forth_book = (LinearLayout) v.findViewById(R.id.Forth_listen_Book);

		first_book.setOnClickListener(clk);
		second_book.setOnClickListener(clk);
		third_book.setOnClickListener(clk);
		forth_book.setOnClickListener(clk);
		first_book.setClickable(false);
		second_book.setClickable(false);
		third_book.setClickable(false);
		forth_book.setClickable(false);
		current_page = MyPage_Fragment.current_page;

		Bundle b = new Bundle();
		b = getArguments();
		current_position = b.getInt("position");
	//	Log.e("커런트 ", current_position + "");
		new VOT_listenlist().execute(current_position + "");

		return v;
	}

	class clickbutton implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Bundle bundle = new Bundle();
			if (f) {
				f = false;
				if (v == first_book) {

					bundle.putInt("bookid", d[current_page][0].bookid);
					// Listen_Mode로 보낼 번들. ((VOT_Listen_Activity)
					((VOT_Listen_Activity) getActivity()).FragmentChange(0, bundle);

				} else if (v == second_book) {
					bundle.putInt("bookid", d[current_page][1].bookid);
					// Listen_Mode로 보낼 번들. ((VOT_Listen_Activity)
					((VOT_Listen_Activity) getActivity()).FragmentChange(0, bundle);

				} else if (v == third_book) {
					bundle.putInt("bookid", d[current_page][2].bookid);
					// Listen_Mode로 보낼 번들. ((VOT_Listen_Activity)
					((VOT_Listen_Activity) getActivity()).FragmentChange(0, bundle);

				} else if (v == forth_book) {
					bundle.putInt("bookid", d[current_page][3].bookid);
					// Listen_Mode로 보낼 번들. ((VOT_Listen_Activity)
					((VOT_Listen_Activity) getActivity()).FragmentChange(0, bundle);

				}
				
			}
		}
	}

	class VOT_listenlist extends AsyncTask<String, Void, ArrayList<BookItemEntityObject>> {

		StringBuilder SB = new StringBuilder();
		int cu;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected ArrayList<BookItemEntityObject> doInBackground(String... params) {
			// TODO Auto-generated method stub
			SB.append("?page=" + params[0]);
			cu = Integer.parseInt(params[0]);
			return VOTHelperHandler.BookList(NetworkDefineConstant.SERVER_URL_VOT_LISTEN_LIST, "GET", SB);
		}

		@Override
		protected void onPostExecute(ArrayList<BookItemEntityObject> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result != null && result.size() > 0) {

				if (result.get(0).result.equalsIgnoreCase("success")) {

					for (int i = 0; i < result.size(); i++) {
						listen_book_item_data da = new listen_book_item_data();

						da.bookid = result.get(i).book_id;
						da.title = result.get(i).book_title;
						da.ImgUrl = result.get(i).book_image;

						d[cu][i] = da;
					}
					switch (result.size()) {

					case 4:
						forth_book.setClickable(true);
						ImageLoader.getInstance().displayImage(d[cu][3].ImgUrl, forth_listen_img, options);
						forth_title_txt.setText(d[cu][3].title);

					//	Log.e("d [cu][3]", cu + ",3 생성함.");
					case 3:
						third_book.setClickable(true);
						ImageLoader.getInstance().displayImage(d[cu][2].ImgUrl, third_listen_img, options);
						third_title_txt.setText(d[cu][2].title);

					//	Log.e("d [cu][2]", cu + ",2 생성함.");
					case 2:
						second_book.setClickable(true);
						ImageLoader.getInstance().displayImage(d[cu][1].ImgUrl, second_listen_img, options);
						second_title_txt.setText(d[cu][1].title);

				//		Log.e("d [cu][1]", cu + ",1 생성함.");
					case 1:
						first_book.setClickable(true);
						ImageLoader.getInstance().displayImage(d[cu][0].ImgUrl, first_listen_img, options);
						first_title_txt.setText(d[cu][0].title);

						//Log.e("d [cu][0]", cu + ",0 생성함.");

					default:
					//	Log.e("여기까지 --- d [cu][]", cu + "생성함. -----");
					}

				}

			}

		}

	}
	/*
	 * @Override public void onResume() { // TODO Auto-generated method stub
	 * super.onResume(); if (f) { first_book.setClickable(true);
	 * 
	 * } if (s) { second_book.setClickable(true);
	 * 
	 * } if (t) { third_book.setClickable(true); } if (fo) {
	 * 
	 * forth_book.setClickable(true);
	 * 
	 * } }
	 */
}
