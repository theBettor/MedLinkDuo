package com.bettor.medlinkduo.core.di;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\b\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0006\u001a\u00020\u0007H&\u00a8\u0006\b"}, d2 = {"Lcom/bettor/medlinkduo/core/di/AppDepsEntryPoint;", "", "sensory", "Lcom/bettor/medlinkduo/core/ui/SensoryFeedback;", "speakNumeric", "Lcom/bettor/medlinkduo/domain/SpeakMeasurementNumericUseCase;", "tts", "Lcom/bettor/medlinkduo/domain/TtsController;", "app_debug"})
@dagger.hilt.EntryPoint
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public abstract interface AppDepsEntryPoint {
    
    @org.jetbrains.annotations.NotNull
    public abstract com.bettor.medlinkduo.domain.TtsController tts();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.bettor.medlinkduo.domain.SpeakMeasurementNumericUseCase speakNumeric();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.bettor.medlinkduo.core.ui.SensoryFeedback sensory();
}