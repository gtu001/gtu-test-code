// IMyAidlInterface.aidl
package com.example.gtu001.qrcodemaker;

// Declare any non-default types here with import statements

interface IUrlPlayerService {

    String startPlay(String name, String url, int currentPosition);

    boolean isPlaying();

    void pauseAndResume();

    void backwardOrBackward(int second);

    String stopPlay();

    boolean isInitDone();

    Map getCurrentBean();

    void setReplayMode(String currentName, int currentPosition, inout List<String> nameLst, inout List<String> pathLst, boolean isRandom);

    void onProgressChange(int percent);

    int getProgressPercent();

    String getProgressTime();

    void stopSelf();

    void start();

    void pause();

    boolean isAlive();
}
