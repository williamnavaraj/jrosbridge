package com.android.hp.ros.entity;

import com.android.hp.ros.message.Message;
import com.android.hp.ros.message.MessageType;

@MessageType(string = "std_msgs/String")
public class Msg extends Message {
    public String data;

    // Without this field Ros will complain and message wont be send
    private static final long serialVersionUID = 1L;
}