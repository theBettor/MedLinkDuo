package com.bettor.medlinkduo.core.common;

/**
 * BLE + 알림 권한 유틸 (런타임 요청 대상만 반환).
 * Foreground Service 권한(FGS/CONNECTED_DEVICE)은 '선언'만 필요, 런타임 요청 대상 아님.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tJ\u001b\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\u0005\u001a\u00020\u0006H\u0002\u00a2\u0006\u0002\u0010\rJ\u0013\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0007\u00a2\u0006\u0002\u0010\u000f\u00a8\u0006\u0010"}, d2 = {"Lcom/bettor/medlinkduo/core/common/PermissionMgr;", "", "()V", "allGranted", "", "context", "Landroid/content/Context;", "isPermanentlyDenied", "activity", "Landroid/app/Activity;", "missing", "", "", "(Landroid/content/Context;)[Ljava/lang/String;", "required", "()[Ljava/lang/String;", "app_debug"})
public final class PermissionMgr {
    @org.jetbrains.annotations.NotNull
    public static final com.bettor.medlinkduo.core.common.PermissionMgr INSTANCE = null;
    
    private PermissionMgr() {
        super();
    }
    
    /**
     * SDK별 필요한 런타임 권한 배열을 반환 (확장함수 사용 X)
     */
    @android.annotation.SuppressLint(value = {"InlinedApi"})
    @org.jetbrains.annotations.NotNull
    public final java.lang.String[] required() {
        return null;
    }
    
    /**
     * 아직 허용되지 않은 권한들만 반환
     */
    private final java.lang.String[] missing(android.content.Context context) {
        return null;
    }
    
    /**
     * 전부 허용되었는지 (확장함수 isEmpty() 대신 size 비교)
     */
    public final boolean allGranted(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return false;
    }
    
    /**
     * 시스템이 더는 다이얼로그를 띄워주지 않는 상태(= 사실상 자동 차단)
     */
    public final boolean isPermanentlyDenied(@org.jetbrains.annotations.NotNull
    android.app.Activity activity) {
        return false;
    }
}