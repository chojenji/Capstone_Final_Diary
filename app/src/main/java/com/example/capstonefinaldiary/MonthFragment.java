package com.example.capstonefinaldiary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.capstonefinaldiary.Models.AudioFileInfo;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private TextView tv_month, month;
    private ImageButton btnPreviousMonth, btnNextMonth;
    private Calendar currentCalendar = Calendar.getInstance();

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
        tv_month = view.findViewById(R.id.tv_month);
        month = view.findViewById(R.id.month);
        btnPreviousMonth = view.findViewById(R.id.btn_previous_month);
        btnNextMonth = view.findViewById(R.id.btn_next_month);

        btnPreviousMonth.setOnClickListener(v -> changeMonth(-1));
        btnNextMonth.setOnClickListener(v -> changeMonth(1));

        fetchEmotionData();
        return view;
    }

    private void fetchEmotionData() {
        // Firebase 데이터베이스의 참조를 가져옵니다.
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("audios");

        // 현재 날짜로부터 해당 달의 첫날과 마지막 날을 구합니다.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        // 달의 첫날로 설정
        currentCalendar.set(Calendar.DAY_OF_MONTH, 1);
        String startOfMonth = sdf.format(currentCalendar.getTime());

        // 달의 마지막 날로 설정
        currentCalendar.set(Calendar.DAY_OF_MONTH, currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endOfMonth = sdf.format(currentCalendar.getTime());

        // 해당 달의 데이터를 조회합니다.
        databaseRef.orderByChild("recordTime").startAt(startOfMonth).endAt(endOfMonth + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int[] emotionCounts = new int[7]; // 7가지 감정에 대한 카운트
                        int maxEmotionIndex = -1;
                        int maxEmotionValue = 0;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            AudioFileInfo audioFile = snapshot.getValue(AudioFileInfo.class);
                            if (audioFile != null && audioFile.getEmotion() != null) {
                                int emotionIndex = audioFile.getEmotion();
                                emotionCounts[emotionIndex]++;
                                // 최대 감정 값 업데이트
                                if (emotionCounts[emotionIndex] > maxEmotionValue) {
                                    maxEmotionValue = emotionCounts[emotionIndex];
                                    maxEmotionIndex = emotionIndex;
                                }
                            }
                        }

                        // 원그래프에 데이터를 표시합니다.
                        displayPieChart(emotionCounts);
                        // updateMonthTextView를 여기서 호출합니다.
                        updateMonthTextView(maxEmotionIndex);
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
    private void changeMonth(int amount) {
        currentCalendar.add(Calendar.MONTH, amount);
        fetchEmotionData();
    }

    private void updateMonthTextView(int maxEmotionIndex) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월", Locale.getDefault());
        month.setText(sdf.format(currentCalendar.getTime()));

        String emotion = getEmotionText(maxEmotionIndex); // 이 메서드는 감정 인덱스를 감정 텍스트로 변환합니다.

        // 현재 로그인한 사용자의 이름을 가져와서 텍스트 뷰에 설정
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userName = currentUser.getDisplayName();
            String userText = (userName != null && !userName.isEmpty()) ? userName + "님의 " : "사용자의 ";
            tv_month.setText(String.format(Locale.getDefault(), "%s 감정은 \n%s입니다.", userText, emotion));
        }
    }
    private String getEmotionText(int emotionIndex) {
        String[] emotions = {"행복", "슬픔", "분노", "놀람", "공포", "혐오", "중립"};
        if (emotionIndex >= 0 && emotionIndex < emotions.length) {
            return emotions[emotionIndex];
        } else {
            return "알 수 없는 감정";
        }
    }
}
