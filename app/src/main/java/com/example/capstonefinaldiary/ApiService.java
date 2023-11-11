package com.example.capstonefinaldiary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/** Retrofit을 사용하여 Flask 앱의
 * '/get_playlist' 엔드포인트를 호출하는 API인터페이스 정의*/
public interface ApiService {
    // 서버로부터 PlaylistResponse 타입의 응답을 받습니다.
    @GET("/get_playlist")
    Call<List<PlaylistItem>> getPlaylist();
    // PlaylistItem 클래스는 서버에서 받은 플레이리스트 아이템의 데이터를 저장하는 모델 클래스
}