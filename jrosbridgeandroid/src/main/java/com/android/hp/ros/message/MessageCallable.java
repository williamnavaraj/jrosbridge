package com.android.hp.ros.message;

public interface MessageCallable<T, S> {
    T call(S var1);
}