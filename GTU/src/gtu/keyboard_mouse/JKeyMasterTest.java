package gtu.keyboard_mouse;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.MediaKey;
import com.tulskiy.keymaster.common.Provider;

public class JKeyMasterTest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Provider provider = Provider.getCurrentProvider(true);

        provider.register(MediaKey.MEDIA_STOP, new HotKeyListener() {
            @Override
            public void onHotKey(HotKey arg0) {
            }
        });

    }

}
