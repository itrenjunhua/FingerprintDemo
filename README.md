# Android 指纹验证
Android指纹验证工具类及示例Demo

## 使用方法

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

## 类说明
### FingerprintManagerUtil 类
用于开启指纹验证和取消验证。  
1.开始指纹验证
	
	// 开始进行指纹验证，参数1：上下文；参数2：回调接口
	public static void startFingerprinterVerification(Context context, 
				final FingerprintListener fingerprintListener)

2.取消验证

	// 取消指纹验证
	public static void cancel()
### FingerprintListener、FingerprintListenerAdapter 类
提供验证过程中各种时期的回调方法，`FingerprintListenerAdapter`类实现`FingerprintListener`接口，用于减少重写方法的个数，只需要重写有相关操作时期的方法。

	/**
     * 手机或系统不支持指纹验证时回调
     */
    void onNonsupport();

    /**
     * 手机支持指纹验证，但是还没有录入指纹时回调
     */
    void onEnrollFailed();

    /**
     * 可以进行指纹验证时回调，该方法主要作用用于在进行指纹验证之前做一些操作，比如弹出对话框
     */
    void onAuthenticationStart();

    /**
     * 指纹验证成功时回调
     *
     * @param result {@link FingerprintManagerCompat.AuthenticationResult} 对象
     */
    void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result);

    /**
     * 验证失败时回调
     * <br/>&nbsp;&nbsp;&nbsp;&nbsp;
     * 指纹验证失败后,指纹传感器不会立即关闭指纹验证，系统会提供5次重试的机会,即调用
     * 5次 {@link #onAuthenticationFailed()} 后，才会调用 {@link #onAuthenticationError(int, CharSequence)}
     */
    void onAuthenticationFailed();

    /**
     * 验证出错时回调，指纹传感器会关闭一段时间，具体时间根据厂商不同有所区别
     *
     * @param errMsgId  错误信息id
     * @param errString 错误信息描述
     */
    void onAuthenticationError(int errMsgId, CharSequence errString);

    /**
     * 验证帮助回调
     *
     * @param helpMsgId  帮助信息id
     * @param helpString 帮助信息描述
     */
    void onAuthenticationHelp(int helpMsgId, CharSequence helpString);
