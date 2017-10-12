package com.timelesssoftware.popularmovies.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Luka on 12.10.2017.
 */

public class FragmentSettingsObject implements Parcelable {
    public int toolbarColor;
    public int statusBarColor;
    public String title;


    public FragmentSettingsObject(int toolbarColor, int statusBarColor, String title) {
        this.toolbarColor = toolbarColor;
        this.statusBarColor = statusBarColor;
        this.title = title;
    }

    protected FragmentSettingsObject(Parcel in) {
        toolbarColor = in.readInt();
        statusBarColor = in.readInt();
        title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(toolbarColor);
        dest.writeInt(statusBarColor);
        dest.writeString(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FragmentSettingsObject> CREATOR = new Creator<FragmentSettingsObject>() {
        @Override
        public FragmentSettingsObject createFromParcel(Parcel in) {
            return new FragmentSettingsObject(in);
        }

        @Override
        public FragmentSettingsObject[] newArray(int size) {
            return new FragmentSettingsObject[size];
        }
    };
}
