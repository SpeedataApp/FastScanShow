package com.speedata.scanpaidservice.scandemo;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honeywell.barcode.HSMDecodeResult;
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
    private List<ScanBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_demo);
        initView();
    }

    private void initView() {
        mTvShow = findViewById(R.id.tv_show);
        mTvTime = findViewById(R.id.tv_time);

        mTvShow.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTvTime.setMovementMethod(ScrollingMovementMethod.getInstance());


        tvNextActivity = findViewById(R.id.tv_result);
        tvNextActivity.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


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
                    tvNextActivity.setText("扫描数量" + list.size());
                    tvNextActivity.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v
     *         The view that was clicked.
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
