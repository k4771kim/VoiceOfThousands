package gyun.bo.voice_of_thousands_1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class VOT_CreditPage extends Fragment {

	Button credit1, credit2;
	TextView credittext1, credittext2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View v = inflater.inflate(R.layout.credit, container, false);
		Voice_Of_Thousands.sm.setSlidingEnabled(false);
		Voice_Of_Thousands.sm.toggle();
		credit1 = (Button) v.findViewById(R.id.credit_btn1);
		credit2 = (Button) v.findViewById(R.id.credit_btn2);

		credittext1 = (TextView) v.findViewById(R.id.credittext1);
		credittext2 = (TextView) v.findViewById(R.id.credittext2);
		credittext1.setVisibility(View.VISIBLE);
		credittext2.setVisibility(View.INVISIBLE);
		credit2.setAlpha(0.3f);
		credit1.setAlpha(1f);
		credit1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				credittext1.setVisibility(View.VISIBLE);
				credittext2.setVisibility(View.INVISIBLE);
				credit2.setAlpha(0.3f);
				credit1.setAlpha(1f);
			}
		});
		credit2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				credittext2.setVisibility(View.VISIBLE);
				credittext1.setVisibility(View.INVISIBLE);
				credit1.setAlpha(0.3f);
				credit2.setAlpha(1f);
			}
		});
		return v;

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Voice_Of_Thousands.sm.setSlidingEnabled(true);
	}

}
