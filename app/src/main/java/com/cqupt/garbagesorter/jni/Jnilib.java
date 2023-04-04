package com.cqupt.garbagesorter.jni;

public class Jnilib {
    public Jnilib() {
    }

    static  { // 导入的lib名去掉前面的lib
        System.loadLibrary( "hello.cp39-win_amd64" );
    }
public static native void say_hello(String message);
}
