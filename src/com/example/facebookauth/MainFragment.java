package com.example.facebookauth;

import java.util.Arrays;









import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainFragment extends Fragment implements GraphUserCallback {
	private static final String TAG = "MainFragment";
	private UiLifecycleHelper uiHelper;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 View view = inflater.inflate(R.layout.main, container, false);
		 LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
		 authButton.setReadPermissions(Arrays.asList("user_location", "user_birthday", "user_likes","email"));
		 authButton.setFragment(this);

		return view;
	}
	@SuppressWarnings("deprecation")
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		Log.d("MainFragment","onSessionStateChange");
		if (state.isOpened()) {
			Log.d("MainFragment","onSessionStateChange in opened");
	    	Request.executeMeRequestAsync(session, this);
	        Log.i(TAG, "Logged in...");
	    } else if (state.isClosed()) {
	    	Log.d("MainFragment","onSessionStateChange in closed");
	        Log.i(TAG, "Logged out...");
	    }
	}
	private Session.StatusCallback callback = new Session.StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			// TODO Auto-generated method stub
			
		}
	};
	@Override
	public void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	    Session session = Session.getActiveSession();
	    Log.d("MainFragment","OnResume");
	    if(session==null){
	    	  Log.d("MainFragment","OnResume session null");
	    }
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	        Log.d("MainFragment","OnResume if passed");
	    }else{
	    	Log.d("MainFragment","OnResume session not opened or closed");
	    }
	    
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	@Override
	public void onCompleted(GraphUser user, Response response) {
		SharedPreferences pref=getActivity().getSharedPreferences("AUTH", FragmentActivity.MODE_PRIVATE);
		SharedPreferences.Editor edit=pref.edit();
		edit.putInt("AUTH?", 1);
		edit.commit();	
		Toast.makeText(getActivity(), user.getProperty("email").toString(), Toast.LENGTH_LONG).show();
		String name=user.getFirstName()+" "+user.getLastName();
		String email=user.getProperty("email").toString();
		String auth="facebook";
		DetailsParcelable parcel=new DetailsParcelable(name, email, auth);
		Intent intent=new Intent(getActivity(), HomeScreen.class);
		intent.putExtra("FACEBOOKPARCEL", parcel);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);	
		getActivity().finish();
	}
	

}
