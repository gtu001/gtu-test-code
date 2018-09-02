package gtu.amazon.polly;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.DescribeVoicesRequest;
import com.amazonaws.services.polly.model.DescribeVoicesResult;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.Voice;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

/**
 * 亞馬遜雲端產生Mp3檔案
 */
public class PollyDemo {

    public static void main(String args[]) throws Exception {
        System.out.println("done...");
    }

    private final AmazonPollyClient polly;
    private Voice voice;
    private static final String SAMPLE = "Congratulations. You have successfully built this working demo of Amazon Polly in Java. Have fun building voice enabled apps with Amazon Polly (that's me!), and always look at the AWS website for tips and tricks on using Amazon Polly and other great services from AWS";
    private java.util.List<Voice> voices;
    private String sampleRate;

    public enum SampleRate {
        rate_8000("8000"), //
        rate_16000("16000"), //
        rate_22050("22050"),//
        ;
        final String sampleRate;

        SampleRate(String sampleRate) {
            this.sampleRate = sampleRate;
        }

        public String getValue() {
            return sampleRate;
        }
    }

    public enum SpeechMarkTypes {
        sentence, ssml, viseme, word;

        SpeechMarkTypes() {
        }

        public static String[] getAll() {
            List<String> lst = new ArrayList<String>();
            for (SpeechMarkTypes e : SpeechMarkTypes.values()) {
                lst.add(e.name());
            }
            return lst.toArray(new String[0]);
        }
    }

    private PollyDemo(Region region, String accessKeyId, String secretAccessKey) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);

        // create an Amazon Polly client in a specific region
        // polly = new AmazonPollyClient(new
        // DefaultAWSCredentialsProviderChain(), new ClientConfiguration());
        polly = new AmazonPollyClient(credentials, new ClientConfiguration());
        polly.setRegion(region);
        // Create describe voices request.
        DescribeVoicesRequest describeVoicesRequest = new DescribeVoicesRequest();

        // Synchronously ask Amazon Polly to describe available TTS voices.
        DescribeVoicesResult describeVoicesResult = polly.describeVoices(describeVoicesRequest);

        voices = describeVoicesResult.getVoices();
        voice = describeVoicesResult.getVoices().get(0);

        sampleRate = SampleRate.rate_22050.getValue();
    }

    public java.util.List<Voice> getVoices() {
        return voices;
    }

    public void setVoice(Voice voice) {
        System.out.println("setVoice : " + voice);
        this.voice = voice;
    }

    public void setSampleRate(String sampleRate) {
        System.out.println("setSampleRate : " + sampleRate);
        this.sampleRate = sampleRate;
    }

    public InputStream synthesize(String text) throws IOException {
        OutputFormat format = OutputFormat.Mp3;
        SynthesizeSpeechRequest synthReq = new SynthesizeSpeechRequest()
                // .withSpeechMarkTypes(SpeechMarkTypes.getAll())//
                .withSampleRate(sampleRate)//
                .withText(text)//
                .withVoiceId(voice.getId())//
                .withOutputFormat(format);//
        SynthesizeSpeechResult synthRes = polly.synthesizeSpeech(synthReq);
        return synthRes.getAudioStream();
    }

    public void playMp3(InputStream speechStream) throws JavaLayerException {
        AdvancedPlayer player = new AdvancedPlayer(speechStream, javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());
        player.setPlayBackListener(new PlaybackListener() {
            @Override
            public void playbackStarted(PlaybackEvent evt) {
                System.out.println("Playback started");
                System.out.println(SAMPLE);
            }

            @Override
            public void playbackFinished(PlaybackEvent evt) {
                System.out.println("Playback finished");
            }
        });
        player.play();
    }

    public void exportMp3File(InputStream speechStream, File destFile) throws IOException {
        byte[] arrayOfByte = new byte[4096];
        BufferedInputStream input = new BufferedInputStream(speechStream);
        FileOutputStream baos = new FileOutputStream(destFile);
        int i;
        while ((i = input.read(arrayOfByte, 0, arrayOfByte.length)) != -1) {
            baos.write(arrayOfByte, 0, i);
        }
        baos.close();
        input.close();
    }

    public static PollyDemo newInstance(String accessKeyId, String secretAccessKey) {
        PollyDemo helloWorld = new PollyDemo(Region.getRegion(Regions.US_EAST_1), accessKeyId, secretAccessKey);
        return helloWorld;
    }
}