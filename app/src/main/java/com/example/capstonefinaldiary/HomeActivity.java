package com.example.capstonefinaldiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// 로그인 성공 후 화면
public class HomeActivity extends AppCompatActivity {

    private GoogleSignInClient mSignInClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser currentUser;
    private MenuActivity menuActivity;

    ImageView ivProfile;
    TextView tv_Userid;
    TextView tv_Username;

    Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        menuActivity = new MenuActivity(this) ; // MenuActivity 인스턴스 생성

        // Firebase 인증 및 현재 사용자 가져오기
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();

        ivProfile = findViewById(R.id.iv_Profile);
        tv_Userid = findViewById(R.id.tv_Userid);
        tv_Username = findViewById(R.id.tv_Username);
        logoutBtn = findViewById(R.id.logout_Btn);

        if (currentUser != null) {
            String userName = currentUser.getDisplayName();
            String userId = currentUser.getUid();
            Uri profilePicUri = currentUser.getPhotoUrl();

            if(userId != null){
                tv_Userid.setText("UserID : " + userId);
            }

            if (userName != null) {
                // 유저 이름 설정
                tv_Username.setText("UserName : " + userName);
            }

            if (profilePicUri != null) {
                // 프로필 이미지 설정
                Glide.with(this)
                        .load(profilePicUri)
                        .circleCrop()
                        .into(ivProfile);
            }
        }

        // logout 버튼 클릭 시 초기화
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSignInClient.signOut();
                mFirebaseAuth.signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finish();

            }
        });

    }
}