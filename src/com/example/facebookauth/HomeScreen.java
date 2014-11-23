package com.example.facebookauth;

import com.facebook.Session;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class HomeScreen extends ActionBarActivity implements
		ConnectionCallbacks, OnConnectionFailedListener {
	GoogleApiClient mGoogleApiClient;
	int id;
	SharedPreferences pref;
	Session session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		pref = getSharedPreferences("AUTH", MODE_PRIVATE);
		id = pref.getInt("AUTH?", -1);
		if (id == 2) {
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this).addApi(Plus.API)
					.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		}else if(id==1){
			session = Session.getActiveSession();
			if(session!=null){
				Toast.makeText(this, "session not null", Toast.LENGTH_SHORT).show();
			  if(session.isOpened()){
				  Toast.makeText(this, "session is open", Toast.LENGTH_SHORT).show();
			  }
			}
		     
		}
		findViewById(R.id.signout_home).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (id == 2) {
							Plus.AccountApi
									.clearDefaultAccount(mGoogleApiClient);
							mGoogleApiClient.disconnect();
						}else if(id==1){
							session.closeAndClearTokenInformation();
						}
						signOut();

					}
				});
	}

	public void signOut() {
		SharedPreferences.Editor edit = pref.edit();
		edit.putInt("AUTH?", -1);
		edit.commit();
		Intent intent = new Intent(HomeScreen.this, MainActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (id == 2) {
			mGoogleApiClient.connect();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (id == 2) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Toast.makeText(this, "connection failed in home screen",
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onConnected(Bundle arg0) {
		Toast.makeText(this, "connection passed in home screen",
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
	}

}
