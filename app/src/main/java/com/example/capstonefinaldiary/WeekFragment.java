package com.example.capstonefinaldiary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class WeekFragment extends Fragment {
    View view;
    private PieChart weekPieChart;
    private TextView tv_week;

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
        tv_week = view.findViewById(R.id.tv_week);

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
        int maxEmotionIndex = -1;
        int maxEmotionCount = 0;

        // 각 감정에 대한 엔트리를 추가합니다.
        for (int i = 0; i < emotionCounts.length; i++) {
            if (emotionCounts[i] > 0) { // 횟수가 0보다 큰 감정만 추가
                entries.add(new PieEntry(emotionCounts[i], "Emotion " + i));
                Log.d("StatisticsActivity", "Emotion " + i + " count: " + emotionCounts[i]);
                // 가장 높은 감정 추출
                if (emotionCounts[i] > maxEmotionCount) {
                    maxEmotionCount = emotionCounts[i];
                    maxEmotionIndex = i;
                }
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

        tv_view(maxEmotionIndex);

    }


    private void tv_view(int emotionIndex){
        // FirebaseUser 객체를 가져옵니다.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String emotionText = "Unknown"; // 기본 텍스트

        if (currentUser != null) {
            // 사용자 이름을 TextView에 설정합니다.
            String userName = currentUser.getDisplayName();
            userName = (userName != null && !userName.isEmpty()) ? userName : "사용자";

            // 감정 인덱스에 따라 감정 텍스트를 설정할 수 있습니다.
            // 예를 들면, emotionIndex가 0이면 "행복", 1이면 "슬픔" 등으로 매핑할 수 있습니다.
            emotionText = getEmotionTextByIndex(emotionIndex);

            // 텍스트뷰 업데이트
            tv_week.setText(String.format("이번주 %s님의 감정은 \n%s입니다.", userName, emotionText));
            /**
            if (userName != null && !userName.isEmpty()) {
                tv_week.setText("이번주 " + userName + "님의 감정은 \n00입니다.");
            } else {
                tv_week.setText("이번주 사용자님의 감정은 \n 입니다.");
            }*/
        }
    }
    private String getEmotionTextByIndex(int index) {
        // 인덱스에 따라 해당하는 감정의 이름을 반환합니다.
        // 이 부분은 애플리케이션에서 사용하는 감정 목록에 맞게 수정해야 합니다.
        String[] emotions = {"행복", "슬픔", "분노", "놀람", "공포", "혐오", "중립"};
        return (index >= 0 && index < emotions.length) ? emotions[index] : "알 수 없는 감정";
    }


}
