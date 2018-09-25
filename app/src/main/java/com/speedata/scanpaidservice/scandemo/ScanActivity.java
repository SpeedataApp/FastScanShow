package com.speedata.scanpaidservice.scandemo;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.barcode.HSMDecodeResult;
import com.honeywell.barcode.HSMDecoder;
import com.honeywell.plugins.decode.DecodeResultListener;
import com.speedata.scanpaidservice.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.honeywell.barcode.Symbology.CODE128;
import static com.honeywell.barcode.Symbology.EAN13;
import static com.honeywell.barcode.Symbology.EAN8;
import static com.honeywell.barcode.Symbology.QR;



public class ScanActivity extends AppCompatActivity implements DecodeResultListener, View.OnClickListener {//}, DecodeResultListener {

    private final static String TAG = "MainActivity";
    private TextView tvNextActivity;
    private TextView mTvShow;
    private TextView mTvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_demo);
        initView();
        setSettings();
    }

    private void initView() {
        mTvShow = findViewById(R.id.tv_show);
        mTvTime = findViewById(R.id.tv_time);

        tvNextActivity = findViewById(R.id.tv_result);
        tvNextActivity.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
        HSMDecode.getInstance().removeHsmDecoder();
    }


    public void setSettings() {
        HSMDecode.getInstance().setHsmDecoder(this);
        HSMDecode.getInstance().getHsmDecoder().enableSymbology(QR);
        HSMDecode.getInstance().getHsmDecoder().enableSymbology(EAN13);
        HSMDecode.getInstance().getHsmDecoder().enableSymbology(CODE128);
        HSMDecode.getInstance().getHsmDecoder().enableSymbology(EAN8);
        CameraManage.getInstance().setCameraManager(this);

    }

    private List<ScanBean> list = new ArrayList<>();

    @Override
    public void onHSMDecodeResult(HSMDecodeResult[] hsmDecodeResults) {
        try {
            for (int i = 0; i < hsmDecodeResults.length; i++) {
                String decodeDate = new String(hsmDecodeResults[i].getBarcodeDataBytes(),
                        "utf8");
                boolean success = true;
                long longValue = hsmDecodeResults[i].getDecodeTime().longValue();
                for (ScanBean scanBean : list) {
                    String code = scanBean.getCode();
                    if (code.equals(decodeDate)) {
                        success = false;
                    }
                }
                if (success) {
                    ScanBean scanBean = new ScanBean();
                    scanBean.setCode(decodeDate);
                    scanBean.setLongValue(longValue);
                    list.add(scanBean);
                    mTvShow.append(decodeDate + "\n");
                    mTvTime.append(longValue + "ms\n");
                }
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_result:
                startActivity(new Intent(ScanActivity.this, SecondActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart");
        CameraManage.getInstance().getCameraManager().openCamera();
        HSMDecode.getInstance().getHsmDecoder().addResultListener(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop");
        CameraManage.getInstance().getCameraManager().closeCamera();
        HSMDecode.getInstance().getHsmDecoder().removeResultListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
