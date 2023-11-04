package com.example.capstonefinaldiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity {

    private GoogleSignInClient mSignInClient;
    private FirebaseAuth mFirebaseAuth;
    private TextView logout;
    private MenuActivity menuActivity;

    ImageView ivProfile;
    TextView tv_Userid;
    TextView tv_Username;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        menuActivity = new MenuActivity(this) ; // MenuActivity 인스턴스 생성

        Intent intent = getIntent();

        String userName = getIntent().getStringExtra("userName");
        String ProfilePic = getIntent().getStringExtra("ProfilePic");

        ivProfile = findViewById(R.id.iv_Profile);
        //tv_Userid = findViewById(R.id.tv_Userid);
        tv_Username = findViewById(R.id.tv_Username);
        logout = findViewById(R.id.logout);

        // 이미지 가져오기
        Glide.with(this)
                .load(ProfilePic)
                .circleCrop()
                .into(ivProfile);

        // 유저 이름
        tv_Username.setText("UserName" + userName);

        // logout 버튼 클릭 시 초기화
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSignInClient.signOut();
                mFirebaseAuth.signOut();
                startActivity(new Intent(SettingActivity.this, MainActivity.class));
                finish();

            }
        });

    }

}