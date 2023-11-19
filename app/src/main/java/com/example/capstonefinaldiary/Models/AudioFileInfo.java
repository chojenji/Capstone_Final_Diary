package com.example.capstonefinaldiary.Models;

public class AudioFileInfo {

    private String filename; // 오디오 파일 이름
    private String url;      // 오디오 파일 URL
    private String recordTime; // 오디오 파일 저장 날짜
    // 월 정보를 추가합니다. 예: "202311"
    private String monthYear;
    private Integer emotion;
    /**
    // 기본 생성자 (필요한 경우 생성자 추가 가능)
   public  AudioFileInfo(String filename, String url){
       this.filename = filename;
       this.url = url;
   }
    */
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getRecordTime() {
        return recordTime;
    }
    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }
    public Integer getEmotion(){return emotion;}
    public void setEmotion(Integer emotion){
        this.emotion = emotion;
    }

}
