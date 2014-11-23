package com.example.facebookauth;



import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends FragmentActivity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_main);
		if(getSharedPreferences("AUTH", MODE_PRIVATE).getInt("AUTH?", -1)==2||
				getSharedPreferences("AUTH", MODE_PRIVATE).getInt("AUTH?", -1)==1){
			Intent intent=new Intent(this, HomeScreen.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			

		}
		
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		Log.d("MainActivity","onActivityResult");
		if (arg0 == GoogleAuth.RC_SIGN_IN) {
            GoogleAuth fragment = (GoogleAuth) getSupportFragmentManager()
                    .findFragmentById(R.id.google);
            fragment.onActivityResult(arg0, arg1, arg2);
        } else {
            super.onActivityResult(arg0, arg1, arg2);
        }
		
	}
	
}
