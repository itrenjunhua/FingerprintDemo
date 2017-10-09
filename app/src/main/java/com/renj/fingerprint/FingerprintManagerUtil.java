package com.renj.fingerprint;

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

    public static void startFingerprinterVerification(Context context, final FingerprintManagerListener fingerprintManagerListener) {
        fingerprintManagerCompat = FingerprintManagerCompat.from(context);

        if (fingerprintManagerCompat == null || !fingerprintManagerCompat.isHardwareDetected()) {
            if (fingerprintManagerListener != null)
                fingerprintManagerListener.onNonsupport();
            return;
        }

        if (!fingerprintManagerCompat.hasEnrolledFingerprints()) {
            if (fingerprintManagerListener != null)
                fingerprintManagerListener.onEnrollFailed();
            return;
        }

        if (fingerprintManagerListener != null)
            fingerprintManagerListener.onAuthenticationStart();

        cancellationSignal = new CancellationSignal();
        fingerprintManagerCompat.authenticate(null, 0, cancellationSignal, new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                super.onAuthenticationError(errMsgId, errString);
                if (fingerprintManagerListener != null)
                    fingerprintManagerListener.onAuthenticationError(errMsgId, errString);
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                super.onAuthenticationHelp(helpMsgId, helpString);
                if (fingerprintManagerListener != null)
                    fingerprintManagerListener.onAuthenticationHelp(helpMsgId, helpString);
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                if (fingerprintManagerListener != null)
                    fingerprintManagerListener.onAuthenticationSucceeded(result);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                if (fingerprintManagerListener != null)
                    fingerprintManagerListener.onAuthenticationFailed();
            }
        }, null);
    }

    public static void cancel() {
        if (cancellationSignal != null)
            cancellationSignal.cancel();
    }

    public interface FingerprintManagerListener {
        void onNonsupport();

        void onEnrollFailed();

        void onAuthenticationStart();

        void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result);

        void onAuthenticationFailed();

        void onAuthenticationError(int errMsgId, CharSequence errString);

        void onAuthenticationHelp(int helpMsgId, CharSequence helpString);
    }
}
