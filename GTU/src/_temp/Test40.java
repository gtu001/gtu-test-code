package _temp;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

public class Test40 {

    public static void main(String[] args) throws UnsupportedEncodingException {
//        System.out.println(new BigDecimal(-1).compareTo(BigDecimal.ZERO));
        
//        System.out.println(StringUtils.substring("1234567", 0, -2));
        
        
//        System.out.println(URLDecoder.decode("quality=hd720&type=video%2Fmp4%3B+codecs%3D%22avc1.64001F%2C+mp4a.40.2%22&itag=22&url=https%3A%2F%2Fr2---sn-8pcv-u2xz.googlevideo.com%2Fvideoplayback%3Fc%3DWEB%26ipbits%3D0%26fvip%3D2%26ratebypass%3Dyes%26initcwndbps%3D755000%26sparams%3Ddur%252Cei%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Cratebypass%252Crequiressl%252Csource%252Cexpire%26expire%3D1523540410%26mime%3Dvideo%252Fmp4%26itag%3D22%26mn%3Dsn-8pcv-u2xz%252Csn-un57sn76%26mm%3D31%252C29%26mt%3D1523518715%26key%3Dyt6%26signature%3DBC712B8DE3171879B2008D33D458E4DD457DC8EE.62337D09763C4340C96FA9244FAFEEC263483411%26id%3Do-AAOB6EDM6SOw1zbBQ3be9mAVYdEw_ruwXJni5n6x87cv%26requiressl%3Dyes%26ip%3D180.217.89.79%26pl%3D21%26lmt%3D1522841365119199%26dur%3D85.519%26mv%3Dm%26source%3Dyoutube%26ms%3Dau%252Crdu%26ei%3DWg3PWu3RJ4W64gLQ3YjIDg,quality=medium&type=video%2Fwebm%3B+codecs%3D%22vp8.0%2C+vorbis%22&itag=43&url=https%3A%2F%2Fr2---sn-8pcv-u2xz.googlevideo.com%2Fvideoplayback%3Fc%3DWEB%26ipbits%3D0%26clen%3D9564367%26initcwndbps%3D755000%26sparams%3Dclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Cratebypass%252Crequiressl%252Csource%252Cexpire%26mime%3Dvideo%252Fwebm%26mn%3Dsn-8pcv-u2xz%252Csn-un57sn76%26mm%3D31%252C29%26id%3Do-AAOB6EDM6SOw1zbBQ3be9mAVYdEw_ruwXJni5n6x87cv%26ip%3D180.217.89.79%26pl%3D21%26mv%3Dm%26mt%3D1523518715%26ms%3Dau%252Crdu%26ei%3DWg3PWu3RJ4W64gLQ3YjIDg%26fvip%3D2%26ratebypass%3Dyes%26expire%3D1523540410%26itag%3D43%26key%3Dyt6%26signature%3DA425BFF69D132D986816E3D392F94D5690AB8046.CBE9A2AC0BED731F20E5D553E714BAF11CD16BB0%26gir%3Dyes%26requiressl%3Dyes%26dur%3D0.000%26source%3Dyoutube%26lmt%3D1522843345988762,quality=medium&type=video%2Fmp4%3B+codecs%3D%22avc1.42001E%2C+mp4a.40.2%22&itag=18&url=https%3A%2F%2Fr2---sn-8pcv-u2xz.googlevideo.com%2Fvideoplayback%3Fc%3DWEB%26ipbits%3D0%26clen%3D7761644%26initcwndbps%3D755000%26sparams%3Dclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Cratebypass%252Crequiressl%252Csource%252Cexpire%26mime%3Dvideo%252Fmp4%26mn%3Dsn-8pcv-u2xz%252Csn-un57sn76%26mm%3D31%252C29%26id%3Do-AAOB6EDM6SOw1zbBQ3be9mAVYdEw_ruwXJni5n6x87cv%26ip%3D180.217.89.79%26pl%3D21%26mv%3Dm%26mt%3D1523518715%26ms%3Dau%252Crdu%26ei%3DWg3PWu3RJ4W64gLQ3YjIDg%26fvip%3D2%26ratebypass%3Dyes%26expire%3D1523540410%26itag%3D18%26key%3Dyt6%26signature%3DB8855CBFE847D1E205AE6E6700A16981839EFA04.B29638DA7D0A7E23C2121C72CCF2B99252D11BAF%26gir%3Dyes%26requiressl%3Dyes%26dur%3D85.519%26source%3Dyoutube%26lmt%3D1522840898773334,quality=small&type=video%2F3gpp%3B+codecs%3D%22mp4v.20.3%2C+mp4a.40.2%22&itag=36&url=https%3A%2F%2Fr2---sn-8pcv-u2xz.googlevideo.com%2Fvideoplayback%3Fc%3DWEB%26ipbits%3D0%26clen%3D2344798%26initcwndbps%3D755000%26sparams%3Dclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Dvideo%252F3gpp%26mn%3Dsn-8pcv-u2xz%252Csn-un57sn76%26mm%3D31%252C29%26id%3Do-AAOB6EDM6SOw1zbBQ3be9mAVYdEw_ruwXJni5n6x87cv%26ip%3D180.217.89.79%26pl%3D21%26mv%3Dm%26mt%3D1523518715%26ms%3Dau%252Crdu%26ei%3DWg3PWu3RJ4W64gLQ3YjIDg%26fvip%3D2%26expire%3D1523540410%26itag%3D36%26key%3Dyt6%26signature%3D0FDB78E0612253EB82AE71903F2168AF91CB6645.77A134F0E890962CCA80CE14FE7718CB192DAD10%26gir%3Dyes%26requiressl%3Dyes%26dur%3D85.542%26source%3Dyoutube%26lmt%3D1522840895865251,quality=small&type=video%2F3gpp%3B+codecs%3D%22mp4v.20.3%2C+mp4a.40.2%22&itag=17&url=https%3A%2F%2Fr2---sn-8pcv-u2xz.googlevideo.com%2Fvideoplayback%3Fc%3DWEB%26ipbits%3D0%26clen%3D843016%26initcwndbps%3D755000%26sparams%3Dclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Dvideo%252F3gpp%26mn%3Dsn-8pcv-u2xz%252Csn-un57sn76%26mm%3D31%252C29%26id%3Do-AAOB6EDM6SOw1zbBQ3be9mAVYdEw_ruwXJni5n6x87cv%26ip%3D180.217.89.79%26pl%3D21%26mv%3Dm%26mt%3D1523518715%26ms%3Dau%252Crdu%26ei%3DWg3PWu3RJ4W64gLQ3YjIDg%26fvip%3D2%26expire%3D1523540410%26itag%3D17%26key%3Dyt6%26signature%3D3AE286A5E7FB8B35C329F81CF36E676608F90EF1.1C7AB60F3C7D2E00B157DB8FAE282E198DFD5F20%26gir%3Dyes%26requiressl%3Dyes%26dur%3D85.542%26source%3Dyoutube%26lmt%3D1522840891655788", "utf8"));
        
       String urlOk = "https://r2---sn-8pcv-u2xz.googlevideo.com/videoplayback?lmt=1522840898773334&expire=1523537993&mt=1523516281&dur=85.519&mv=m&ms=au%2Crdu&ip=180.217.89.79&clen=7761644&initcwndbps=757500&mm=31%2C29&ipbits=0&c=WEB&mn=sn-8pcv-u2xz%2Csn-un57sn76&gir=yes&requiressl=yes&ei=6QPPWvKJKoq2gQPWrICQDg&itag=18&sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Cratebypass%2Crequiressl%2Csource%2Cexpire&ratebypass=yes&fvip=2&signature=1436718E40C4BB33289931B5607B8537D3B415C7.A4F8E08DFBC5193AD8FE9DF7E8A6F9CAE942FEBC&source=youtube&mime=video%2Fmp4&pl=21&id=o-AExTUwtjYbbC8z3a68Euv6zi5O511pMkgqfLzp90i8db&key=yt6";
       String urlMyself = "https://r1---sn-8pcv-u2x6.googlevideo.com/videoplayback?requiressl=yes&ratebypass=yes&clen=19502529&mime=video%2Fmp4&fvip=1&ipbits=0&mn=sn-8pcv-u2x6%2Csn-un57en7e&signature=99C0134D24734623C4C07862EA68126C03739594.B6EE7ECB510E0C9E7F8EA63C7AA0745C3004F5AF&mm=31%2C29&itag=18&gir=yes&dur=247.408&mv=m&mt=1523644494&ms=au%2Crdu&lmt=1522908981203546&ip=180.217.89.79&ei=7_jQWo3bJ5b94QKsrpXwCA&key=yt6&expire=1523666255&id=o-APAxGNe_1IWdyl9l2yoL6LIasw1zuEFkvki-ysieqiS3&c=WEB&pl=21&initcwndbps=972500&source=youtube&sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Cratebypass%2Crequiressl%2Csource%2Cexpire&&itag=18";
       
       List<NameValuePair> lst1 = parseURL(urlOk.substring(urlOk.indexOf("?")));
       List<NameValuePair> lst2 = parseURL(urlMyself.substring(urlMyself.indexOf("?")));
       
       System.out.println("-------------------------------------");
       lst2NotInLst1(lst1, lst2);
       System.out.println("-------------------------------------");
       lst2NotInLst1(lst2, lst1);
       
       System.out.println("done...");
    }
    
    private static void lst2NotInLst1(List<NameValuePair> lst1, List<NameValuePair> lst2) {
        for(NameValuePair v2 : lst2) {
            boolean findOk = false;
            A : for(NameValuePair v1 : lst1) {
                if(v2.getName().equals(v1.getName())) {
                    findOk = true;
                    break A;
                }
            }
            if(!findOk) {
                System.out.println("no " + v2.getName());
            }
        }
    }

    private static List<NameValuePair> parseURL(String url) {
        List<NameValuePair> infoMap = new ArrayList<NameValuePair>();
        URLEncodedUtils.parse(infoMap, new Scanner(url), "utf8");
        for(NameValuePair pair : infoMap) {
//            System.out.println("\t" + pair.getName() + "\t" + pair.getValue());
        }
        return infoMap;
    }
}
