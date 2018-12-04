// IMyAidlInterface.aidl
package com.example.gtu001.qrcodemaker;

// Declare any non-default types here with import statements

interface IUrlPlayerService {

    String startPlay(String url);

    boolean isPlaying();

    void pauseAndResume();

    void backwardOrBackward(int second);

    String stopPlay();
}
