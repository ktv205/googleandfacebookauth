package com.example.facebookauth;

import android.os.Parcel;
import android.os.Parcelable;

public class DetailsParcelable implements Parcelable {
	private String name;
	private String email;
	private String authProvider;

	public DetailsParcelable(String name, String email, String authProvider) {
		this.name = name;
		this.email = email;
		this.authProvider = authProvider;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAuthProvider() {
		return authProvider;
	}

	public void setAuthProvider(String authProvider) {
		this.authProvider = authProvider;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public static final Parcelable.Creator<DetailsParcelable> CREATOR=new Creator<DetailsParcelable>() {
		
		@Override
		public DetailsParcelable[] newArray(int size) {
			// TODO Auto-generated method stub
			return new DetailsParcelable[size];
		}
		
		@Override
		public DetailsParcelable createFromParcel(Parcel source) {
			
			return new DetailsParcelable(source);
		}
	};
     public DetailsParcelable(Parcel source) {
		
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(email);
		dest.writeString(authProvider);

	}

}
