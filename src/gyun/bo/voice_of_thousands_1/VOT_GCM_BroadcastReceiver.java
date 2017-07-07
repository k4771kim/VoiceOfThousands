/*
 * ��� ���� �ܸ����� CPU�� ����� ����Ʈ�� IntentService�� ������
 * ��ü�� �ѱ�� BR
 */
package gyun.bo.voice_of_thousands_1;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class VOT_GCM_BroadcastReceiver extends WakefulBroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intentFromGCMServer) {
		/*
		 * ��� ID �߱��� ����ϴ� BR�� ����� �� ���� String actionName =
		 * intentFromGCMServer.getAction();
		 * if(actionName.equals("com.google.android.c2dm.intent.REGISTRATION")){
		 * String registrationMessageFromGCM =
		 * gcmController.getMessageType(intentFromGCMServer); buf = new
		 * StringBuilder(); Bundle bundle = intentFromGCMServer.getExtras();
		 * Set<String> bundleKeys = bundle.keySet(); Iterator<String> keyValues
		 * = bundleKeys.iterator(); while(keyValues.hasNext()){ String key =
		 * keyValues.next(); buf.append(key + ":" + bundle.getString(key) +
		 * "\n"); } Log.i(DEBUG_TAG, " REGISTRATION Message " + buf.toString());
		 * Log.i(DEBUG_TAG, " REGISTRATION getMessageType(intent) =>" +
		 * registrationMessageFromGCM); }
		 */
		SharedPreferences prefs = context.getSharedPreferences(VOT_GCM_DefineConstant.PREFERENCES_FILE_NAME,
				Context.MODE_PRIVATE);

		if (prefs.getString("push", "on").equalsIgnoreCase("on")) {
			ComponentName targetCompName = new ComponentName(context.getPackageName(),
					VOT_GCM_IntentService.class.getName());
			// Intent gcmIntent =
			// intentFromGCMServer.setComponent(targetCompName);
		//	Log.e("SLEEP", "wake=" + SystemClock.elapsedRealtime());
			startWakefulService(context, intentFromGCMServer.setComponent(targetCompName));
		}
	}
}