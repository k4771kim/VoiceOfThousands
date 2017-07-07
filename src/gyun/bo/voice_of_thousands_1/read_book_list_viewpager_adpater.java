package gyun.bo.voice_of_thousands_1;

import java.util.ArrayList;
import java.util.Vector;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Toast;

public class read_book_list_viewpager_adpater extends FragmentStatePagerAdapter {
	Bundle b;

	public read_book_list_viewpager_adpater(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		Fragment f = new read_book_list_fragment(); // 여기에 인터넷 연결 - 리스트 구현하면
		b = new Bundle(); // 될듯함.

		b.putInt("position", position);

		f.setArguments(b);
		return f;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}
	
	

}