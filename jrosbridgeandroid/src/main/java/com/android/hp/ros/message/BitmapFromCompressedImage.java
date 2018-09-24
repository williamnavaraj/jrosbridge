package com.android.hp.ros.message;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.hp.ros.rosbridge.implementation.Base64;

import java.io.ByteArrayInputStream;

public class BitmapFromCompressedImage implements MessageCallable<Bitmap, CompressedImage> {

    public BitmapFromCompressedImage() {}

    public Bitmap call(CompressedImage message) {
        return saveToInternalStorage(message.data);
    }

    private Bitmap saveToInternalStorage(String base64) {
        byte[] byteArray = Base64.decode(base64);
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        return BitmapFactory.decodeStream(arrayInputStream);
    }

}