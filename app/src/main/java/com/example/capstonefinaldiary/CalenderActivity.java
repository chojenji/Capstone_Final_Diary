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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstonefinaldiary.Models.AudioFileInfo;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

public class CalenderActivity extends AppCompatActivity {

    private String readDay = null;
    private CalendarView calendarView;
    private ImageButton record;
    private ImageView emotion_img;
    private TextView diary;
    private MenuActivity menuActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        calendarView = findViewById(R.id.calendarView);
        menuActivity = new MenuActivity(this);
        record = findViewById(R.id.record);
        emotion_img = findViewById(R.id.emotion_img);
        diary = findViewById(R.id.diary);

        // 현재 날짜 가져오기
        Calendar currentDate = Calendar.getInstance();
        fetchAudioForDate(currentDate);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                fetchAudioForDate(selectedDate);

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

    private void fetchAudioForDate(Calendar calendar) {
        String selectedDate = String.format(Locale.getDefault(), "%04d%02d%02d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("audios");
        databaseRef.orderByChild("recordTime").startAt(selectedDate).endAt(selectedDate + "\uf8ff")
                .limitToLast(1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DataSnapshot latestSnapshot = dataSnapshot.getChildren().iterator().next();
                            AudioFileInfo audioFile = latestSnapshot.getValue(AudioFileInfo.class);
                            if (audioFile != null) {
                                diary.setText(audioFile.getFilename());
                                 // 감정 데이터가 있는지 확인하고, 있다면 이미지를 업데이트합니다.
                                EmotionImageUtils.setEmotionImage(emotion_img, audioFile.getEmotion());
                            } else {
                                    // 감정 데이터가 없을 경우 기본 이미지를 설정합니다.
                                    emotion_img.setImageResource(R.drawable.default_emotion);
                            }
                        }else {
                            diary.setText("녹음된 일기가 없습니다.");
                            emotion_img.setImageResource(R.drawable.default_emotion); // 기본 이모지
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(CalenderActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}