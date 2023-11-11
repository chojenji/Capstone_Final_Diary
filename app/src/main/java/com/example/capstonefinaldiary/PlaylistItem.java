package com.example.capstonefinaldiary;

import com.google.gson.annotations.SerializedName;
public class PlaylistItem {
    // 플레이리스트 아이템의 데이터를 저장하는 모델
    // 서버의 응답 JSON 구조에 맞춰져 있어야함
    @SerializedName("artist") //서버 응답에서의 필드 이름과 일치하도록
    private String artist;

    @SerializedName("title")
    private String title;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("preview_url")
    private String previewUrl;

    // Getter 및 Setter 메서드

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
