package com.example.capstonefinaldiary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

// Splash 시작 화면 구성
public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);   // activity_splash와 연동

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Splash에서 Main(로그인)으로 이동
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);  // 3초 보여주기
    }
}