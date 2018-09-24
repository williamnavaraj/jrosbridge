package com.android.hp.ros.message;

import com.android.hp.ros.message.Header;
import com.android.hp.ros.message.Message;
import com.android.hp.ros.message.MessageType;

@MessageType(string = "sensor_msgs/CompressedImage")
public class CompressedImage extends Message {
    public Header header;
    public String format;
    public String data;

}