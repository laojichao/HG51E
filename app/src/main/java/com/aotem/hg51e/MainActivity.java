package com.aotem.hg51e;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aotem.hg51e.net.NetworkUtils;
import com.hjq.shape.view.ShapeButton;
import com.hjq.shape.view.ShapeEditText;
import com.hjq.toast.Toaster;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private ShapeEditText et_account;
    private ShapeEditText et_password;
    private ShapeEditText et_host;
    private ShapeButton btn_get;
    private ShapeButton btn_copy;

    private ClipboardManager clipboardManager;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        et_host = findViewById(R.id.et_host);
        btn_get = findViewById(R.id.btn_get);
        btn_copy = findViewById(R.id.btn_copy);
        btn_copy.setOnClickListener(this);
        btn_get.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_get) {
            String host = et_host.getText().toString();
            if (host.isEmpty()) {
                host = "192.168.1.1";
            }
            Network network = connectivityManager.getActiveNetwork();
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            if (networkCapabilities != null) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                    Toaster.showShort("请关闭你的代理");
                    return;
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    NetworkUtils.queryUser("http://" + host).safeSubscribe(new DisposableObserver<User>() {
                        @Override
                        public void onNext(@NonNull User user) {
                            Log.d(TAG, "onNext: ");
                            Toaster.showShort("获取成功");
                            String account = user.getData().getWeb_tele_account_name();
                            String password = user.getData().getTeleAccount_password();
//                            Log.d(TAG, "onNext: " + account);
//                            Log.d(TAG, "onNext: " + password);
                            et_account.setText(account);
                            et_password.setText(password);

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Toaster.showShort("获取失败");
//                            Log.d(TAG, "onError: " + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

//                            Log.d(TAG, "onComplete: ");
                        }
                    });
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Toaster.showShort("请连接上无线网络");
                }
            } else {
                Toaster.showShort("当前无网络连接");
            }

        } else if (v.getId() == R.id.btn_copy) {
            String password = et_password.getText().toString();
            if (password.isEmpty()) {
                Toaster.showShort("当前密码为空，请先获取密码");
                return;
            }
            if (clipboardManager != null) {
                clipboardManager.setPrimaryClip(ClipData.newPlainText("password", password));
                Toaster.showShort("已复制到剪切板");
            }
        }
    }
}