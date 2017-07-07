package gyun.bo.voice_of_thousands_1;

import java.io.IOException;
import java.lang.reflect.Field;

import com.facebook.FacebookSdk;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;

public class VOTApplication extends Application {
	private static Context mContext;
	public static SharedPreferences prefs;
	public static SharedPreferences.Editor prefsEditor;
	private String gcmRegistrationID = "";
	private GoogleCloudMessaging gcmMessaging;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		FacebookSdk.sdkInitialize(this);

		mContext = this;
		prefs = getSharedPreferences(VOT_GCM_DefineConstant.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
//		prefsEditor = prefs.edit();
//		 prefsEditor.putString("recent_login_way", "google");
//		 prefsEditor.putString("google_Token", "ccc@ccc.com");

//		 prefsEditor.commit(); //�����۷��� �ʱ�ȭ�ϱ�.
		String result_pref = prefs.getString("GCM", "NULL");
		if (result_pref.equalsIgnoreCase("NULL")) {
		//	Log.e("����� �����۷����� ����.", result_pref.toString());
			new GCM_Async().execute();

		} else {
		//	Log.e("�̹� ��ϵ� �����۷����� ����", result_pref.toString());
		}
		//Log.e("	��ϵ� Ǫ�� Ű����", result_pref.toString());

	
		/*
		 * setDefaultFont(this, "SANS_SERIF", "SEOULHANGANGB.TTF");
		 * setDefaultFont(this, "SERIF", "SEOULHANGANGB.TTF");
		 * setDefaultFont(this, "DEFAULT", "SEOULHANGANGB.TTF");
		 */
		mContext = this;

	}

	public static Context getContext() {

		return mContext;

	}

	class GCM_Async extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				if (gcmMessaging == null) {
					gcmMessaging = GoogleCloudMessaging.getInstance(getApplicationContext());
				}
				/*
				 * GCM ������ ���� ���ø����̼��� GCM�� ���� �� �ֵ��� ��� ID�� ��û�Ѵ�.���������� ��Ʈ��ũ �ڵ尡
				 * ����ǹǷ� �ݵ�� Back Ground Thread������ �����Ͽ��� �Ѵ�.
				 */
				// gcmRegistrationID = gcmMessaging.register("559044369485");
				// 358013194665
				/////////////////////////////////////////////////////////
				gcmMessaging = GoogleCloudMessaging.getInstance(getApplicationContext());
				gcmRegistrationID = gcmMessaging.register("101710000987");
				//Log.e("�߱޹��� ��� ID �� =>", gcmRegistrationID);

				gcmMessaging.close();
			} catch (IOException ioe) {

			}
			return gcmRegistrationID;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub

			SharedPreferences prefs = getSharedPreferences(VOT_GCM_DefineConstant.PREFERENCES_FILE_NAME,
					Context.MODE_PRIVATE);
			SharedPreferences.Editor prefsEditor = prefs.edit();
			prefsEditor.putString("GCM", gcmRegistrationID);
			prefsEditor.commit();
		//	Log.e("�����۷��� �����.", gcmRegistrationID.toString());

			super.onPostExecute(result);
		}

	}

	public static void setDefaultFont(Context ctx, String staticTypefaceFieldName, String fontAssetName) {
		final Typeface regular = Typeface.createFromAsset(ctx.getAssets(), fontAssetName);
		replaceFont(staticTypefaceFieldName, regular);
	}

	protected static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
		try {
			final Field StaticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
			StaticField.setAccessible(true);
			StaticField.set(null, newTypeface);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
