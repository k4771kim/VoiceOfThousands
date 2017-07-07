package gyun.bo.voice_of_thousands_1;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class mypage_adapter extends FragmentStatePagerAdapter {
	public static int total_page = 0;
	private ArrayList<Integer> mArr_Mission = new ArrayList<Integer>();
	private ArrayList<Fragment> mArr_Fragment = new ArrayList<Fragment>();

	public mypage_adapter(FragmentManager fm) {
			
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub

		Fragment f = new Mypage_List(); // 여기에 인터넷 연결 - 리스트 구현하면 될듯함.

		/* MyPage_Fragment = new MyPage_Fragment() */

		Bundle b = new Bundle();
		b.putInt("position", position);
		f.setArguments(b);
		/*Mypage_List.cp = position;*/
		return f;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub

		try {
			if (total_page == 0) {
				total_page = (new VOT_Async().execute().get().get(0).total_cnum);
			} else {
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MyPage_Fragment.total_page = total_page;
		return total_page;
	}

	@Override
	public int getItemPosition(Object object) {
		/*
		 * // TODO Auto-generated method stub return
		 * super.getItemPosition(object);
		 */

		return POSITION_NONE;
	}

	class VOT_Async extends AsyncTask<String, Void, ArrayList<BookItemEntityObject>> {
		StringBuilder SB = new StringBuilder();

		protected void onPreExecute() {

			SB.append("?memberid=" + Voice_Of_Thousands.member_id).append("&page=" + "0"); 

			super.onPreExecute();
		}

		@Override
		protected ArrayList<BookItemEntityObject> doInBackground(String... params) {
			return VOTHelperHandler.BookList(NetworkDefineConstant.SERVER_URL_VOT_MEMBER_INFO, "GET", SB);

		}

		@Override
		protected void onPostExecute(ArrayList<BookItemEntityObject> result) {
			// TODO Auto-generated method stub

		}

	}
}
