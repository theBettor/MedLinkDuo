package com.bettor.medlinkduo.domain;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\u0010\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006H&J\u0019\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u00a6@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\bJ\b\u0010\t\u001a\u00020\u0003H&\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\n"}, d2 = {"Lcom/bettor/medlinkduo/domain/TtsController;", "", "shutdown", "", "speak", "text", "", "speakAndWait", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "stop", "app_debug"})
public abstract interface TtsController {
    
    /**
     * 큐를 비우고 즉시 말함(대기 없음)
     */
    public abstract void speak(@org.jetbrains.annotations.NotNull
    java.lang.String text);
    
    /**
     * 이 발화가 끝날 때까지 suspend (UtteranceProgressListener 기반)
     */
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object speakAndWait(@org.jetbrains.annotations.NotNull
    java.lang.String text, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * 현재 발화 중지
     */
    public abstract void stop();
    
    /**
     * 엔진 종료(앱 종료 등)
     */
    public abstract void shutdown();
}