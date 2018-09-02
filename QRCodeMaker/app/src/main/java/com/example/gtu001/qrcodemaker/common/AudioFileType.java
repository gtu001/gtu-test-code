package com.example.gtu001.qrcodemaker.common;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by gtu001 on 2017/12/13.
 */

public enum AudioFileType {

    MP3(".mp3"),
    _3gp(".3gp"),
    AMR(".amr"),

    _669(".669"),//Composer669module,UNISComposermodule

    ST(".8st"),//8VoicesSoundTracker

    AAC(".aac"),//Advancedaudiocoding

    ABK(".abk"),//AmosMusicBank

    AC1D(".ac1d"),//CompactModFile

    ADSC(".adsc"),//AudioSculpture

    AHX(".ahx"),//AHXv2

    AIFC(".aifc"),//CompressedAudioInterchangeFormatFile

    AIFF(".aiff"),//AudioInterchangeFormatFile

    AIS(".ais"),//VelvetStudioInstrument,Akaisamplerdiskandfileformats

    AKP(".akp"),//AkaiS5000/S6000ProgramFile

    ALAW(".alaw"),//RawA-lawdata

    AMAD(".amad"),//Amadeus

    AMC(".amc"),//A.M.Composer1.2

    AMS(".ams"),//ExtremeTrackerModule VelvetStudioModule

    AON(".aon"),//ArtOfNoise

    APEX(".apex"),//AVMSampleStudiobank

    APE(".ape"),//Monkey'sAudio(losslessaudiocompressioncodec)

    APS(".aps"),//AProSys

    ASE(".ase"),//VelvetStudioSample

    ASF(".asf"),//MicrosoftAdvancedStreamingFormat

    AST(".ast"),//ActionAmics

    ASX(".asx"),//MicrosoftAdvancedStreamingFormatMetafile

    AU(".au"),//Sun/NextAudioFile(linearm-laworA-law)

    AVI(".avi"),//MicrosoftAudioVideoInterleaveFile

    AVR(".avr"),//AudioVisualResearchsoundfile

    AY(".ay"),//AYChipSound

    BEN(".ben"),//BenReplay

    BP(".bp"),//SoundMon2.0

    C01(".c01"),//Typhoonwavefile

    CDA(".cda"),//CDAudioTrack

    CDR(".cdr"),//RawAudio-CDdata

    CHAN(".chan"),//ChannelPlayer1,2,3

    CMF(".cmf"),//CreativeLabsMusicFile

    CRB(".crb"),//HeatSeekermc1.0

    CUST(".cust"),//CustomPlay

    DBM(".dbm"),//DigiBoosterPro

    DCM(".dcm"),//DCMModule

    DEWF(".dewf"),//MacintoshSoundCap/SoundEditrecorderinstrument

    DF2(".df2"),//Defractor2ExtendedInstrument

    DFC(".dfc"),//DefractorInstrument

    DI(".di"),//DigitalIllusions

    DIG(".dig"),//Digilinkformat SoundDesignerIaudio

    DIGI(".digi"),//DigiBooster1.x

    DLS(".dls"),//DownloadableSoundsSpec

    DM1(".dm1"),//DeltaMusic1.0

    DMF(".dmf"),//DelusionDigitalMusicFile

    DDMS(".ddms"),//X-Tracker

    DMU(".dmu"),//DigitalMugician

    DNS(".dns"),//DynamicSynthesizer

    DSF(".dsf"),//DelusionDigitalSoundFile

    DSM(".dsm"),//DigitalSoundmodule

    DSP(".dsp"),//DynamicStudioProfessionalmodule

    DSS(".dss"),//DSS

    DTM(".dtm"),//DigiTrekkermodule

    DTS(".dts"),//DigitalTheatreSystem

    DW(".dw"),//Whittaker

    DWD(".dwd"),//DiamondWareDigitizedaudio

    EA(".ea"),//EarAche
    ;

    final String subname;

    AudioFileType(String subname) {
        this.subname = subname;
    }

    public static boolean isAudioFileType(String name) {
        name = StringUtils.trimToEmpty(name.toLowerCase());
        for (AudioFileType e : AudioFileType.values()) {
            if (name.endsWith(e.subname)) {
                return true;
            }
        }
        return false;
    }
}
