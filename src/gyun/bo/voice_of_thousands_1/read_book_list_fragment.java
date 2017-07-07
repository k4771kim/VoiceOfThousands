package gyun.bo.voice_of_thousands_1;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class read_book_list_fragment extends Fragment {
	public static ListView listview;
	public MyAdapter mAdapter;
	public static Bundle bundle;
	public String genre;
	public static DisplayImageOptions options;
	ImageLoaderConfiguration ILC;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.readbook_listview, container, false);
		listview = (ListView) v.findViewById(R.id.kbglistview);
		
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.book_loading)
				.showImageForEmptyUri(R.drawable.ic_headset_white_24dp).showImageOnFail(R.drawable.name)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();
		ILC = new ImageLoaderConfiguration.Builder(getActivity()).build();
		
		ImageLoader.getInstance().init(ILC);
		mAdapter = new MyAdapter(inflater.getContext());
		bundle = getArguments();
		if (bundle.getInt("position") == 0) {

			genre = "";
		} else if (bundle.getInt("position") == 1) {

			genre = "?genre=essay";
		} else if (bundle.getInt("position") == 2) {

			genre = "?genre=poem";
		}

		listview.setAdapter(mAdapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				/*
				 * Toast.makeText(getActivity(),
				 * mAdapter.items.get(position).title.toString(),
				 * Toast.LENGTH_SHORT).show();
				 */	bundle = new Bundle();

				bundle.putInt("position", position);
				bundle.putInt("bookid", mAdapter.items.get(position).bookid);
				bundle.putString("title", mAdapter.items.get(position).title.toString());
				bundle.putString("part", mAdapter.items.get(position).part_per + "");
				((Voice_Of_Thousands) getActivity()).FragmentChange(2, bundle);

				// 아이템 클릭하면 출력

			}
		});

		new VOT_Async().execute(NetworkDefineConstant.SERVER_URL_VOT_READ_LIST, "GET", null);

		setRetainInstance(true);
		return v;
	}

	class VOT_Async extends AsyncTask<String, Void, ArrayList<BookItemEntityObject>> {
		ProgressDialog dialog;
		StringBuilder SB = new StringBuilder();

		protected void onPreExecute() {
			try {
				dialog = ProgressDialog.show(getActivity(), "", "서버에서 책 정보를 불러오고 있습니다. 잠시만 기다려 주세요.", true);

			} catch (Exception e) {
				e.printStackTrace();
			}
			SB.append(genre);
			super.onPreExecute();
		}

		@Override
		protected ArrayList<BookItemEntityObject> doInBackground(String... params) {

			return VOTHelperHandler.BookList(params[0], params[1], SB);

		}

		@Override
		protected void onPostExecute(ArrayList<BookItemEntityObject> result) {
			// TODO Auto-generated method stub
			dialog.dismiss();

			if (result == null) {

				Toast.makeText(getActivity(), "서버에서 정보를 받아올 수 없습니다.", Toast.LENGTH_SHORT).show();
			} else if (result != null && result.size() > 0) {

				for (int i = 0; i < result.size(); i++) {
					bookitemdata d = new bookitemdata();
					/* d.imageId = R.drawable.ic_launcher; */
					d.bookid = result.get(i).book_id;
					d.genre = result.get(i).book_genre;
					d.title = result.get(i).book_title;
					d.publisher = result.get(i).book_publisher;
					/* d.imageId= result.get(i).book_image; */
					d.author = result.get(i).book_writer;
					d.part_current = result.get(i).current_cnum;
					d.part_total = result.get(i).total_cnum;
					d.part_per = (int) (((double) d.part_current / (double) d.part_total) * 100);
					d.book_img_url = result.get(i).book_img_url;
					
					//d.bookimg = result.get(i).bookimg;
					d.book_Synop = result.get(i).book_synopsis;
					
					mAdapter.add(d);

				}
			} else {

			}

		}

	}

}
