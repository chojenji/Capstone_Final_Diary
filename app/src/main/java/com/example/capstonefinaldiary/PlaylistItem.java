package com.example.capstonefinaldiary;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class PlaylistItem implements Parcelable {
    // 플레이리스트 아이템의 데이터를 저장하는 모델
    // 서버의 응답 JSON 구조에 맞춰져 있어야함
    @SerializedName("id")
    private  String id;
    @SerializedName("artist") //서버 응답에서의 필드 이름과 일치하도록
    private String artist;

    @SerializedName("title")
    private String title;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("preview_url")
    private String previewUrl;

    // Parcelable 생성자
    protected PlaylistItem(Parcel in) {
        id = in.readString();
        artist = in.readString();
        title = in.readString();
        imageUrl = in.readString();
        previewUrl = in.readString();
    }

    // Parcelable 인터페이스 구현
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(artist);
        dest.writeString(title);
        dest.writeString(imageUrl);
        dest.writeString(previewUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlaylistItem> CREATOR = new Creator<PlaylistItem>() {
        @Override
        public PlaylistItem createFromParcel(Parcel in) {
            return new PlaylistItem(in);
        }

        @Override
        public PlaylistItem[] newArray(int size) {
            return new PlaylistItem[size];
        }
    };

    // Getter 및 Setter 메서드
    public String getId(){
        return id;
    }
    public void setId(String Id){
        id = Id;
    }
    public String getArtist(){
        return artist;
    }
    public void setArtist(String Artist){
        artist = Artist;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String Title){
        title = Title;
    }
    public String getImageUrl(){
        return imageUrl;
    }
    public void setImageUrl(String ImageUrl){
        imageUrl = ImageUrl;
    }
    public String getPreviewUrl(){
        return previewUrl;
    }

    public void setPreviewUrl(String PreviewUrl) {
        previewUrl = PreviewUrl;
    }
}
