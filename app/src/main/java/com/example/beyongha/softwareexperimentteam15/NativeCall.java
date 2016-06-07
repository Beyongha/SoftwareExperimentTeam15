package com.example.beyongha.softwareexperimentteam15;


public class NativeCall {

    static {
        System.loadLibrary("ndk");
    }

    public native String string();

}
