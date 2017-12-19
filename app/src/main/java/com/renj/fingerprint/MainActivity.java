package com.renj.fingerprint;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.renj.fingerprint.fingerprint.FingerprintManagerUtil;

/**
 * 指纹验证示例
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {
    private Button startVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startVerification = (Button) findViewById(R.id.start_verification);

        startVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVerification();
            }
        });
    }

    private AlertDialog alertDialog;

    // 调用指纹验证方法
    private void startVerification() {
        FingerprintManagerUtil.startFingerprinterVerification(this,
                new FingerprintManagerUtil.FingerprintListenerAdapter() {

                    @Override
                    public void onAuthenticationStart() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("指纹验证")
                                .setMessage("指纹验证测试")
                                .setCancelable(false)
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FingerprintManagerUtil.cancel();
                                    }
                                });
                        alertDialog = builder.create();
                        alertDialog.show();
                    }

                    @Override
                    public void onNonsupport() {
                        Log.i("MainActivity", "onNonsupport");
                        Toast.makeText(MainActivity.this, "不支持指纹验证", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onEnrollFailed() {
                        Log.i("MainActivity", "onEnrollFailed");
                        Toast.makeText(MainActivity.this, "没有录入指纹", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                        alertDialog.dismiss();
                        Log.i("MainActivity", "onAuthenticationSucceeded result = [" + result + "]");
                        Toast.makeText(MainActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        Log.i("MainActivity", "onAuthenticationFailed");
                        Toast.makeText(MainActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationError(int errMsgId, CharSequence errString) {
                        Log.i("MainActivity", "onAuthenticationError errMsgId = [" + errMsgId + "], errString = [" + errString + "]");
                        Toast.makeText(MainActivity.this, "提示: " + errString, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                        Log.i("MainActivity", "onAuthenticationHelp helpMsgId = [" + helpMsgId + "], helpString = [" + helpString + "]");
                        Toast.makeText(MainActivity.this, "提示: " + helpString, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
