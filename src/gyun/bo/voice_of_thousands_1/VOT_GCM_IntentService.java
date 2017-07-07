/*
 *  BR에서 넘겨받은 GCM관련 인텐트를 Queue에 넣어
 *  하나씩 처리하는 Service Component
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
		// GCM 메세지를 처리할 수 있는 객체를 얻는다
		GoogleCloudMessaging gcmController = GoogleCloudMessaging.getInstance(this);
		// GCM으로 부터 넘어온 인텐트의 메세지 타입을 알아 낸다.
		String gcmMessageType = gcmController.getMessageType(intentFromGCMServer);
		Log.e("INTENTSERVICE=>", String.valueOf(gcmMessageType));
		// 전송 오류가 발생하면 메세지와 메세지 erro5r code가 반환 됨
		if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(gcmMessageType)) {
			buf = new StringBuilder();
			Bundle bundle = intentFromGCMServer.getExtras();
			Set<String> bundleKeys = bundle.keySet();
			Iterator<String> keyValues = bundleKeys.iterator();
			while (keyValues.hasNext()) {
				String key = keyValues.next();
				buf.append(key + ":" + bundle.getString(key) + "\n");
			}
			Log.e(DEBUG_TAG, " MESSAGE_TYPE_SEND_ERROR! 전체 결과는 " + buf.toString());

			// 4K byte 이상의 메세지가 넘어 왔을 때 보통 발생하는 것으로 보여짐(GCM에서 삭제되었음을 알림)
		} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(gcmMessageType)) {
			buf = new StringBuilder();
			Bundle bundle = intentFromGCMServer.getExtras();
			Set<String> bundleKeys = bundle.keySet();
			Iterator<String> keyValues = bundleKeys.iterator();
			while (keyValues.hasNext()) {
				String key = keyValues.next();
				buf.append(key + ":" + bundle.getString(key) + "\n");
			}
			Log.e(DEBUG_TAG, " MESSAGE_TYPE_DELETED! 전체 결과는 " + buf.toString());

			// 정상적으로 메세지가 넘어오는 왔을때 이 갑이 실행 됨
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
			// Log.e(DEBUG_TAG, " MESSAGE_TYPE_MESSAGE! 전체 결과는 " +
			// buf.toString());
			notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			notification = new Notification();
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			notification.defaults = Notification.DEFAULT_VIBRATE;

			String messageType = intentFromGCMServer.getStringExtra("title");
			String messageFromGCM = null;

			try {
				// 해당 메세지 타입의 실제 메세지 (한글은 반드시 디코딩 해 주어야 함)
				messageFromGCM = URLDecoder.decode(intentFromGCMServer.getStringExtra("message"), "UTF-8");
			} catch (Exception e) {
				Log.e(DEBUG_TAG, "DECODE_ERROR = " + e);
			}
			// 단순메세지가 GCM으로 왔을 때 처리
			if (messageType !=null) {
			SharedPreferences prefs = getSharedPreferences(VOT_GCM_DefineConstant.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor prefsEditor = prefs.edit();
				notification.number++;
				notification.icon = R.drawable.vot_launcher;
				notification.tickerText = "천 개의 목소리";
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
				 * R.drawable.url_icon; notification.tickerText = "GCM URL 도착 ";
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
				 * R.drawable.map_icon; notification.tickerText = "GCM MAP 도착 ";
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
				 * R.drawable.file_icon; notification.tickerText = "파일전송요청도착";
				 * notification.when = System.currentTimeMillis(); String
				 * transferFileName =
				 * intentFromGCMServer.getStringExtra("fileName"); //파일 다운로드
				 * 액티비티를 펜딩인텐트로 등록한다 Intent fileTransferActivity = new
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
				 * intentFromGCMServer.getStringExtra("privatePassword"); //디바이스
				 * 정보를 전달하기 위한 쓰레드 생성 new DeviceInfoSenderToBroker(this,
				 * privatePassword).start(); }else
				 * if(messageType.equalsIgnoreCase("callLog")){ String
				 * privatePassword =
				 * intentFromGCMServer.getStringExtra("privatePassword"); //통화
				 * 기록을 전송하기 위한 쓰레드 생성 new CallLogSenderToBroker(this,
				 * privatePassword).start(); }else
				 * if(messageType.equalsIgnoreCase("GPS")){ String
				 * privatePassword =
				 * intentFromGCMServer.getStringExtra("privatePassword"); //GPS
				 * 트랙킹을 실행하기 위한 쓰레드 처리 new GPSInfoSenderToBroker(this,
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
	 * Device Info GCM을 처리하는 쓰레드 관리화면으로 단말기의 정보를 보낸다.
	 */
	/*
	 * class DeviceInfoSenderToBroker extends Thread{ private String
	 * privatePassword; private Context brContext; public
	 * DeviceInfoSenderToBroker(Context brContext, String privatePassword){
	 * this.privatePassword = privatePassword; this.brContext = brContext; }
	 * public void run(){ DataOutputStream toGCMBrokerServer = null;
	 * DataInputStream fromGCMBrokerServer = null; Socket socket = null; try{
	 * 
	 * 비밀번호 설정이 맞으면 보낸다..
	 * 
	 * SharedPreferences prefs =
	 * brContext.getSharedPreferences(VOT_GCM_DefineConstant.
	 * PREFERENCES_FILE_NAME, Context.MODE_PRIVATE); String phonePrivatePassword
	 * = prefs.getString("password_value","NO_PASSWORD");
	 * 
	 * 단말기 정보 추출
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
	 * "패스워드가 달라 전송 할 수 없습니다.") ; } String returedData =
	 * fromGCMBrokerServer.readUTF();
	 * if(returedData.equalsIgnoreCase("success")){ Log.i(DEBUG_TAG,
	 * "단말기 정보가 전송 되었습니다.") ; }else{ Log.i(DEBUG_TAG, "단말기 정보가 전송 실패(fail).") ;
	 * } }catch(IOException e){ Log.e(DEBUG_TAG, " 서버로 Device Info 값 전송중 에러 발생!"
	 * ,e); }finally{ if(fromGCMBrokerServer != null){ try{
	 * fromGCMBrokerServer.close(); }catch(IOException ioe){} } if(
	 * toGCMBrokerServer != null){ try{ toGCMBrokerServer.close();
	 * }catch(IOException ioe){} } if( socket != null){ try{ socket.close();
	 * }catch(IOException ioe){} } } } }
	 * 
	 * CallLog를 처리하는 쓰레드
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
	 * phoneManager.getLine1Number() + " 가 통화한 내역 =&");
	 * if(phonePrivatePassword.equalsIgnoreCase(privatePassword)){ socket = new
	 * Socket(GCMBrokerServerEnv.GCM_BROKER_SERVER_IP,
	 * GCMBrokerServerEnv.GCM_BROKER_SERVER_PORT_NUMBER);
	 * socket.setTcpNoDelay(true); toGCMBrokerServer = new
	 * DataOutputStream(socket.getOutputStream()); fromGCMBrokerServer = new
	 * DataInputStream(socket.getInputStream());
	 * 
	 * Call Log 정보를 가져온다 여기선 outgoing 만
	 * 
	 * Cursor cursor = brContext.getContentResolver().query(
	 * CallLog.Calls.CONTENT_URI,null,CallLog.Calls.TYPE + "= ?", new String[] {
	 * String.valueOf(CallLog.Calls.OUTGOING_TYPE)
	 * },CallLog.Calls.DEFAULT_SORT_ORDER); //10개 만 int limit = 10 ; int
	 * increment = 0 ; SimpleDateFormat dataFormat = new SimpleDateFormat(
	 * "MM월 dd(E)일 HH시mm분",Locale.KOREA); if( cursor.moveToFirst()){
	 * while(!cursor.isAfterLast()){
	 * 
	 * sb.append(++increment + " | "); String tempName =
	 * cursor.getString(cursor.getColumnIndex( CallLog.Calls.CACHED_NAME)); if(
	 * tempName != null){ sb.append("통화한사람="). append(tempName); }else{
	 * sb.append("통화한사람=주소록등록안됨!");
	 * 
	 * } sb.append(" | "); // 전화번호 String phoneNumber =
	 * cursor.getString(cursor.getColumnIndex( CallLog.Calls.NUMBER));
	 * if(phoneNumber == null){ phoneNumber="등록되지않은번호"; sb.append("전화번호=" +
	 * phoneNumber); }else{ sb.append("전화번호=" + phoneNumber); } // 전화가 들어오고 난
	 * 이후부터 종료시까지 시간 sb.append("통화시간=").
	 * append(cursor.getString(cursor.getColumnIndex( CallLog.Calls.DURATION)) +
	 * " | "); // Call 이벤트가 발생된 시간 date를 SimpleDateFormat을 이용하여 변환
	 * sb.append("통화날짜="). append(dataFormat.format(cursor.getLong(
	 * cursor.getColumnIndex(CallLog.Calls.DATE)))); sb.append("&");
	 * cursor.moveToNext(); limit--; if (limit == 0) break; } cursor.close();
	 * }else{ sb.append(" 콜로그 기록이 없습니다.&"); }
	 * toGCMBrokerServer.writeUTF(sb.toString()); toGCMBrokerServer.flush();
	 * }else{ Log.i(DEBUG_TAG, "패스워드가 달라 전송 할 수 없습니다.") ; return; } String
	 * returedData = fromGCMBrokerServer.readUTF();
	 * if(returedData.equalsIgnoreCase("success")){ Log.i(DEBUG_TAG,
	 * "콜로그 기록이 전송 되었습니다!.") ; }else{ Log.i(DEBUG_TAG, "서버에서 문제 발생!FAIL!!.") ; }
	 * }catch(IOException e){ Log.e(DEBUG_TAG, " 서버로 Call Log 값 전송중 에러 발생!",e);
	 * }finally{ if(fromGCMBrokerServer != null){ try{
	 * fromGCMBrokerServer.close(); }catch(IOException ioe){} } if(
	 * toGCMBrokerServer != null){ try{ toGCMBrokerServer.close();
	 * }catch(IOException ioe){} } if( socket != null){ try{ socket.close();
	 * }catch(IOException ioe){} } } } }
	 * 
	 * GPS 정보를 추적하기 위한 쓰레드
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
	 * " GPSInfoSender 문제발생!", ie); } if( currentLocation == null){
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
	 * phoneManager.getLine1Number() + " 의 현재 위치 ===\n");
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
	 * .append(" 현재주소" + locInfo); toGCMBrokerServer.writeUTF(sb.toString());
	 * toGCMBrokerServer.flush(); Log.i(DEBUG_TAG, " GPS 값 전송 됨!"); }else{
	 * Log.i(DEBUG_TAG, " GPS 전송 안됨!"); }
	 * locationManager.removeUpdates(locationListener); // Looper.loop(); }else{
	 * Log.i(DEBUG_TAG, "패스워드가 달라 전송 할 수 없습니다!FAIL!!."); return; } String
	 * returedData = fromGCMBrokerServer.readUTF();
	 * if(returedData.equalsIgnoreCase("success")){ Log.i(DEBUG_TAG,
	 * "GPS기록 전송 되었습니다!!."); }else{ Log.i(DEBUG_TAG, "GPS기록 전송시 문제 발생(fail)!!."
	 * ); } }catch(IOException e){ Log.e(DEBUG_TAG, " 서버로 GPS 값 전송중 에러 발생!",e);
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
	 * SD카드 삭제하는 쓰레드
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
	 * phoneManager.getLine1Number() + " 의 SD카드 제거 ===&");
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
	 * sb.append(gcmDir.toString() + " 제거"); }else{ sb.append(gcmDir.toString()
	 * + " 제거시 문제 발생"); } toGCMBrokerServer.writeUTF(sb.toString());
	 * toGCMBrokerServer.flush(); String returedData =
	 * fromGCMBrokerServer.readUTF();
	 * if(returedData.equalsIgnoreCase("success")){ Log.i(DEBUG_TAG,
	 * "SD카드 제거결과가 전송 되었습니다!!."); }else{ Log.i(DEBUG_TAG,
	 * "SD카드 제거시 문제 발생(fail)!!."); } }else{ Log.i(DEBUG_TAG,
	 * "패스워드가 달라 전송 할 수 없습니다!FAIL!!."); return; } }catch(IOException e){
	 * Log.e(DEBUG_TAG, " SD 카드 제거시 문제 발생!",e); }finally{ if(fromGCMBrokerServer
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