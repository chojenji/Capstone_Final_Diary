package com.example.capstonefinaldiary;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** Retrofit을 초기화하고 Flask 서버로 요청을 보내는 코드*/
public class RetrofitClient {
    // Flask 서버가 실행 중인 주소와 포트
    //private static final String BASE_URL = "http://127.0.0.1:5000"; // Flask 서버 주소
    //ipv4 주소 : 172.30.1.26
    private static final String BASE_URL = "http://10.0.2.2:5000"; // 로컬 개발환경 에뮬레이터 사용 시
    //private static final String BASE_URL = "http://172.30.1.26:5000";

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 Gson 컨버터 사용
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiInterface() {
        return getRetrofitInstance().create(ApiService.class);
    }
}
