package _temp;

import java.io.IOException;

import gtu.binary.Base64JdkUtil;

public class Test43 {

    public static void main(String[] args) throws InterruptedException, IOException {
        String from = "bi4gW+akjV1b55qu6IKkXSDojajpurvvvJvorqjkurrlq4zlpbPlo6vvvJvliLrojYnvvIhuZXR0bGXnmoTlpI3mlbDlvaLlvI/vvIkgdnQuIOaDueaAku+8m+S9v+aBvOeBq++8m+eUqOiNqOm6u+WIuu+8iG5ldHRsZeeahOS4ieWNleW9ouW8j++8iSBuLiAoTmV0dGxlcynkurrlkI3vvJso6IuxKeWGheeJueWwlOaWrw\\=\\=";
        String v = Base64JdkUtil.decode(from);
        System.out.println("--" + v);
        System.out.println("done...");
    }
}
