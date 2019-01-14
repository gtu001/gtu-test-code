// IMyAidlInterface.aidl
package com.example.gtu001.qrcodemaker;

// Declare any non-default types here with import statements

interface IUrlPlayerService {

    String startPlay(String name, String url);

    boolean isPlaying();

    void pauseAndResume();

    void backwardOrBackward(int second);

    String stopPlay();

    boolean isInitDone();

    Map getCurrentBean();

    void onMyServiceDestory();
}
