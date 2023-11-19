package com.example.capstonefinaldiary;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** Retrofit을 초기화하고 Flask 서버로 요청을 보내는 코드*/
public class RetrofitClient {
    // Flask 서버가 실행 중인 주소와 포트
    //private static final String BASE_URL = "http://localhost:8080"; // Flask 서버 주소
    //ipv4 주소 : 172.30.1.26
    //private static final String BASE_URL = "http://10.0.2.2:5000"; // 로컬 개발환경 에뮬레이터 사용 시
    private static final String BASE_URL = "http://172.30.1.26:5000";

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // OkHttpClient 인스턴스 생성 및 타임아웃 설정
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS) // 연결 타임아웃
                    .readTimeout(60, TimeUnit.SECONDS) // 읽기 타임아웃
                    .writeTimeout(60, TimeUnit.SECONDS) // 쓰기 타임아웃
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 Gson 컨버터 사용
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiInterface() {
        return getRetrofitInstance().create(ApiService.class);
    }}
