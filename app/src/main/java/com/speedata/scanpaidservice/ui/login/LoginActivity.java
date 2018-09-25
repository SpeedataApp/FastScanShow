package com.speedata.scanpaidservice.ui.login;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.speedata.scanpaidservice.R;
import com.speedata.scanpaidservice.mvp.MVPBaseActivity;
import com.speedata.scanpaidservice.ui.main.MainActivity;
import com.speedata.scanpaidservice.ui.pay.PayActivity;
import com.speedata.scanpaidservice.utils.DataCleanManager;
import com.speedata.scanpaidservice.utils.DateUtils;
import com.speedata.scanpaidservice.utils.SharedXmlUtil;
import com.speedata.scanservice.bean.member2.GetMember2DataBean;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class LoginActivity extends MVPBaseActivity<LoginContract.View, LoginPresenter> implements LoginContract.View {

    private Toolbar mToolbar;
    private EditText mEtUsertel;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private TextView mTvRegister;
    private KProgressHUD kProgressHUD;
    private TextView mTvInfo;
    private Button mBtnClean;

    @Override
    public void initData(Bundle bundle) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permission();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle bundle, View view) {
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.finish();
            }
        });

        mEtUsertel = findViewById(R.id.et_usertel);
        mEtPassword = findViewById(R.id.et_password);
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
        mTvRegister = findViewById(R.id.tv_register);
        mTvRegister.setOnClickListener(this);

        mTvInfo = findViewById(R.id.tv_info);
        mTvInfo.setText("V" + getVerName(getApplicationContext()) + "\n");
        mBtnClean = findViewById(R.id.btn_clean);
        mBtnClean.setOnClickListener(this);
    }

    /**
     * 得到软件显示版本信息
     *
     * @param context 上下文
     * @return 当前版本信息
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            String packageName = context.getPackageName();
            verName = context.getPackageManager()
                    .getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }


    @Override
    public void doBusiness() {
    }

    @Override
    public void onWidgetClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                kProgressHUD = KProgressHUD.create(LoginActivity.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();
                String user = mEtUsertel.getText().toString();
                String paw = mEtPassword.getText().toString();
                SharedXmlUtil.getInstance(this, "scanPaid").write("USER_NAME", user);
                mPresenter.login(LoginActivity.this, user);
                break;
            case R.id.tv_register:
                break;

            case R.id.btn_clean:
                DataCleanManager.cleanSharedPreference(mActivity);
                DataCleanManager.cleanInternalCache(mActivity);
                DataCleanManager.cleanExternalCache(mActivity);
                DataCleanManager.cleanDatabases(mActivity);
                break;
            default:
                break;
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
        if (kProgressHUD != null) {
            kProgressHUD.dismiss();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void openAct(int expireStatus, final String expireDate) {
        if (kProgressHUD != null) {
            kProgressHUD.dismiss();
        }
        if (!TextUtils.isEmpty(expireDate)) {
            String timeByHour = DateUtils.getTimeByHour(48, DateUtils.FORMAT_YMD);
            Long aLong = DateUtils.convertTimeToLong(DateUtils.FORMAT_YMD, expireDate);
            Long time = DateUtils.convertTimeToLong(DateUtils.FORMAT_YMD, timeByHour);
            if (time > aLong) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "注意！会员将在" + expireDate + "到期", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }

        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("expireDate", expireDate);
        bundle.putInt("expireStatus", expireStatus);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 设备换绑
     */
    @Override
    public void exchangeBind(GetMember2DataBean dataBean) {
        if (kProgressHUD != null) {
            kProgressHUD.dismiss();
        }
        ExchangeBindDialog exchangeBindDialog = new ExchangeBindDialog(this, dataBean);
        exchangeBindDialog.setTitle("申请换绑");
        exchangeBindDialog.show();
    }

    @Override
    public void openPayAct() {
        if (kProgressHUD != null) {
            kProgressHUD.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("此为新设备，是否为新设备充值");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(LoginActivity.this, PayActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void permission() {
        AndPermission.with(LoginActivity.this)
                .permission(Manifest.permission.READ_PHONE_STATE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.GET_ACCOUNTS
                        , Manifest.permission.READ_CONTACTS
                        , Manifest.permission.INTERNET
                        , Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.ACCESS_WIFI_STATE
                        , Manifest.permission.CAMERA)
                .callback(listener)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(LoginActivity.this, rationale).show();
                    }
                }).start();
    }

    PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(LoginActivity.this, deniedPermissions)) {
                AndPermission.defaultSettingDialog(LoginActivity.this, 300).show();
            }
        }
    };
}
