package com.example.capstonefinaldiary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.capstonefinaldiary.Models.AudioFileInfo;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class WeekFragment extends Fragment {
    View view;
    private PieChart weekPieChart;
    public WeekFragment() {
        // Required empty public constructor
    }


    public static WeekFragment newInstance() {
        WeekFragment weekFragment = new WeekFragment();
        return weekFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.week_fragment, container, false);
        weekPieChart = view.findViewById(R.id.weekPieChart);
        fetchEmotionData(); // 데이터를 가져와 원그래프를 표시합니다.
        return view;
    }
    private void fetchEmotionData() {
        // Firebase 데이터베이스의 참조를 가져옵니다.
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("audios");

        // 현재 날짜로부터 해당 주의 시작(일요일)과 끝(토요일) 날짜를 구합니다.
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        // 일요일로 설정
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String startOfWeek = sdf.format(calendar.getTime());

        // 토요일로 설정
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        String endOfWeek = sdf.format(calendar.getTime());

        // 해당 주의 데이터를 조회합니다.
        databaseRef.orderByChild("recordTime").startAt(startOfWeek).endAt(endOfWeek + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 감정 데이터를 저장할 배열을 초기화합니다.
                int[] emotionCounts = new int[7]; // 7가지 감정에 대한 카운트

                // 데이터 스냅샷을 순회하면서 감정 데이터를 카운트합니다.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AudioFileInfo audioFile = snapshot.getValue(AudioFileInfo.class);
                    if (audioFile != null && audioFile.getEmotion() != null) {
                        int emotionIndex = audioFile.getEmotion();
                        emotionCounts[emotionIndex]++;
                    }
                }

                // 원그래프에 데이터를 표시합니다.
                displayPieChart(emotionCounts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터를 가져오는 중 에러가 발생한 경우
                Log.e("StatisticsActivity", "Database error", databaseError.toException());
            }
        });
    }

    private void displayPieChart(int[] emotionCounts) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // 각 감정에 대한 엔트리를 추가합니다.
        for (int i = 0; i < emotionCounts.length; i++) {
            if (emotionCounts[i] > 0) { // 횟수가 0보다 큰 감정만 추가
                entries.add(new PieEntry(emotionCounts[i], "Emotion " + i));
                Log.d("StatisticsActivity", "Emotion " + i + " count: " + emotionCounts[i]);
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Emotion Distribution");
        // + 데이터셋 스타일링 (예: 색상 설정)
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS); // 색상 설정
        PieData data = new PieData(dataSet);
        weekPieChart.setData(data);
        weekPieChart.setDescription(null); // 설명 텍스트 제거
        weekPieChart.animateY(1400, Easing.EaseInOutQuad); // Y축 기준 애니메이션 적용
        weekPieChart.invalidate(); // 차트를 갱신합니다.
    }
}
