package net.xxhong.rosclient.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;


import com.android.hp.ros.MessageHandler;
import com.android.hp.ros.message.BitmapFromCompressedImage;
import com.android.hp.ros.message.CompressedImage;
import com.android.hp.ros.rosbridge.ROSBridgeClient;

import net.xxhong.rosclient.R;
import net.xxhong.rosclient.RCApplication;

public class VideoActivity extends Activity {

    private ROSBridgeClient client;

    private com.android.hp.ros.Topic<CompressedImage> messageTopic;

    private Panel panel;
    private BitmapFromCompressedImage bmp = new BitmapFromCompressedImage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        panel = findViewById(R.id.surfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        bmp    = new BitmapFromCompressedImage();
        client = ((RCApplication)getApplication()).getRosClient();
        ///usb_cam/image_raw/compressed
        messageTopic = new com.android.hp.ros.Topic<>("/ur5/usbcam/image_raw/compressed", CompressedImage.class, client);
        messageTopic.queue_length = 1;
        messageTopic.subscribe(new MessageHandler<CompressedImage>() {
            @Override
            public void onMessage(CompressedImage message) {
                Bitmap bitmap = bmp.call(message);
                if (bitmap != null) {
                    panel.bmpIcon = bitmap;
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        messageTopic.unsubscribe();
    }

}