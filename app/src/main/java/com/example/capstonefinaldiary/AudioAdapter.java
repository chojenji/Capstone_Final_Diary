package com.example.capstonefinaldiary;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.capstonefinaldiary.Models.AudioFileInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class AudioAdapter extends RecyclerView.Adapter {

    //리사이클러뷰에 넣을 데이터 리스트
    private ArrayList<AudioFileInfo> dataModels;
    // Firebase 오디오 파일 URL을 저장할 리스트
    private Context context;
    // 리스너 객체 참조를 저장하는 변수
    private OnIconClickListener listener = null;

    /**
     * 커스텀 이벤트 리스너
     * 클릭이벤트를 Adapter에서 구현하기에 제약이 있기 때문에 Activity 에서 실행시키기 위해 커스텀 이벤트 리스너를 생성함.
     * 절차
     * 1.커스텀 리스너 인터페이스 정의
     * 2. 리스너 객체를 전달하는 메서드와 전달된 객체를 저장할변수 추가
     * 3. 아이템 클릭 이벤트 핸들러 메스드에서 리스너 객체 메서드 호출
     * 4. 액티비티에서 커스텀 리스너 객체 생성 및 전달(MainActivity.java 에서 audioAdapter.setOnItemClickListener() )
     */
    // 1.커스텀 리스너 인터페이스 정의
    public interface OnIconClickListener {
        void onItemClick(View view, int position);
    }

    // 2. 리스너 객체를 전달하는 메서드와 전달된 객체를 저장할변수 추가
    public void setOnItemClickListener(OnIconClickListener listener) {
        this.listener = listener;
    }

    //생성자를 통하여 데이터 리스트 context를 받음
    public AudioAdapter(Context context, ArrayList<AudioFileInfo> dataModels) {
        this.dataModels = dataModels;
        this.context = context;
    }

    @Override
    public int getItemCount(){
        return dataModels.size();
        //return audioList != null ? audioList.size() : 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //자신이 만든 itemview를 inflate한 다음 뷰홀더 생성
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        //생선된 뷰홀더를 리턴하여 onBindViewHolder에 전달한다.
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AudioFileInfo audioFileInfo = dataModels.get(position);
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        ((MyViewHolder) holder).audioTitle.setText(dataModels.get(position).getFilename());
        // 여기에서 감정 이미지를 설정합니다.
        Integer emotion = audioFileInfo.getEmotion();
        if (emotion != null) {
            int imageResId = EmotionImageUtils.getEmotionImageResource(emotion);
            ((MyViewHolder) holder).emotion_img.setImageResource(imageResId);
        } else {
            // 감정 데이터가 없는 경우 기본 이미지를 사용합니다.
            ((MyViewHolder) holder).emotion_img.setImageResource(R.drawable.default_emotion);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition(); // 인덱스를 동적으로 가져오기
                if (listener != null && pos != RecyclerView.NO_POSITION) {
                    listener.onItemClick(view, pos);
                }
            }
        });
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView emotion_img;
        TextView audioTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            emotion_img = itemView.findViewById(R.id.audiofile_img);
            audioTitle = itemView.findViewById(R.id.audioTitle_itemAudio);

            emotion_img.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //3. 아이템 클릭 이벤트 핸들러 메스드에서 리스너 객체 메서드 호출
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (listener != null) {
                            listener.onItemClick(view, pos) ;
                        }
                    }
                }
            });
        }
    }
}