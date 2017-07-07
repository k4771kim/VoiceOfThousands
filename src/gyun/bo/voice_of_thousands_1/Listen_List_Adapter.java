package gyun.bo.voice_of_thousands_1;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Toast;
import gyun.bo.voice_of_thousands_1.mypage_adapter.VOT_Async;

public class Listen_List_Adapter extends FragmentStatePagerAdapter {
	public static int total_page = 0;
	public Listen_List_Adapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		Fragment f = new listen_book_item(); // 여기에 인터넷 연결 - 리스트 구현하면 될듯함.

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
				total_page = (new VOT_listenlist().execute().get().get(0).total_cnum);
			} else {
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Listenlist_Fragment.totalpage = total_page;
		return total_page;
	}

	class VOT_listenlist extends AsyncTask<String, Void, ArrayList<BookItemEntityObject>> {

		StringBuilder SB = new StringBuilder();

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			SB.append("?page=0");

		}

		@Override
		protected ArrayList<BookItemEntityObject> doInBackground(String... params) {
			// TODO Auto-generated method stub
			return VOTHelperHandler.BookList(NetworkDefineConstant.SERVER_URL_VOT_LISTEN_LIST, "GET", SB);
		}

		
		@Override
		protected void onPostExecute(ArrayList<BookItemEntityObject> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (!result.get(0).result.equalsIgnoreCase("success")){
				
				Toast.makeText(VOT_Listen_Activity.getContext(),"네트워크 상황이 불안정합니다. 잠시 후에 시도해 주세요.", Toast.LENGTH_SHORT).show();
				
				
			}
		}



	}
	

}
