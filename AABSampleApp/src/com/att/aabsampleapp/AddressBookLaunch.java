package com.att.aabsampleapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.att.api.aab.manager.AabManager;
import com.att.api.error.AttSdkError;
import com.att.api.oauth.OAuthToken;
import com.att.sdk.listener.AttSdkListener;

public class AddressBookLaunch extends Activity {
	
	private final int OAUTH_CODE = 1;
	private AabManager aabManager;
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showProgressDialog("Opening  AddressBook .. ");
		setContentView(R.layout.activity_address_book_launch);
		
		Intent i = new Intent(this,
				com.att.api.consentactivity.UserConsentActivity.class);
		i.putExtra("fqdn", Config.fqdn);
		i.putExtra("clientId", Config.clientID);
		i.putExtra("clientSecret", Config.secretKey);
		i.putExtra("redirectUri", Config.redirectUri);
		i.putExtra("appScope", Config.appScope);
		
		startActivityForResult(i, OAUTH_CODE);				
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == OAUTH_CODE) {
			String oAuthCode = null;
			if (resultCode == RESULT_OK) {
				oAuthCode = data.getStringExtra("oAuthCode");
				Log.i("ContactList", "oAuthCode:" + oAuthCode);
				if (null != oAuthCode) {				
					aabManager = new AabManager(Config.fqdn, Config.clientID,Config.secretKey,new getTokenListener());
					aabManager.getOAuthToken(oAuthCode); 
					
				} else {
					Log.i("ContactList", "oAuthCode: is null");
				}
			} 					
		} 
	}
	
	
	
	public class getTokenListener implements AttSdkListener {

		@Override
		public void onSuccess(Object response) {
			OAuthToken authToken = (OAuthToken) response;
			if (null != authToken) {
				Config.authToken = authToken;
				Config.token = authToken.getAccessToken();
				Config.refreshToken = authToken.getRefreshToken();
				Log.i("getTokenListener","onSuccess Message : " + authToken.getAccessToken());
				getAddressBookContacts();
			}
		}

		@Override
		public void onError(AttSdkError error) {
			Log.i("getTokenListener","onError Message : " );
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.address_book_launch, menu);

		return super.onCreateOptionsMenu(menu);
	}

	public void getAddressBookContacts() {
		Intent i = new Intent(AddressBookLaunch.this, SampleAppLauncher.class);
		startActivity(i);
		dismissProgressDialog();
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			
			case R.id.action_logout :
				CookieSyncManager.createInstance(this);
				CookieManager cookieManager = CookieManager.getInstance();
				cookieManager.removeAllCookie();
				cookieManager.removeExpiredCookie();
				cookieManager.removeSessionCookie();
				finish();
				break;
			
			default :
				return super.onOptionsItemSelected(item);
			}		
			return true;
	}	

	// Progress Dialog
		public void showProgressDialog(String dialogMessage) {

			if (null == pDialog)
				pDialog = new ProgressDialog(this);
			pDialog.setCancelable(false);
			pDialog.setMessage(dialogMessage);
			pDialog.show();
		}

		public void dismissProgressDialog() {
			if (null != pDialog) {
				pDialog.dismiss();
			}
		}
	
}