package com.example.capstonefinaldiary;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.capstonefinaldiary.Models.AudioFileInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MusicAdapter extends RecyclerView.Adapter {

    //리사이클러뷰에 넣을 데이터 리스트
    private List<PlaylistItem> playlistItems;
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
    public MusicAdapter(Context context, List<PlaylistItem> playlistItems) {
        this.context = context;
        this.playlistItems = playlistItems;
    }

    @Override
    public int getItemCount(){
        return playlistItems.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //자신이 만든 itemview를 inflate한 다음 뷰홀더 생성
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        //생선된 뷰홀더를 리턴하여 onBindViewHolder에 전달한다.
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder holder1 = (MyViewHolder)holder;
        PlaylistItem item = playlistItems.get(position);
        holder1.title.setText(item.getTitle());
        holder1.artist.setText(item.getArtist());

        // 이미지 로드 및 설정
        Glide.with(context)
                .load(item.getImageUrl())
                .placeholder(R.drawable.baseline_image_24) // 이미지가 로드되기 전에 표시할 이미지
                .error(R.drawable.baseline_hide_image_24) // 이미지 로드 오류 시 표시할 이미지
                .into(((MyViewHolder) holder).album_img);
        //음악 재생 코드 -> 스포티파이로 이동 및 연결
        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trackUri = "spotify:track:" + item.getId(); // URI 생성
                openSpotifyTrack(trackUri); // item.getId()는 해당 트랙의 Spotify URI를 반환
            }
        });
    }

    private void openSpotifyTrack(String trackUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trackUri));
        intent.putExtra(Intent.EXTRA_REFERRER,
                Uri.parse("android-app://" + context.getPackageName()));

        // 스포티파이 앱이 설치되어 있는지 확인
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            // 스포티파이 앱이 없으면 사용자에게 설치하도록 유도
            Toast.makeText(context, "Spotify 앱이 설치되어 있지 않습니다.", Toast.LENGTH_LONG).show();
            // 여기에 스포티파이 앱 설치 페이지로 이동하는 코드를 추가할 수 있습니다.
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, artist;
        public ImageView album_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.music_Title);
            artist = itemView.findViewById(R.id.music_artist);
            album_img = itemView.findViewById(R.id.album_img);

        }
    }
}
