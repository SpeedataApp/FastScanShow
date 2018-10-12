package com.speedata.scanpaidservice.scandemo;

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
    protected void onStart() {
        super.onStart();
        CameraManage.getInstance().getCameraManager().openCamera();
        HSMDecode.getInstance().getHsmDecoder().addResultListener(this);
//        String decoderVersion = HSMDecode.getInstance().getHsmDecoder().getDecoderVersion();
        String apiVersion = com.honeywell.barcode.HSMDecoder.getAPIVersion();
        System.out.println("====version===="+apiVersion);
    }

    @Override
    protected void onStop() {
        super.onStop();
        CameraManage.getInstance().getCameraManager().closeCamera();
        HSMDecode.getInstance().getHsmDecoder().removeResultListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
