package gyun.bo.voice_of_thousands_1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {

	public String S ="";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		String status = NetworkUtil.getConnectivityStatusString(context);

		if (!S.equalsIgnoreCase(status)) {
	//		Toast.makeText(context, status, Toast.LENGTH_LONG).show();
			S = status;
		}
	}

}
