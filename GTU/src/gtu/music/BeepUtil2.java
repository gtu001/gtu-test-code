package gtu.music;

import com.sun.jna.Native;

public class BeepUtil2 {

    public static void main(String[] args) {
        Kernel32 kernel32 = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);
        kernel32.Beep(500, 300);
        System.out.println("done...");
    }

    private interface Kernel32 extends com.sun.jna.platform.win32.Kernel32 {
        /**
         * Generates simple tones on the speaker. The function is synchronous;
         * it performs an alertable wait and does not return control to its
         * caller until the sound finishes.
         * 
         * @param dwFreq
         *            : The frequency of the sound, in hertz. This parameter
         *            must be in the range 37 through 32,767 (0x25 through
         *            0x7FFF).
         * @param dwDuration
         *            : The duration of the sound, in milliseconds.
         */
        public abstract void Beep(int dwFreq, int dwDuration);
    }
}
