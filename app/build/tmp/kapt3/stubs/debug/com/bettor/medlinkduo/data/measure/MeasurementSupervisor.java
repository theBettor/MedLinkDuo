package com.bettor.medlinkduo.data.measure;

@javax.inject.Singleton
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0019\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J%\u0010\u000b\u001a\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\f0\u000eH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010J\u0011\u0010\u0011\u001a\u00020\fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0013"}, d2 = {"Lcom/bettor/medlinkduo/data/measure/MeasurementSupervisor;", "", "repo", "Lcom/bettor/medlinkduo/domain/BleRepository;", "io", "Lkotlinx/coroutines/CoroutineDispatcher;", "(Lcom/bettor/medlinkduo/domain/BleRepository;Lkotlinx/coroutines/CoroutineDispatcher;)V", "gate", "Lkotlinx/coroutines/sync/Mutex;", "job", "Lkotlinx/coroutines/Job;", "start", "", "onEach", "Lkotlin/Function1;", "Lcom/bettor/medlinkduo/domain/Measurement;", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "stop", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class MeasurementSupervisor {
    @org.jetbrains.annotations.NotNull
    private final com.bettor.medlinkduo.domain.BleRepository repo = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.CoroutineDispatcher io = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.sync.Mutex gate = null;
    @org.jetbrains.annotations.Nullable
    private kotlinx.coroutines.Job job;
    
    @javax.inject.Inject
    public MeasurementSupervisor(@org.jetbrains.annotations.NotNull
    com.bettor.medlinkduo.domain.BleRepository repo, @com.bettor.medlinkduo.core.di.IoDispatcher
    @org.jetbrains.annotations.NotNull
    kotlinx.coroutines.CoroutineDispatcher io) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object start(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.bettor.medlinkduo.domain.Measurement, kotlin.Unit> onEach, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object stop(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}