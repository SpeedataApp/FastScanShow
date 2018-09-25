package com.speedata.scanpaidservice.scandemo;


import android.content.Context;

import com.honeywell.camera.CameraManager;

/**
 * 单例模式 CameraManager
 * @author TER
 * @date 2018/1/25
 */

public class CameraManage {
    private static class Holder {
        private static CameraManage cameraManage = new CameraManage();
    }

    private CameraManage(){}

    public static CameraManage getInstance(){
        return Holder.cameraManage;
    }


    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public void setCameraManager(Context context) {
        if (this.cameraManager ==null){
            cameraManager = CameraManager.getInstance(context);
        }

    }

    private CameraManager cameraManager;
}
