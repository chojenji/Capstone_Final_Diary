package com.example.capstonefinaldiary;

import android.widget.ImageView;

public class EmotionImageUtils {
    // 감정 값에 따라 이미지 리소스 ID를 반환하는 메서드
    public static int getEmotionImageResource(Integer emotion) {
        if (emotion == null) return R.drawable.default_emotion; // 감정 값이 없는 경우 기본 이미지
        switch (emotion) {
            case 0: return R.drawable.angry;
            case 1: return R.drawable.anxious;
            case 2: return R.drawable.embarrassed;
            case 3: return R.drawable.sad;
            case 4: return R.drawable.happy;
            case 5: return R.drawable.hurt;
            case 6: return R.drawable.neutrality;
            default: return R.drawable.default_emotion; // 감정 값이 인식할 수 없는 경우
        }
    }

    // ImageView에 감정 이미지를 설정하는 메서드
    public static void setEmotionImage(ImageView imageView, Integer emotion) {
        int imageResId = getEmotionImageResource(emotion);
        imageView.setImageResource(imageResId);
    }
}
