package com.bettor.medlinkduo.domain;

/**
 * 측정값에서 숫자만 추출해 말하고 끝날 때까지 대기.
 * 예: "120.5 mmHg" -> "120.5" 만 발화
 */
@javax.inject.Singleton
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086B\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\n"}, d2 = {"Lcom/bettor/medlinkduo/domain/SpeakMeasurementNumericUseCase;", "", "tts", "Lcom/bettor/medlinkduo/domain/TtsController;", "(Lcom/bettor/medlinkduo/domain/TtsController;)V", "invoke", "", "m", "Lcom/bettor/medlinkduo/domain/Measurement;", "(Lcom/bettor/medlinkduo/domain/Measurement;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class SpeakMeasurementNumericUseCase {
    @org.jetbrains.annotations.NotNull
    private final com.bettor.medlinkduo.domain.TtsController tts = null;
    
    @javax.inject.Inject
    public SpeakMeasurementNumericUseCase(@org.jetbrains.annotations.NotNull
    com.bettor.medlinkduo.domain.TtsController tts) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object invoke(@org.jetbrains.annotations.NotNull
    com.bettor.medlinkduo.domain.Measurement m, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}