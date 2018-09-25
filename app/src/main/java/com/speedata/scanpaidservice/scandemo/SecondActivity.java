package com.speedata.scanpaidservice.scandemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.speedata.scanpaidservice.R;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initView();
    }

    private void initView() {
        btnScan = findViewById(R.id.btn_second);
        btnScan.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_second:
                startActivity(new Intent(SecondActivity.this, ScanSecondActivity.class));
                break;
                default:
                    break;
        }
    }
}
