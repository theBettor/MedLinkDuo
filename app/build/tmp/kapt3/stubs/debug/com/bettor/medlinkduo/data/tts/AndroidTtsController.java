package com.bettor.medlinkduo.data.tts;

@javax.inject.Singleton
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0007\u0018\u00002\u00020\u0001:\u0001\u001cB\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0013\u001a\u00020\u000bH\u0002J\b\u0010\u0014\u001a\u00020\u000bH\u0002J\b\u0010\u0015\u001a\u00020\u000bH\u0016J\u0010\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u0017\u001a\u00020\u0010H\u0016J\u0019\u0010\u0018\u001a\u00020\u000b2\u0006\u0010\u0017\u001a\u00020\u0010H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0019J\u0010\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\u0017\u001a\u00020\u0010H\u0002J\b\u0010\u001b\u001a\u00020\u000bH\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R/\u0010\u000e\u001a \u0012\u0004\u0012\u00020\u0010\u0012\u0016\u0012\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u0012\u0012\u0004\u0012\u00020\u000b0\u00110\u000fX\u0082\u0004\u00f8\u0001\u0000\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001d"}, d2 = {"Lcom/bettor/medlinkduo/data/tts/AndroidTtsController;", "Lcom/bettor/medlinkduo/domain/TtsController;", "ctx", "Landroid/content/Context;", "(Landroid/content/Context;)V", "audioFocus", "Lcom/bettor/medlinkduo/core/common/AudioFocusManager;", "focusHolders", "Ljava/util/concurrent/atomic/AtomicInteger;", "ready", "Lkotlinx/coroutines/CompletableDeferred;", "", "tts", "Landroid/speech/tts/TextToSpeech;", "waiters", "", "", "Lkotlin/Function1;", "Lkotlin/Result;", "abandonFocus", "requestFocus", "shutdown", "speak", "text", "speakAndWait", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "speakNow", "stop", "TtsStopped", "app_debug"})
public final class AndroidTtsController implements com.bettor.medlinkduo.domain.TtsController {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context ctx = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.CompletableDeferred<kotlin.Unit> ready = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.Map<java.lang.String, kotlin.jvm.functions.Function1<kotlin.Result<kotlin.Unit>, kotlin.Unit>> waiters = null;
    @org.jetbrains.annotations.NotNull
    private final com.bettor.medlinkduo.core.common.AudioFocusManager audioFocus = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.concurrent.atomic.AtomicInteger focusHolders = null;
    private android.speech.tts.TextToSpeech tts;
    
    @javax.inject.Inject
    public AndroidTtsController(@dagger.hilt.android.qualifiers.ApplicationContext
    @org.jetbrains.annotations.NotNull
    android.content.Context ctx) {
        super();
    }
    
    private final void requestFocus() {
    }
    
    private final void abandonFocus() {
    }
    
    /**
     * 큐를 비우고 즉시 말함. 엔진 준비 전이면 준비 후 자동 발화.
     */
    @java.lang.Override
    public void speak(@org.jetbrains.annotations.NotNull
    java.lang.String text) {
    }
    
    /**
     * 발화를 시작하고 **끝날 때까지 suspend**.
     */
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object speakAndWait(@org.jetbrains.annotations.NotNull
    java.lang.String text, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    public void stop() {
    }
    
    @java.lang.Override
    public void shutdown() {
    }
    
    private final void speakNow(java.lang.String text) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\b\u00c2\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0002\u00a8\u0006\u0005"}, d2 = {"Lcom/bettor/medlinkduo/data/tts/AndroidTtsController$TtsStopped;", "Ljava/util/concurrent/CancellationException;", "()V", "readResolve", "", "app_debug"})
    static final class TtsStopped extends java.util.concurrent.CancellationException {
        @org.jetbrains.annotations.NotNull
        public static final com.bettor.medlinkduo.data.tts.AndroidTtsController.TtsStopped INSTANCE = null;
        
        private TtsStopped() {
            super();
        }
        
        private final java.lang.Object readResolve() {
            return null;
        }
    }
}