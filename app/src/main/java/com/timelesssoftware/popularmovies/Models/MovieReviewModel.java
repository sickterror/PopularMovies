package com.timelesssoftware.popularmovies.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Luka on 10. 10. 2017.
 */

public class MovieReviewModel implements Parcelable {
    String id;
    String author;
    String content;
    String url;

    protected MovieReviewModel(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public static final Creator<MovieReviewModel> CREATOR = new Creator<MovieReviewModel>() {
        @Override
        public MovieReviewModel createFromParcel(Parcel in) {
            return new MovieReviewModel(in);
        }

        @Override
        public MovieReviewModel[] newArray(int size) {
            return new MovieReviewModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(author);
        parcel.writeString(content);
        parcel.writeString(url);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
