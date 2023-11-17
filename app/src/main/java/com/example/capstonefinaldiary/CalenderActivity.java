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
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstonefinaldiary.Models.AudioFileInfo;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class CalenderActivity extends AppCompatActivity {

    private String readDay = null;
    private CalendarView calendarView;
    private Button record;
    private MenuActivity menuActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        calendarView = findViewById(R.id.calendarView);
        menuActivity = new MenuActivity(this); // MenuActivity 인스턴스 생성
        record = findViewById(R.id.record);
        /**
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(CalenderActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });*/
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // 날짜 형식을 맞추기 위해 month + 1 을 해줌, month는 0부터 시작하기 때문
                String selectedDate = String.format(Locale.getDefault(), "%04d%02d%02d", year, month + 1, dayOfMonth);

                // Firebase Realtime Database에서 해당 날짜의 녹음 파일 메타데이터 검색
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("audios");
                databaseRef.orderByChild("recordTime").startAt(selectedDate).endAt(selectedDate + "\uf8ff")
                        .limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // 최신 녹음 파일의 데이터스냅샷을 가져옴
                            DataSnapshot latestSnapshot = dataSnapshot.getChildren().iterator().next();
                            AudioFileInfo audioFile = latestSnapshot.getValue(AudioFileInfo.class);
                            if (audioFile != null) {
                                TextView diaryTextView = findViewById(R.id.diary);
                                diaryTextView.setText(audioFile.getFilename()); // TextView에 최신 녹음 파일 이름 표시
                            }
                        } else {
                            Toast.makeText(CalenderActivity.this, "No audio found for this date", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(CalenderActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalenderActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });
    }
}