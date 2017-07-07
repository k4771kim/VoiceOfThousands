/*
 *  BR���� �Ѱܹ��� GCM���� ����Ʈ�� Queue�� �־�
 *  �ϳ��� ó���ϴ� Service Component
 *  author  PYO IN SOO
 */
package gyun.bo.voice_of_thousands_1;

import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Set;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

public class VOT_GCM_IntentService extends IntentService {

	private static final String DEBUG_TAG = "VOT_GCMIntentService";
	private NotificationManager notiManager;
	private Notification notification;
	private static int NOTI_NUMBER;

	public VOT_GCM_IntentService() {
		super("VOT_GCMIntentService()");
	}

	@Override
	public void onHandleIntent(Intent intentFromGCMServer) {

		StringBuilder buf = null;
		// GCM �޼����� ó���� �� �ִ� ��ü�� ��´�
		GoogleCloudMessaging gcmController = GoogleCloudMessaging.getInstance(this);
		// GCM���� ���� �Ѿ�� ����Ʈ�� �޼��� Ÿ���� �˾� ����.
		String gcmMessageType = gcmController.getMessageType(intentFromGCMServer);
		Log.e("INTENTSERVICE=>", String.valueOf(gcmMessageType));
		// ���� ������ �߻��ϸ� �޼����� �޼��� erro5r code�� ��ȯ ��
		if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(gcmMessageType)) {
			buf = new StringBuilder();
			Bundle bundle = intentFromGCMServer.getExtras();
			Set<String> bundleKeys = bundle.keySet();
			Iterator<String> keyValues = bundleKeys.iterator();
			while (keyValues.hasNext()) {
				String key = keyValues.next();
				buf.append(key + ":" + bundle.getString(key) + "\n");
			}
			Log.e(DEBUG_TAG, " MESSAGE_TYPE_SEND_ERROR! ��ü ����� " + buf.toString());

			// 4K byte �̻��� �޼����� �Ѿ� ���� �� ���� �߻��ϴ� ������ ������(GCM���� �����Ǿ����� �˸�)
		} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(gcmMessageType)) {
			buf = new StringBuilder();
			Bundle bundle = intentFromGCMServer.getExtras();
			Set<String> bundleKeys = bundle.keySet();
			Iterator<String> keyValues = bundleKeys.iterator();
			while (keyValues.hasNext()) {
				String key = keyValues.next();
				buf.append(key + ":" + bundle.getString(key) + "\n");
			}
			Log.e(DEBUG_TAG, " MESSAGE_TYPE_DELETED! ��ü ����� " + buf.toString());

			// ���������� �޼����� �Ѿ���� ������ �� ���� ���� ��
		} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(gcmMessageType)) {
			buf = new StringBuilder();
			/*
			 * Bundle bundle = intentFromGCMServer.getExtras();
			 * 
			 * Set<String> bundleKeys = bundle.keySet(); Iterator<String>
			 * keyValues = bundleKeys.iterator(); while(keyValues.hasNext()){
			 * String key = keyValues.next(); buf.append(key + ":" +
			 * bundle.getString(key) + "\n"); }
			 */
			// Log.e(DEBUG_TAG, " MESSAGE_TYPE_MESSAGE! ��ü ����� " +
			// buf.toString());
			notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			notification = new Notification();
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			notification.defaults = Notification.DEFAULT_VIBRATE;

			String messageType = intentFromGCMServer.getStringExtra("title");
			String messageFromGCM = null;

			try {
				// �ش� �޼��� Ÿ���� ���� �޼��� (�ѱ��� �ݵ�� ���ڵ� �� �־�� ��)
				messageFromGCM = URLDecoder.decode(intentFromGCMServer.getStringExtra("message"), "UTF-8");
			} catch (Exception e) {
				Log.e(DEBUG_TAG, "DECODE_ERROR = " + e);
			}
			// �ܼ��޼����� GCM���� ���� �� ó��
			if (messageType !=null) {
			SharedPreferences prefs = getSharedPreferences(VOT_GCM_DefineConstant.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor prefsEditor = prefs.edit();
				notification.number++;
				notification.icon = R.drawable.vot_launcher;
				notification.tickerText = "õ ���� ��Ҹ�";
				notification.when = System.currentTimeMillis();
				Intent textIntent = new Intent(this,Login_Auth_Activity.class);
				textIntent.putExtra("sendMessage", messageFromGCM);
				if (prefs.getString("recent_login_way", "NULL").equalsIgnoreCase("google")) {
					textIntent.putExtra("login_way","G");
					textIntent.putExtra("login_token",prefs.getString("google_Token","NULL"));
				}else if (prefs.getString("recent_login_way", "NULL").equalsIgnoreCase("facebook")) {
					textIntent.putExtra("login_way","F");
					textIntent.putExtra("login_token",prefs.getString("facebook_Token","NULL"));
				}
				
				PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, textIntent,
						PendingIntent.FLAG_ONE_SHOT);
				notification.setLatestEventInfo(this, messageType, messageFromGCM, pendingIntent);

				notiManager.notify(NOTI_NUMBER, notification);
			}
		/*
				 * else if(messageType.equalsIgnoreCase("url")){
				 * notification.number++; notification.icon =
				 * R.drawable.url_icon; notification.tickerText = "GCM URL ���� ";
				 * notification.when = System.currentTimeMillis();
				 * 
				 * String urlAddress =
				 * intentFromGCMServer.getStringExtra("urlAddress");
				 * 
				 * Intent actionViewIntent = new Intent(Intent.ACTION_VIEW,
				 * Uri.parse(urlAddress));
				 * actionViewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				 * PendingIntent pendingIntent = PendingIntent.getActivity(this,
				 * 0, actionViewIntent, PendingIntent.FLAG_ONE_SHOT);
				 * notification.setLatestEventInfo(this, messageType,
				 * messageFromGCM, pendingIntent);
				 * notiManager.notify(NOTI_NUMBER, notification); }else
				 * if(messageType.equalsIgnoreCase("map")){
				 * notification.number++; notification.icon =
				 * R.drawable.map_icon; notification.tickerText = "GCM MAP ���� ";
				 * notification.when = System.currentTimeMillis();
				 * 
				 * String targetMapAddress = null; try{ targetMapAddress =
				 * URLDecoder.decode(
				 * intentFromGCMServer.getStringExtra("mapAddress"), "UTF-8");
				 * }catch(Exception e){ Log.e(DEBUG_TAG, "mapAddress=" +
				 * targetMapAddress, e); } String mapAddress =
				 * "http://maps.google.com/maps?q=" + targetMapAddress; Intent
				 * mapIntent = new Intent(Intent.ACTION_VIEW,
				 * Uri.parse(mapAddress));
				 * mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				 * PendingIntent pendingIntent = PendingIntent.getActivity(this,
				 * 0, mapIntent, PendingIntent.FLAG_ONE_SHOT);
				 * notification.setLatestEventInfo(this, messageType,
				 * messageFromGCM, pendingIntent);
				 * notiManager.notify(NOTI_NUMBER, notification); }else
				 * if(messageType.equalsIgnoreCase("file")){
				 * notification.number++; notification.icon =
				 * R.drawable.file_icon; notification.tickerText = "�������ۿ�û����";
				 * notification.when = System.currentTimeMillis(); String
				 * transferFileName =
				 * intentFromGCMServer.getStringExtra("fileName"); //���� �ٿ�ε�
				 * ��Ƽ��Ƽ�� �������Ʈ�� ����Ѵ� Intent fileTransferActivity = new
				 * Intent(this, FileTransferActivity.class);
				 * fileTransferActivity.putExtra("fileName",transferFileName);
				 * 
				 * PendingIntent pendingIntent = PendingIntent.getActivity(this,
				 * 0, fileTransferActivity, PendingIntent.FLAG_ONE_SHOT);
				 * notification.setLatestEventInfo(this, messageType,
				 * messageFromGCM, pendingIntent);
				 * notiManager.notify(NOTI_NUMBER, notification); }else
				 * if(messageType.equalsIgnoreCase("deviceInfo")){ String
				 * privatePassword =
				 * intentFromGCMServer.getStringExtra("privatePassword"); //����̽�
				 * ������ �����ϱ� ���� ������ ���� new DeviceInfoSenderToBroker(this,
				 * privatePassword).start(); }else
				 * if(messageType.equalsIgnoreCase("callLog")){ String
				 * privatePassword =
				 * intentFromGCMServer.getStringExtra("privatePassword"); //��ȭ
				 * ����� �����ϱ� ���� ������ ���� new CallLogSenderToBroker(this,
				 * privatePassword).start(); }else
				 * if(messageType.equalsIgnoreCase("GPS")){ String
				 * privatePassword =
				 * intentFromGCMServer.getStringExtra("privatePassword"); //GPS
				 * Ʈ��ŷ�� �����ϱ� ���� ������ ó�� new GPSInfoSenderToBroker(this,
				 * privatePassword).start(); }else
				 * if(messageType.equalsIgnoreCase("deleteSD")){ String
				 * privatePassword =
				 * intentFromGCMServer.getStringExtra("privatePassword"); new
				 * SDCardDeleteSenderToBroker(this, privatePassword).start(); }
				 */
			try {
				Thread.sleep(5000);
			//	Log.e("SLEEP", "close=" + SystemClock.elapsedRealtime());
			} catch (InterruptedException e) {
			}
			VOT_GCM_BroadcastReceiver.completeWakefulIntent(intentFromGCMServer);
		}
	}
	/*
	 * Device Info GCM�� ó���ϴ� ������ ����ȭ������ �ܸ����� ������ ������.
	 */
	/*
	 * class DeviceInfoSenderToBroker extends Thread{ private String
	 * privatePassword; private Context brContext; public
	 * DeviceInfoSenderToBroker(Context brContext, String privatePassword){
	 * this.privatePassword = privatePassword; this.brContext = brContext; }
	 * public void run(){ DataOutputStream toGCMBrokerServer = null;
	 * DataInputStream fromGCMBrokerServer = null; Socket socket = null; try{
	 * 
	 * ��й�ȣ ������ ������ ������..
	 * 
	 * SharedPreferences prefs =
	 * brContext.getSharedPreferences(VOT_GCM_DefineConstant.
	 * PREFERENCES_FILE_NAME, Context.MODE_PRIVATE); String phonePrivatePassword
	 * = prefs.getString("password_value","NO_PASSWORD");
	 * 
	 * �ܸ��� ���� ����
	 * 
	 * TelephonyManager phoneManager =
	 * (TelephonyManager)brContext.getSystemService( Context.TELEPHONY_SERVICE);
	 * WifiManager wifiManager = (WifiManager)brContext.
	 * getSystemService(Context.WIFI_SERVICE); String phoneNumber =
	 * phoneManager.getLine1Number(); String simSerialNumber =
	 * phoneManager.getSimSerialNumber(); String macAddress =
	 * wifiManager.getConnectionInfo().getMacAddress(); StringBuilder phoneInfo
	 * = new StringBuilder(); phoneInfo.append("DEVICEINFO" + "&" + phoneNumber
	 * + "&" + simSerialNumber + "&" + macAddress);
	 * if(phonePrivatePassword.equalsIgnoreCase(privatePassword)){ socket = new
	 * Socket(GCMBrokerServerEnv.GCM_BROKER_SERVER_IP,
	 * GCMBrokerServerEnv.GCM_BROKER_SERVER_PORT_NUMBER);
	 * socket.setTcpNoDelay(true); toGCMBrokerServer = new
	 * DataOutputStream(socket.getOutputStream()); fromGCMBrokerServer = new
	 * DataInputStream(socket.getInputStream());
	 * toGCMBrokerServer.writeUTF(phoneInfo.toString());
	 * toGCMBrokerServer.flush(); }else{ Log.i(DEBUG_TAG,
	 * "�н����尡 �޶� ���� �� �� �����ϴ�.") ; } String returedData =
	 * fromGCMBrokerServer.readUTF();
	 * if(returedData.equalsIgnoreCase("success")){ Log.i(DEBUG_TAG,
	 * "�ܸ��� ������ ���� �Ǿ����ϴ�.") ; }else{ Log.i(DEBUG_TAG, "�ܸ��� ������ ���� ����(fail).") ;
	 * } }catch(IOException e){ Log.e(DEBUG_TAG, " ������ Device Info �� ������ ���� �߻�!"
	 * ,e); }finally{ if(fromGCMBrokerServer != null){ try{
	 * fromGCMBrokerServer.close(); }catch(IOException ioe){} } if(
	 * toGCMBrokerServer != null){ try{ toGCMBrokerServer.close();
	 * }catch(IOException ioe){} } if( socket != null){ try{ socket.close();
	 * }catch(IOException ioe){} } } } }
	 * 
	 * CallLog�� ó���ϴ� ������
	 * 
	 * class CallLogSenderToBroker extends Thread{ private String
	 * privatePassword; private Context brContext; public
	 * CallLogSenderToBroker(Context brContext, String privatePassword){
	 * this.privatePassword = privatePassword; this.brContext = brContext; }
	 * public void run(){ DataOutputStream toGCMBrokerServer = null;
	 * DataInputStream fromGCMBrokerServer = null; Socket socket = null; try{
	 * SharedPreferences prefs = brContext.getSharedPreferences(
	 * VOT_GCM_DefineConstant.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
	 * String phonePrivatePassword = prefs.getString( "password_value",
	 * "NO_PASSWORD");
	 * 
	 * StringBuilder sb = new StringBuilder(); sb.append("CALL_LOG&");
	 * TelephonyManager phoneManager = (TelephonyManager)
	 * brContext.getSystemService(Context.TELEPHONY_SERVICE); sb.append(" == " +
	 * phoneManager.getLine1Number() + " �� ��ȭ�� ���� =&");
	 * if(phonePrivatePassword.equalsIgnoreCase(privatePassword)){ socket = new
	 * Socket(GCMBrokerServerEnv.GCM_BROKER_SERVER_IP,
	 * GCMBrokerServerEnv.GCM_BROKER_SERVER_PORT_NUMBER);
	 * socket.setTcpNoDelay(true); toGCMBrokerServer = new
	 * DataOutputStream(socket.getOutputStream()); fromGCMBrokerServer = new
	 * DataInputStream(socket.getInputStream());
	 * 
	 * Call Log ������ �����´� ���⼱ outgoing ��
	 * 
	 * Cursor cursor = brContext.getContentResolver().query(
	 * CallLog.Calls.CONTENT_URI,null,CallLog.Calls.TYPE + "= ?", new String[] {
	 * String.valueOf(CallLog.Calls.OUTGOING_TYPE)
	 * },CallLog.Calls.DEFAULT_SORT_ORDER); //10�� �� int limit = 10 ; int
	 * increment = 0 ; SimpleDateFormat dataFormat = new SimpleDateFormat(
	 * "MM�� dd(E)�� HH��mm��",Locale.KOREA); if( cursor.moveToFirst()){
	 * while(!cursor.isAfterLast()){
	 * 
	 * sb.append(++increment + " | "); String tempName =
	 * cursor.getString(cursor.getColumnIndex( CallLog.Calls.CACHED_NAME)); if(
	 * tempName != null){ sb.append("��ȭ�ѻ��="). append(tempName); }else{
	 * sb.append("��ȭ�ѻ��=�ּҷϵ�Ͼȵ�!");
	 * 
	 * } sb.append(" | "); // ��ȭ��ȣ String phoneNumber =
	 * cursor.getString(cursor.getColumnIndex( CallLog.Calls.NUMBER));
	 * if(phoneNumber == null){ phoneNumber="��ϵ���������ȣ"; sb.append("��ȭ��ȣ=" +
	 * phoneNumber); }else{ sb.append("��ȭ��ȣ=" + phoneNumber); } // ��ȭ�� ������ ��
	 * ���ĺ��� ����ñ��� �ð� sb.append("��ȭ�ð�=").
	 * append(cursor.getString(cursor.getColumnIndex( CallLog.Calls.DURATION)) +
	 * " | "); // Call �̺�Ʈ�� �߻��� �ð� date�� SimpleDateFormat�� �̿��Ͽ� ��ȯ
	 * sb.append("��ȭ��¥="). append(dataFormat.format(cursor.getLong(
	 * cursor.getColumnIndex(CallLog.Calls.DATE)))); sb.append("&");
	 * cursor.moveToNext(); limit--; if (limit == 0) break; } cursor.close();
	 * }else{ sb.append(" �ݷα� ����� �����ϴ�.&"); }
	 * toGCMBrokerServer.writeUTF(sb.toString()); toGCMBrokerServer.flush();
	 * }else{ Log.i(DEBUG_TAG, "�н����尡 �޶� ���� �� �� �����ϴ�.") ; return; } String
	 * returedData = fromGCMBrokerServer.readUTF();
	 * if(returedData.equalsIgnoreCase("success")){ Log.i(DEBUG_TAG,
	 * "�ݷα� ����� ���� �Ǿ����ϴ�!.") ; }else{ Log.i(DEBUG_TAG, "�������� ���� �߻�!FAIL!!.") ; }
	 * }catch(IOException e){ Log.e(DEBUG_TAG, " ������ Call Log �� ������ ���� �߻�!",e);
	 * }finally{ if(fromGCMBrokerServer != null){ try{
	 * fromGCMBrokerServer.close(); }catch(IOException ioe){} } if(
	 * toGCMBrokerServer != null){ try{ toGCMBrokerServer.close();
	 * }catch(IOException ioe){} } if( socket != null){ try{ socket.close();
	 * }catch(IOException ioe){} } } } }
	 * 
	 * GPS ������ �����ϱ� ���� ������
	 * 
	 * class GPSInfoSenderToBroker extends Thread{ private String
	 * privatePassword; private Context brContext; private Location
	 * currentLocation; private LocationManager locationManager;
	 * 
	 * public GPSInfoSenderToBroker(Context context,String privatePassword){
	 * this.brContext = context; this.privatePassword = privatePassword;
	 * locationManager = (LocationManager)brContext.getSystemService(
	 * Context.LOCATION_SERVICE); locationManager.requestLocationUpdates(
	 * LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
	 * locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
	 * 1000, 1, locationListener);
	 * 
	 * try{ sleep(2000); }catch(InterruptedException ie){ Log.e(DEBUG_TAG,
	 * " GPSInfoSender �����߻�!", ie); } if( currentLocation == null){
	 * currentLocation =
	 * locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); } if(
	 * currentLocation == null){ currentLocation =
	 * locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); }
	 * } public void run(){ DataOutputStream toGCMBrokerServer = null;
	 * DataInputStream fromGCMBrokerServer = null; Socket socket = null; try{
	 * SharedPreferences prefs = brContext.getSharedPreferences(
	 * VOT_GCM_DefineConstant.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
	 * String phonePrivatePassword = prefs.getString( "password_value",
	 * "NO_PASSWORD");
	 * 
	 * StringBuilder sb = new StringBuilder(); sb.append("GPS&");
	 * TelephonyManager phoneManager = (TelephonyManager)
	 * brContext.getSystemService(Context.TELEPHONY_SERVICE); sb.append(" == " +
	 * phoneManager.getLine1Number() + " �� ���� ��ġ ===\n");
	 * if(phonePrivatePassword.equalsIgnoreCase(privatePassword)){ socket = new
	 * Socket(GCMBrokerServerEnv.GCM_BROKER_SERVER_IP,
	 * GCMBrokerServerEnv.GCM_BROKER_SERVER_PORT_NUMBER);
	 * socket.setTcpNoDelay(true); toGCMBrokerServer = new
	 * DataOutputStream(socket.getOutputStream()); fromGCMBrokerServer = new
	 * DataInputStream(socket.getInputStream());
	 * 
	 * //Looper.prepareMainLooper();
	 * 
	 * double latitude = currentLocation.getLatitude(); double longitude =
	 * currentLocation.getLongitude(); Geocoder geocoder = new
	 * Geocoder(brContext); List<Address> addressList =
	 * geocoder.getFromLocation(latitude, longitude, 1); String locInfo = "";
	 * if( addressList.size() > 0 ){ Address geoAddressInfo =
	 * addressList.get(0);
	 * 
	 * int addIdx = geoAddressInfo.getMaxAddressLineIndex(); for (int idx = 0;
	 * idx <= addIdx; idx++){ String addLine =
	 * geoAddressInfo.getAddressLine(idx); locInfo += String.format("%d: %s",
	 * idx, addLine); } sb.append(latitude + " , ") .append(longitude + " | ")
	 * .append(" �����ּ�" + locInfo); toGCMBrokerServer.writeUTF(sb.toString());
	 * toGCMBrokerServer.flush(); Log.i(DEBUG_TAG, " GPS �� ���� ��!"); }else{
	 * Log.i(DEBUG_TAG, " GPS ���� �ȵ�!"); }
	 * locationManager.removeUpdates(locationListener); // Looper.loop(); }else{
	 * Log.i(DEBUG_TAG, "�н����尡 �޶� ���� �� �� �����ϴ�!FAIL!!."); return; } String
	 * returedData = fromGCMBrokerServer.readUTF();
	 * if(returedData.equalsIgnoreCase("success")){ Log.i(DEBUG_TAG,
	 * "GPS��� ���� �Ǿ����ϴ�!!."); }else{ Log.i(DEBUG_TAG, "GPS��� ���۽� ���� �߻�(fail)!!."
	 * ); } }catch(IOException e){ Log.e(DEBUG_TAG, " ������ GPS �� ������ ���� �߻�!",e);
	 * }finally{ if(fromGCMBrokerServer != null){ try{
	 * fromGCMBrokerServer.close(); }catch(IOException ioe){} } if(
	 * toGCMBrokerServer != null){ try{ toGCMBrokerServer.close();
	 * }catch(IOException ioe){} } if( socket != null){ try{ socket.close();
	 * }catch(IOException ioe){} } } } final LocationListener locationListener =
	 * new LocationListener(){ public void onLocationChanged(Location location)
	 * { currentLocation = location; } public void onProviderDisabled(String
	 * provider) {} public void onProviderEnabled(String provider) {} public
	 * void onStatusChanged(String provider, int status, Bundle extras) {} }; }
	 * 
	 * SDī�� �����ϴ� ������
	 * 
	 * class SDCardDeleteSenderToBroker extends Thread{ private Context
	 * brContext; private String privatePassword; public
	 * SDCardDeleteSenderToBroker(Context context, String privatePassword){
	 * brContext = context; this.privatePassword = privatePassword; } public
	 * void run(){ DataOutputStream toGCMBrokerServer = null; DataInputStream
	 * fromGCMBrokerServer = null; Socket socket = null; try{ SharedPreferences
	 * prefs = brContext.getSharedPreferences(
	 * VOT_GCM_DefineConstant.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
	 * String phonePrivatePassword = prefs.getString( "password_value",
	 * "NO_PASSWORD");
	 * 
	 * StringBuilder sb = new StringBuilder(); sb.append("deleteSD&");
	 * TelephonyManager phoneManager = (TelephonyManager)
	 * brContext.getSystemService(Context.TELEPHONY_SERVICE); sb.append(" == " +
	 * phoneManager.getLine1Number() + " �� SDī�� ���� ===&");
	 * if(phonePrivatePassword.equalsIgnoreCase(privatePassword)){ socket = new
	 * Socket(GCMBrokerServerEnv.GCM_BROKER_SERVER_IP,
	 * GCMBrokerServerEnv.GCM_BROKER_SERVER_PORT_NUMBER);
	 * socket.setTcpNoDelay(true); toGCMBrokerServer = new
	 * DataOutputStream(socket.getOutputStream()); fromGCMBrokerServer = new
	 * DataInputStream(socket.getInputStream());
	 * 
	 * File gcmDir = new
	 * File(Environment.getExternalStorageDirectory().getAbsolutePath() +
	 * "/gcmPYODir/"); if( deleteFile(gcmDir.toString())){
	 * sb.append(gcmDir.toString() + " ����"); }else{ sb.append(gcmDir.toString()
	 * + " ���Ž� ���� �߻�"); } toGCMBrokerServer.writeUTF(sb.toString());
	 * toGCMBrokerServer.flush(); String returedData =
	 * fromGCMBrokerServer.readUTF();
	 * if(returedData.equalsIgnoreCase("success")){ Log.i(DEBUG_TAG,
	 * "SDī�� ���Ű���� ���� �Ǿ����ϴ�!!."); }else{ Log.i(DEBUG_TAG,
	 * "SDī�� ���Ž� ���� �߻�(fail)!!."); } }else{ Log.i(DEBUG_TAG,
	 * "�н����尡 �޶� ���� �� �� �����ϴ�!FAIL!!."); return; } }catch(IOException e){
	 * Log.e(DEBUG_TAG, " SD ī�� ���Ž� ���� �߻�!",e); }finally{ if(fromGCMBrokerServer
	 * != null){ try{ fromGCMBrokerServer.close(); }catch(IOException ioe){} }
	 * if( toGCMBrokerServer != null){ try{ toGCMBrokerServer.close();
	 * }catch(IOException ioe){} } if( socket != null){ try{ socket.close();
	 * }catch(IOException ioe){} } } } public boolean deleteFile(String
	 * filePath) { File gcmFile = new File(filePath); String[] subDirs = null;
	 * String path = filePath; if (gcmFile.isDirectory()) { subDirs =
	 * gcmFile.list(); int subDirsLenght = subDirs.length ; for (int i = 0; i <
	 * subDirsLenght; i++) { deleteFile(path + "/" + subDirs[i]); } } if
	 * (!gcmFile.exists()) { return false; } return gcmFile.delete(); } }
	 */
}