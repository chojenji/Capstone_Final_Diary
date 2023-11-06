package com.example.capstonefinaldiary;

import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity {

    private AppCompatActivity activity;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    public MenuActivity(AppCompatActivity activity) {
        this.activity = activity;
        initializeUI();
    }

    public void initializeUI() {
        toolbar = activity.findViewById(R.id.toolbar);
        drawerLayout = activity.findViewById(R.id.drawer);
        navigationView = activity.findViewById(R.id.navigation);

        // Firebase에서 사용자 정보 가져오기
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

        if (currentUser != null) {
            // 사용자 정보 가져오기
            String userName = currentUser.getDisplayName();
            Uri profilePicUri = currentUser.getPhotoUrl();

            // 헤더 뷰의 ImageView와 TextView에 사용자 정보 설정
            View headerView = navigationView.getHeaderView(0);
            ImageView ivProfile = headerView.findViewById(R.id.iv_Profile);
            TextView tvUsername = headerView.findViewById(R.id.tv_Username);

            if (userName != null) {
                tvUsername.setText(userName);
            }

            if (profilePicUri != null) {
                Glide.with(activity)
                        .load(profilePicUri)
                        .circleCrop()
                        .into(ivProfile);
            }
        }

        setupNavigationDrawer();
    }

    public void setupNavigationDrawer() {
        // 사이드 메뉴를 오픈하기위한 아이콘 추가
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_setting) {
                    startNewActivity(SettingActivity.class);
                } else if (id == R.id.home) {
                    startNewActivity(CalenderActivity.class);
                } else if (id == R.id.menu_statistics) {
                    startNewActivity(StatisticsActivity.class);
                } else if (id == R.id.menu_file) {
                    startNewActivity(AudioFileActivity.class);
                } else if (id == R.id.menu_music) {
                    startNewActivity(MusicActivity.class);
                }

                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleDrawer();
            }
        });
    }

    public void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }
    public void startNewActivity(Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }



}