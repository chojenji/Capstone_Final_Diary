package com.example.capstonefinaldiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstonefinaldiary.Models.AudioFileInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MusicAdapter musicAdapter;
    private List<PlaylistItem> playlistItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        recyclerView = findViewById(R.id.musicRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        musicAdapter = new MusicAdapter(this, playlistItems);
        recyclerView.setAdapter(musicAdapter);


        // 화면이 생성될 때 플레이리스트 데이터 가져오기
        //fetchPlaylist();
        // Intent에서 플레이리스트 데이터를 가져옵니다.
        ArrayList<PlaylistItem> items = getIntent().getParcelableArrayListExtra("playlist");
        if (items != null) {
            playlistItems.clear();
            playlistItems.addAll(items);
            musicAdapter.notifyDataSetChanged();
        }
    }

    private void fetchPlaylist() {
        ApiService apiService = RetrofitClient.getApiInterface();

        Call<List<PlaylistItem>> call = apiService.getPlaylist();
        call.enqueue(new Callback<List<PlaylistItem>>() {
            @Override
            public void onResponse(Call<List<PlaylistItem>> call, Response<List<PlaylistItem>> response) {
                if (response.isSuccessful()) {
                    playlistItems.clear();
                    playlistItems.addAll(response.body());
                    musicAdapter.notifyDataSetChanged();
                } else {
                    // 서버 응답이 에러일 경우의 처리
                    Toast.makeText(getApplicationContext(), "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PlaylistItem>> call, Throwable t) {
                // 네트워크 요청 실패 시의 처리
                Toast.makeText(getApplicationContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
