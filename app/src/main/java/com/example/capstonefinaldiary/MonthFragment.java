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


public class MonthFragment extends Fragment {
    View view;
    private PieChart monthPieChart;
    public MonthFragment() {
        // Required empty public constructor
    }


    public static MonthFragment newInstance() {
        MonthFragment monthFragment = new MonthFragment();
        return monthFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.month_fragment, container, false);
        monthPieChart = view.findViewById(R.id.monthPieChart);
        fetchEmotionData();
        return view;
    }

    private void fetchEmotionData() {
        // Firebase 데이터베이스의 참조를 가져옵니다.
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("audios");

        // 현재 날짜로부터 해당 달의 첫날과 마지막 날을 구합니다.
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        // 달의 첫날로 설정
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String startOfMonth = sdf.format(calendar.getTime());

        // 달의 마지막 날로 설정
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endOfMonth = sdf.format(calendar.getTime());

        // 해당 달의 데이터를 조회합니다.
        databaseRef.orderByChild("recordTime").startAt(startOfMonth).endAt(endOfMonth + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int[] emotionCounts = new int[7]; // 7가지 감정에 대한 카운트

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
                        Log.e("MonthFragment", "Database error", databaseError.toException());
                    }
                });
    }

    private void displayPieChart(int[] emotionCounts) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < emotionCounts.length; i++) {
            if (emotionCounts[i] > 0) {
                entries.add(new PieEntry(emotionCounts[i], "Emotion " + i));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Monthly Emotion Distribution");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);
        monthPieChart.setData(data);
        monthPieChart.setDescription(null);
        monthPieChart.animateY(1400, Easing.EaseInOutQuad);
        monthPieChart.invalidate();
    }
}
