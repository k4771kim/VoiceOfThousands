package gyun.bo.voice_of_thousands_1;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;s
import android.widget.BaseAdapter;

public class MyAdapter extends BaseAdapter {

	ArrayList<bookitemdata> items = new ArrayList<bookitemdata>();

	Context mContext;

	public MyAdapter(Context context) {
		// TODO Auto-generated constructor stub

		mContext = context;
	}
	public void add(bookitemdata item) {
		items.add(item);
		notifyDataSetChanged();
	}

	public void addAll(ArrayList<bookitemdata> items) {
		this.items.addAll(items);
		notifyDataSetChanged();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}
s
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		bookitem v = new bookitem(mContext);
		v.setItemData(items.get(position));

		return v;
	}

}
