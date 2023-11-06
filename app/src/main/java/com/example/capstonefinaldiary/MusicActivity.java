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

public class MusicActivity extends AppCompatActivity {

    // 음악 재생 관련 변수
    private MediaPlayer mediaPlayer = null;
    private Boolean isPlaying = false;
    private Boolean isPaused = false;
    SeekBar seekBar;
    private TextView playTimeTextView; //진행시간
    private TextView totalTimeTextView; //완료시간 (오디오 파일 총 길이)
    private int lastPlayedPosition = -1;
    private ImageButton playImageBtn;

    /** 리사이클러뷰
     * 음악 플레이리스트를 받아오는 MusicAdapter 따로 작성필요 */
    private RecyclerView audioRecyclerView;
    private AudioAdapter audioAdapter;
    private ArrayList<String> audioList;

    // 권한 요청 코드 (예: 1)
    private static final int PERMISSION_REQUEST_CODE = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        /**

        // 재생관련 버튼 초기화
        playImageBtn = findViewById(R.id.playImageBtn);
        seekBar = findViewById(R.id.music_seekbar);
        playTimeTextView = findViewById(R.id.play_time_text_view);
        totalTimeTextView = findViewById(R.id.total_time_text_view);

        // 리사이클러뷰 초기화
        audioRecyclerView = findViewById(R.id.musicRecyclerView);
        audioRecyclerView.setHasFixedSize(true); //리사이클러뷰 성능강화

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        audioRecyclerView.setLayoutManager(mLayoutManager);
        audioList = new ArrayList<>(); //오디오 파일정보를 담을 리스트(어댑터쪽으로)

        // Adapter에 데이터가 변경되었음을 알림
        audioAdapter = new AudioAdapter(this, audioList);
        audioRecyclerView.setAdapter(audioAdapter); // 리사이클러뷰 어댑터 연결

        // 변수 초기화
        audioIcon = null;
        isPlaying = false;
        isPaused = false;
        lastPlayedPosition = -1;


        // 커스텀 이벤트 리스너 4. 액티비티에서 커스텀 리스너 객체 생성 및 전달
        audioAdapter.setOnItemClickListener(new AudioAdapter.OnIconClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 오디오 파일의 URL을 가져옴
                String uriName = audioList.get(position).getUrl();

                if (position == lastPlayedPosition) {
                    // 같은 파일을 다시 클릭하면 아무 동작 안 함
                    return;
                }

                // 다른 파일을 클릭시
                stopAudio();

                // 권한 요청
                if(requestStoragePermission()) {
                    playAudio(uriName);
                    lastPlayedPosition = position;
                }
            }
        });

        playImageBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (isPlaying) {
                    pauseAudio();
                }else{
                    // 권한 요청
                    if(requestStoragePermission()) {
                        resumeAudio(); // 일시정지 상태에서 재개
                    }
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //searchView = findViewById(R.id.searchView);
        //filterList = new ArrayList<>();

        // SearchView 리스너 설정


    }

    // 녹음 파일 재생
    private void playAudio(String audioUrl) {
        Log.d("AudioFileActivity", "playAudio() called"); // 디버깅을 위한 로그
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopAudio();
                }
            });
        } else {
            mediaPlayer.reset();
        }
        try {
            // 오디오 속성 설정
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build();
            mediaPlayer.setAudioAttributes(audioAttributes);
            // Firebase Storage에서 오디오 파일 다운로드
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync(); // 비동기로 준비 시작
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    isPlaying = true;
                    isPaused = false;
                    // 재생관련 버튼 가시성 (UI)
                    playImageBtn.setImageResource(R.drawable.baseline_pause_24);
                    seekBar.setMax(mp.getDuration());
                    updateSeekBar();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("AudioFileActivity", "playAudio() not called"); // 디버깅을 위한 로그
        }
    }
    //녹음 재개
    private void resumeAudio() {
        // 일시정지 상태에서 재생버튼 클릭 시 재생 재개
        if (mediaPlayer != null && isPaused) {
            mediaPlayer.start();
            isPlaying = true;
            isPaused = false;
            // 재생관련 버튼 가시성 (UI)
            playImageBtn.setImageResource(R.drawable.baseline_pause_24);
            Log.d("AudioFileActivity", "resumeAudio() called"); // 디버깅을 위한 로그
            updateSeekBar();
        }
    }

    // 녹음 파일 일시정지
    private  void pauseAudio(){
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
            isPaused = true;
            // 재생관련 버튼 가시성 (UI)
            playImageBtn.setImageResource(R.drawable.baseline_play_arrow_24);
            updateSeekBar();
        }
    }


    // 녹음 파일 중지
    private void stopAudio() {
        if (mediaPlayer != null && (isPlaying || !mediaPlayer.isPlaying())) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
            playImageBtn.setImageResource(R.drawable.baseline_play_arrow_24);
            seekBar.setProgress(0);
            playTimeTextView.setText("00:00");
            totalTimeTextView.setText("00:00");
        }

    }
    private void updateSeekBar() {
        if (mediaPlayer != null) {
            final int duration = mediaPlayer.getDuration();
            seekBar.setMax(duration);
            totalTimeTextView.setText(formatTime(duration));
            //playTimeTextView.setText(formatTime(0));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isPlaying) {
                        try {
                            int currentPosition = mediaPlayer.getCurrentPosition();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    seekBar.setProgress(currentPosition);
                                    playTimeTextView.setText(formatTime(currentPosition));
                                }
                            });
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    private String formatTime(int millis) {
        int seconds = (millis / 1000) % 60;
        int minutes = (millis / (1000 * 60)) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    // 권한 요청 메서드
    private boolean requestStoragePermission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // 사용자에게 권한에 대한 설명을 보여줄 필요가 있는 경우
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("권한 요청");
                builder.setMessage("오디오 파일을 읽기 위해서는 권한이 필요합니다. 권한을 부여해주세요.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 사용자에게 권한 요청 다이얼로그를 표시
                        ActivityCompat.requestPermissions(MusicActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 권한을 부여하지 않을 경우 처리
                        Toast.makeText(MusicActivity.this, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            } else {
                // 권한 요청 다이얼로그를 표시
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
            return false;
        }
    }

    // 권한 요청 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 액티비티 종료 시 MediaPlayer 해제
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    */
    }
}
