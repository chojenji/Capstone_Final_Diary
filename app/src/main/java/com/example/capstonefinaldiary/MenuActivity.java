package com.example.capstonefinaldiary;

import android.content.Intent;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

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

        //navigationView = activity.findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_setting) {
                    startNewActivity(SettingActivity.class);
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
        //Toast.makeText(activity, cls.getSimpleName() + " 시작", Toast.LENGTH_SHORT).show();
    }



}