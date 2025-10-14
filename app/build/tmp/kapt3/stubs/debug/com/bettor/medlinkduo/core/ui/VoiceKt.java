package com.bettor.medlinkduo.core.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000(\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a.\u0010\u0000\u001a\u00020\u00012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u00032\u0010\b\u0002\u0010\u0005\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0006H\u0007\u001a\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u00042\u0006\u0010\b\u001a\u00020\t\u001aB\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u00062\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u00032\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bH\u0007\u00a8\u0006\r"}, d2 = {"VoiceButton", "", "onCommand", "Lkotlin/Function1;", "Lcom/bettor/medlinkduo/core/ui/Command;", "allowed", "", "parseCommand", "text", "", "rememberVoiceCommandLauncher", "Lkotlin/Function0;", "onUnknown", "app_debug"})
public final class VoiceKt {
    
    @org.jetbrains.annotations.Nullable
    public static final com.bettor.medlinkduo.core.ui.Command parseCommand(@org.jetbrains.annotations.NotNull
    java.lang.String text) {
        return null;
    }
    
    /**
     * 더블탭 제스처로 호출할 음성 명령 런처를 준비해주는 헬퍼.
     * 사용: val launchVoice = rememberVoiceCommandLauncher(allowed = setOf(...)) { cmd -> ... }
     * 그 다음 .a11yGestures(onDoubleTap = { launchVoice() }) 로 연결.
     */
    @androidx.compose.runtime.Composable
    @org.jetbrains.annotations.NotNull
    public static final kotlin.jvm.functions.Function0<kotlin.Unit> rememberVoiceCommandLauncher(@org.jetbrains.annotations.NotNull
    java.util.Set<? extends com.bettor.medlinkduo.core.ui.Command> allowed, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.bettor.medlinkduo.core.ui.Command, kotlin.Unit> onCommand, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onUnknown) {
        return null;
    }
    
    /**
     * 공용 음성 명령 버튼.
     * - 마이크 권한 요청 → 프롬프트 TTS → 인식 → 명령 콜백
     * - SensoryFeedback(비프/진동)도 함께 사용
     */
    @androidx.compose.runtime.Composable
    public static final void VoiceButton(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.bettor.medlinkduo.core.ui.Command, kotlin.Unit> onCommand, @org.jetbrains.annotations.Nullable
    java.util.Set<? extends com.bettor.medlinkduo.core.ui.Command> allowed) {
    }
}