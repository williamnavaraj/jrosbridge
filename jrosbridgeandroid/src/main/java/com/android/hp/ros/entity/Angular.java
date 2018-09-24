package com.android.hp.ros.entity;

import com.android.hp.ros.message.Message;
import com.android.hp.ros.message.MessageType;

@MessageType(string = "angular")
public class Angular extends Message {
    public float x;
    public float y;
    public float z;
}