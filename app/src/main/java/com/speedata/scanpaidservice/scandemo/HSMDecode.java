package com.speedata.scanpaidservice.scandemo;


import android.content.Context;
import android.graphics.Color;
import android.hardware.Camera;

import com.honeywell.barcode.DecodeManager;
import com.honeywell.barcode.HSMDecoder;
import com.honeywell.plugins.PluginManager;

/**
 * 单例模式 HSMDecoder
 *
 * @author TER
 * @date 2018/1/25
 */

public class HSMDecode {
    private static final Object LOCK = new Object();
    private static HSMDecode hsmDecode;
    private HSMDecoder hsmDecoder;

    public static HSMDecode getInstance() {
        if (hsmDecode == null) {
            synchronized (LOCK) {
                if (hsmDecode == null) {
                    hsmDecode = new HSMDecode();
                }
            }
        }
        return hsmDecode;
    }


    public HSMDecoder getHsmDecoder() {
        return hsmDecoder;
    }

    public void setHsmDecoder(Context context) {
        if (this.hsmDecoder == null) {
            hsmDecoder = HSMDecoder.getInstance(context);
            hsmDecoder.enableSound(true);
            hsmDecoder.enableAimer(true);
            hsmDecoder.setAimerColor(Color.RED);
//            hsmDecoder.setOverlayText("ceshi");
            hsmDecoder.setOverlayTextColor(Color.RED);
        }

    }

    public void removeHsmDecoder() {
        DecodeManager.destroyInstance();
        hsmDecoder = null;
    }

}
