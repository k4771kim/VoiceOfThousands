package gyun.bo.voice_of_thousands_1;

import java.util.Arrays;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class VOT_Login_Page extends Activity
		implements ConnectionCallbacks, OnConnectionFailedListener, OnClickListener {

	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
		// mGoogleApiClient.connect();
		/*
		 * if (responseCode == RESULT_OK) { if (login_flag == "facebook") {
		 * revokeGplusAccess(); } else if (login_flag == "google") { logout(); }
		 * }
		 */
		if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
			// mGoogleApiClient.connect();
			/*
			 * try { mConnectionResult.startResolutionForResult(this,
			 * REQUEST_CODE_RESOLVE_ERR);
			 * 
			 * } catch (SendIntentException e) { // �ٽ� ������ �õ��մϴ�.
			 * mConnectionResult = null; mGoogleApiClient.connect(); }
			 */

		logout();
			// ����
			// mConnectionProgressDialog.show(); // �α��θ�
			// ȣ���.

		}
		if (responseCode == RESULT_OK) {
			mConnectionProgressDialog.dismiss();
			if (login_flag == "facebook") {
				callbackManager.onActivityResult(requestCode, responseCode, intent);

			} else if (login_flag == "google") {
				mConnectionProgressDialog.show();
				
				mGoogleApiClient.connect();
				
			revokeGplusAccess();

			}

			/*
			 * if (login_flag.equalsIgnoreCase("facebook")) {
			 * mConnectionProgressDialog.dismiss();
			 * 
			 * }
			 */
		}

	}

	// private static int SFLASH_DELAY = 2000; // ms
	public static String SNS_Name;
	private static final String TAG = "SplashActivity";
	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	private String login_flag;
	private ProgressDialog mConnectionProgressDialog;
	private GoogleApiClient mGoogleApiClient;
	private ConnectionResult mConnectionResult;
	private CallbackManager callbackManager;
	public static String login_tocken;
	public static String login_way;
	private AccessToken accessToken;
	private AccessTokenTracker accessTokenTracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FacebookSdk.sdkInitialize(getApplicationContext());
		callbackManager = CallbackManager.Factory.create();

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.vot_loginpage);
		mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).build();
		findViewById(R.id.google_login_btn).setOnClickListener(this);
		// findViewById(R.id.twitter_login_btn).setOnClickListener(this);
		findViewById(R.id.facebook_login_btn).setOnClickListener(this);
		mConnectionProgressDialog = new ProgressDialog(this);
		mConnectionProgressDialog.setMessage("���� Ȯ�����Դϴ�. ��ø� ��ٷ� �ּ���.");
		accessTokenTracker = new AccessTokenTracker() {
			@Override
			protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
				// Set the access token using
				// currentAccessToken when it's loaded or set.
			}
		};
		// If the access token is available already assign it.
		accessToken = AccessToken.getCurrentAccessToken();

	}

	@Override
	protected void onStart() {
		super.onStart();
		// mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.hold, android.R.anim.fade_out);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (mConnectionProgressDialog.isShowing()) {
			// Log.e("", "");
			// ����ڰ� �̹� �α��� ��ư�� Ŭ���߽��ϴ�. �ذ��� �����մϴ�.
			// ���� ������ �߻��߽��ϴ�. onConnected()�� ���� ��ȭ���ڸ� ���� ������
			// ��ٸ��ϴ�.
			if (result.hasResolution()) {
				try {
					result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					mGoogleApiClient.connect();
				}
			}
		}
		mConnectionProgressDialog.dismiss();
		// ����ڰ� �α��� ��ư�� Ŭ���� �� Ȱ���� ������ �� �ֵ���
		// ����Ʈ�� �����մϴ�.
		mConnectionResult = result;

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		mConnectionProgressDialog.dismiss();
		// Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
		login_tocken = Plus.AccountApi.getAccountName(mGoogleApiClient);

		SharedPreferences prefs = getSharedPreferences(VOT_GCM_DefineConstant.PREFERENCES_FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = prefs.edit();
		prefsEditor.putString("google_Token", login_tocken);

		prefsEditor.putString("recent_login_way", "google");
		prefsEditor.commit();
		// Log.e("���� �����۷��� �����.", login_tocken.toString());
		// Toast.makeText(this, login_tocken + "is login now",
		// Toast.LENGTH_LONG).show();
		Intent login_credit = new Intent(VOT_Login_Page.this, Login_Auth_Activity.class);

		login_way = "G";
		login_credit.putExtra("login_way", login_way);
		login_credit.putExtra("login_token", login_tocken);
		// SNS_Name =
		// Plus.PeopleApi.getCurrentPerson(mGoogleApiClient).getNickname();
		startActivity(login_credit); // ���� - �α��� ������������ �̵�����.
		finish();

	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
		// Log.e("", "");
		// mConnectionProgressDialog.show();
		if (v.getId() == R.id.google_login_btn && !mGoogleApiClient.isConnected()) {

			login_way = "G";
			login_flag = "google";
			// if (mConnectionResult == null) {
			//
			// mConnectionProgressDialog.show();
			// // mGoogleApiClient.connect();
			// } else {
			// try {
			// mConnectionResult.startResolutionForResult(this,
			// REQUEST_CODE_RESOLVE_ERR);
			// } catch (SendIntentException e) {
			// // �ٽ� ������ �õ��մϴ�.
			// mConnectionResult = null;
			// mGoogleApiClient.connect();
			// }
			if (mConnectionResult == null) {
				mConnectionProgressDialog.show();
				mGoogleApiClient.connect();
			} else {
				try {
					mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					// �ٽ� ������ �õ��մϴ�.
					mConnectionResult = null;
					mGoogleApiClient.connect();
				}

			}
		}

		else if (v.getId() == R.id.facebook_login_btn) {
			login_flag = "facebook";

			// ���̽��� �α���.

			LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
				@Override
				public void onSuccess(LoginResult result) {
					// TODO Auto-generated method stub

					Log.d("Success", "Login");

					SharedPreferences prefs = getSharedPreferences(VOT_GCM_DefineConstant.PREFERENCES_FILE_NAME,
							Context.MODE_PRIVATE);
					SharedPreferences.Editor prefsEditor = prefs.edit();

					prefsEditor.putString("facebook_Token", result.getAccessToken().getUserId().toString()); // �����۷�����
					// ���̽���
					// ��ū
					// ����.

					prefsEditor.putString("recent_login_way", "facebook"); // �����۷�����
																			// �ֱ�
																			// �α��ΰ�����
																			// ���̽�����

					prefsEditor.commit();
					// Log.e("���̽��� �����۷��� �����.", result.toString());
					login_tocken = result.getAccessToken().getUserId().toString();
					login_way = "F";
					Intent login_credit = new Intent(VOT_Login_Page.this, Login_Auth_Activity.class);
					login_credit.putExtra("login_way", login_way);
					login_credit.putExtra("login_token", login_tocken);

					startActivity(login_credit);
					finish();

				}

				@Override
				public void onError(FacebookException error) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplication(), "���̽��� �α��ο� �����Ͽ����ϴ�.", Toast.LENGTH_LONG).show();
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub

					Toast.makeText(getApplication(), "���̽��� �α����� ����ϼ̽��ϴ�.", Toast.LENGTH_LONG).show();
				}

			});
			LoginManager.getInstance().logInWithReadPermissions(this,
					Arrays.asList("public_profile", "user_friends", "email"));

		}
		/*
		 * else if (v.getId() == R.id.twitter_login_btn)
		 * 
		 * { // Ʈ���� �α����� ���⼭ ����. } revokeGplusAccess(); }
		 */
	}

	private void revokeGplusAccess() {
		// Log.e("", "");

		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();
			mConnectionProgressDialog.show();
		}
	}

	private void signOutFromGplus() {
		// Log.e("", "");
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();
			// updateUI(false);
		}
	}

	public void logout() {
		// Log.e("", "");
		if (accessToken != null && accessTokenTracker != null) {
			AccessToken.setCurrentAccessToken(null);
			accessTokenTracker.stopTracking();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// The activity is about to be destroyed.
	}

}
