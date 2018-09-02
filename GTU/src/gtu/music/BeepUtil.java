package gtu.music;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.Timer;

public class BeepUtil {

    public static void main(String[] args) {
        System.out.println("done...");
    }

    /**
     * Beeps. Currently half-assumes that the format the system expects is
     * "PCM_SIGNED unknown sample rate, 16 bit, stereo, 4 bytes/frame,
     * big-endian" I don't know what to do about the sample rate. Using 11025,
     * since that seems to be right, by testing against A440. I also can't
     * figure out why I had to *4 the duration. Also, there's up to about a 100
     * ms delay before the sound starts playing.
     * 
     * @param freq
     * @param millis
     */
    public static void beep(double freq, final double millis) {
        try {
            final Clip clip = AudioSystem.getClip();
            AudioFormat af = clip.getFormat();

            if (af.getSampleSizeInBits() != 16) {
                System.err.println("Weird sample size.  Dunno what to do with it.");
                return;
            }

            // System.out.println("format " + af);

            int bytesPerFrame = af.getFrameSize();
            double fps = 11025;
            int frames = (int) (fps * (millis / 1000));
            frames *= 4; // No idea why it wasn't lasting as long as it should.

            byte[] data = new byte[frames * bytesPerFrame];

            double freqFactor = (Math.PI / 2) * freq / fps;
            double ampFactor = (1 << af.getSampleSizeInBits()) - 1;

            for (int frame = 0; frame < frames; frame++) {
                short sample = (short) (0.5 * ampFactor * Math.sin(frame * freqFactor));
                data[(frame * bytesPerFrame) + 0] = (byte) ((sample >> (1 * 8)) & 0xFF);
                data[(frame * bytesPerFrame) + 1] = (byte) ((sample >> (0 * 8)) & 0xFF);
                data[(frame * bytesPerFrame) + 2] = (byte) ((sample >> (1 * 8)) & 0xFF);
                data[(frame * bytesPerFrame) + 3] = (byte) ((sample >> (0 * 8)) & 0xFF);
            }
            clip.open(af, data, 0, data.length);

            // This is so Clip releases its data line when done. Otherwise at 32
            // clips it breaks.
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == javax.sound.sampled.LineEvent.Type.START) {
                        Timer t = new Timer((int) millis + 1, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                clip.close();
                            }
                        });
                        t.setRepeats(false);
                        t.start();
                    }
                }
            });
            clip.start();
        } catch (LineUnavailableException ex) {
            System.err.println(ex);
        }
    }
}
