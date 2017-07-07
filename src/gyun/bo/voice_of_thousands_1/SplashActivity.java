package gyun.bo.voice_of_thousands_1;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends Activity {

	private static int SFLASH_DELAY = 2000; // ms
	SharedPreferences prefs;
	String token;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_splash);
		prefs = getSharedPreferences(VOT_GCM_DefineConstant.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
		//		Log.e("À¯À¯", prefs.getString("recent_login_way", "NULL").toString());
				if (prefs.getString("recent_login_way", "NULL").equalsIgnoreCase("NULL")) {

					startActivity(new Intent(SplashActivity.this, VOT_Login_Page.class));

				} else if (prefs.getString("recent_login_way", "NULL").equalsIgnoreCase("google")) {
					token = prefs.getString("google_Token", "NULL");

					Intent intent = new Intent(new Intent(SplashActivity.this, Login_Auth_Activity.class));
					intent.putExtra("login_way", "G");
					intent.putExtra("login_token", token);
					startActivity(intent);
				} else if (prefs.getString("recent_login_way", "NULL").equalsIgnoreCase("facebook")) {
					token = prefs.getString("facebook_Token", "NULL");
				//	Toast.makeText(getApplication(), token, Toast.LENGTH_SHORT).show();

					Intent intent = new Intent(new Intent(SplashActivity.this, Login_Auth_Activity.class));
					intent.putExtra("login_way", "F");
					intent.putExtra("login_token", token);
					startActivity(intent);

				}

				finish();
				/*
				 * overridePendingTransition(android.R.anim.fade_in,
				 * android.R.anim.fade_out);
				 */
			}
		}, SFLASH_DELAY);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			File file = new File(
					Environment.getExternalStorageDirectory().getAbsolutePath() + "/VOT/");
			if (!file.exists()) {
				file.mkdirs();
			}
		} else {
			Intent intent = new Intent(Settings.ACTION_MEMORY_CARD_SETTINGS);
			startActivity(intent);
		}
	};

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.abc_fade_in, android.R.anim.fade_out);
	}
}
