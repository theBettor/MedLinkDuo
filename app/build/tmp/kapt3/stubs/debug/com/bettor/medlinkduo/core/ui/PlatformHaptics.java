package com.bettor.medlinkduo.core.ui;

/**
 * 시스템 진동 API(파형)
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0016\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0007\u001a\u00020\bJ\u0006\u0010\t\u001a\u00020\bJ\u0010\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\fH\u0002R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/bettor/medlinkduo/core/ui/PlatformHaptics;", "", "ctx", "Landroid/content/Context;", "(Landroid/content/Context;)V", "vibrator", "Landroid/os/Vibrator;", "error", "", "success", "vibratePattern", "pattern", "", "app_debug"})
public final class PlatformHaptics {
    @org.jetbrains.annotations.Nullable
    private final android.os.Vibrator vibrator = null;
    
    public PlatformHaptics(@org.jetbrains.annotations.NotNull
    android.content.Context ctx) {
        super();
    }
    
    /**
     * 연결 성공: 짧게-간격-짧게
     */
    public final void success() {
    }
    
    /**
     * 에러: 짧게 3회
     */
    public final void error() {
    }
    
    private final void vibratePattern(long[] pattern) {
    }
}