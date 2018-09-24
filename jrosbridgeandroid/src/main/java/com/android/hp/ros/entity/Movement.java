package com.android.hp.ros.entity;

import com.android.hp.ros.message.Message;
import com.android.hp.ros.message.MessageType;

@MessageType(string = "geometry_msgs/Twist")
public class Movement extends Message {
    public Linear linear;
    public Angular angular;
}