package com.example.facebookauth;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class GoogleAuth extends Fragment implements ConnectionCallbacks,
		OnConnectionFailedListener {
	/* Request code used to invoke sign in user interactions. */
	public static final int RC_SIGN_IN = 0;
	private GoogleApiClient mGoogleApiClient;
	/*
	 * A flag indicating that a PendingIntent is in progress and prevents us
	 * from starting further intents.
	 */
	private boolean mIntentInProgress;
	private boolean mSignInClicked;
	Button button;
	private ConnectionResult mConnectionResult;
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.google_auth, container, false);
		mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		view.findViewById(R.id.sign_in_button).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!mGoogleApiClient.isConnecting()) {
							mSignInClicked = true;
							Toast.makeText(getActivity(), "clcked",
									Toast.LENGTH_SHORT).show();
							resolveSignInError();
						}

					}
				});
		view.findViewById(R.id.signout).setOnClickListener(new View.OnClickListener() {
	           @Override
	           public void onClick(View v) {
	               Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
	               mGoogleApiClient.disconnect();
	               mGoogleApiClient.connect();
	               

	           }
	       });

	}

	@Override
	public void onStart() {
		super.onStart();
		mGoogleApiClient.connect();

	}

	@Override
	public void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				getActivity().startIntentSenderForResult(
						mConnectionResult.getResolution().getIntentSender(),
						RC_SIGN_IN, null, 0, 0, 0);
			} catch (IntentSender.SendIntentException e) {
				// The intent was canceled before it was sent. Return to the
				// default
				// state and attempt to connect to get an updated
				// ConnectionResult.
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!mIntentInProgress) {
			Toast.makeText(getActivity(), "here in on connection failed",
					Toast.LENGTH_SHORT).show();
			mConnectionResult = result;
			if (mConnectionResult != null) {
				Toast.makeText(getActivity(), "not null", Toast.LENGTH_SHORT)
						.show();
			}

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}

	}

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;
		
		Toast.makeText(getActivity(), "User is connected!", Toast.LENGTH_LONG)
				.show();
		SharedPreferences pref=getActivity().getSharedPreferences("AUTH", FragmentActivity.MODE_PRIVATE);
		SharedPreferences.Editor edit=pref.edit();
		edit.putInt("AUTH?", 2);
		edit.commit();	
		Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
		 String personName = currentPerson.getDisplayName();
		 String auth="google";
		 String email=Plus.AccountApi.getAccountName(mGoogleApiClient);
		 DetailsParcelable parcel=new DetailsParcelable(personName, email, auth);
		 Intent intent=new Intent(getActivity(), HomeScreen.class);
		 intent.putExtra("GOOGLEPARCEL",parcel);
		 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 startActivity(intent);
         getActivity().finish();
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("GoogleAuth","onActivityResult out side");
		if (requestCode == RC_SIGN_IN) {
			Log.d("GoogleAuth","onActivityResult RC_SIGN_IN");
			if (resultCode != Activity.RESULT_OK) {
				Log.d("GoogleAuth","onActivityResult resultCode != Activity.RESULT_OK");
				mSignInClicked = false;
			}
               
			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				Log.d("GoogleAuth","!mGoogleApiClient.isConnecting()");
				mGoogleApiClient.connect();
			}
		}
	}

}
