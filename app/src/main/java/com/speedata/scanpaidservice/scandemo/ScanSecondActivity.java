package com.speedata.scanpaidservice.scandemo;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.honeywell.barcode.HSMDecodeResult;
import com.honeywell.plugins.decode.DecodeResultListener;
import com.speedata.scanpaidservice.R;

import java.io.UnsupportedEncodingException;

public class ScanSecondActivity extends AppCompatActivity implements DecodeResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_second);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onHSMDecodeResult(HSMDecodeResult[] hsmDecodeResults) {
        try {
            String decodeDate = new String(hsmDecodeResults[0].getBarcodeDataBytes(),
                    "utf8");
            Toast.makeText(this, decodeDate, Toast.LENGTH_SHORT).show();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (HSMDecode.getInstance().getHsmDecoder() != null) {
            HSMDecode.getInstance().getHsmDecoder().removeResultListener(this);
            HSMDecode.getInstance().getHsmDecoder().releaseCameraConnection();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        HSMDecode.getInstance().setHsmDecoder(this);
        if (HSMDecode.getInstance().getHsmDecoder() != null) {
            HSMDecode.getInstance().getHsmDecoder().addResultListener(this);
            HSMDecode.getInstance().getHsmDecoder().initCameraConnection();
            Camera camera = HSMDecode.getInstance().getHsmDecoder().getCamera();
            Camera.Parameters parameters = camera.getParameters();
            parameters.set("iso-speed", "auto");
            camera.setParameters(parameters);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
