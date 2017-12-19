package com.renj.fingerprint.fingerprint;

import android.content.Context;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-10-09   16:23
 * <p>
 * 描述：指纹验证工具类
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class FingerprintManagerUtil {
    private static FingerprintManagerCompat fingerprintManagerCompat;
    private static CancellationSignal cancellationSignal;

    private FingerprintManagerUtil() {
    }

    /**
     * 开始进行指纹验证
     *
     * @param context                    上下文
     * @param fingerprintListener 指纹验证回调接口
     */
    public static void startFingerprinterVerification(Context context, final FingerprintListener fingerprintListener) {
        fingerprintManagerCompat = FingerprintManagerCompat.from(context);

        // 是否支持指纹验证
        if (fingerprintManagerCompat == null || !fingerprintManagerCompat.isHardwareDetected()) {
            if (fingerprintListener != null)
                fingerprintListener.onNonsupport();
            return;
        }

        // 是否录入了指纹
        if (!fingerprintManagerCompat.hasEnrolledFingerprints()) {
            if (fingerprintListener != null)
                fingerprintListener.onEnrollFailed();
            return;
        }

        // 回调可以开始进行认证
        if (fingerprintListener != null)
            fingerprintListener.onAuthenticationStart();

        cancellationSignal = new CancellationSignal();
        fingerprintManagerCompat.authenticate(null, 0, cancellationSignal, new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                // 验证出错回调，指纹传感器会关闭一段时间
                super.onAuthenticationError(errMsgId, errString);
                if (fingerprintListener != null)
                    fingerprintListener.onAuthenticationError(errMsgId, errString);
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                // 验证帮助回调
                super.onAuthenticationHelp(helpMsgId, helpString);
                if (fingerprintListener != null)
                    fingerprintListener.onAuthenticationHelp(helpMsgId, helpString);
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                // 验证成功
                super.onAuthenticationSucceeded(result);
                if (fingerprintListener != null)
                    fingerprintListener.onAuthenticationSucceeded(result);
            }

            @Override
            public void onAuthenticationFailed() {
                // 验证失败  指纹验证失败后,指纹传感器不会立即关闭指纹验证,
                // 系统会提供5次重试的机会,即调用5次onAuthenticationFailed()后,才会调用onAuthenticationError()
                super.onAuthenticationFailed();
                if (fingerprintListener != null)
                    fingerprintListener.onAuthenticationFailed();
            }
        }, null);
    }

    /**
     * 取消指纹验证
     */
    public static void cancel() {
        if (cancellationSignal != null && !cancellationSignal.isCanceled())
            cancellationSignal.cancel();
    }

    /**
     * 指纹验证回调监听
     */
    public interface FingerprintListener {
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
    }

    /**
     * FingerprintListener 回调监听适配器，减少不必要方法的重写，只需要重写需要处理的对应方法即可
     */
    public abstract static class FingerprintListenerAdapter implements FingerprintListener {

        @Override
        public void onNonsupport() {
        }

        @Override
        public void onEnrollFailed() {
        }

        @Override
        public void onAuthenticationStart() {
        }

        @Override
        public void onAuthenticationFailed() {
        }

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        }
    }
}
