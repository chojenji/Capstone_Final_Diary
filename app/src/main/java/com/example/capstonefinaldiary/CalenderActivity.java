package com.example.capstonefinaldiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class CalenderActivity extends AppCompatActivity {

    private String readDay = null;
    private CalendarView calendarView;
    private MenuActivity menuActivity;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        calendarView = findViewById(R.id.calendarView);
        //menuActivity = new MenuActivity(this, navigationView); // MenuActivity 인스턴스 생성
        settingSideNavBar();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(CalenderActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void settingSideNavBar(){
        /** 메뉴바 */
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navigation);
        drawerLayout = findViewById(R.id.drawer);
        // 사이드 메뉴를 오픈하기위한 아이콘 추가
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                CalenderActivity.this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        // 사이드 네브바 클릭 리스너
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // -> 사이드 네브바 아이템 클릭 이벤트 설정
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_setting) {
                    Intent intent = new Intent(CalenderActivity.this, SettingActivity.class);
                    startActivity(intent);
                    Log.d("Activity", "설정액티비티");
                } else if (id == R.id.menu_statistics) {
                    Intent intent = new Intent(CalenderActivity.this, StatisticsActivity.class);
                    startActivity(intent);
                } else if (id == R.id.menu_file) {
                    Intent intent = new Intent(CalenderActivity.this, AudioFileActivity.class);
                    startActivity(intent);
                } else if (id == R.id.menu_music) {
                    Intent intent = new Intent(CalenderActivity.this, MusicActivity.class);
                    startActivity(intent);
                }

                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}