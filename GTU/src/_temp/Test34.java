package _temp;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;

import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notification.Level;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.SendNotification;
import gtu.image.ImageUtil;

public class Test34 {

    public static void main(String[] args) throws MalformedURLException {
        Image image = ImageUtil.getInstance().getImageAutoChoice("resource/images/ico/janna.ico");
        
        BufferedImage bImage  = ImageUtil.getInstance().toBufferedImage(image);
        URL url = new URL("resource/images/ico/janna.ico");
        Icon icon = Icon.create(url, "id");
        
        Application app = Application.builder().id("id").icon(icon).name("name").build();
        
        Notifier notifier = new SendNotification()//
                .setApplication(app)//
                .initNotifier();
        
        Notification notification = Notification.builder().level(Level.INFO).icon(icon).message("xxxxxxx").subtitle("subtitle").title("title").build();
        
        try {
            notifier.send(notification);
        }finally {
            notifier.close();
        }
    }

}
