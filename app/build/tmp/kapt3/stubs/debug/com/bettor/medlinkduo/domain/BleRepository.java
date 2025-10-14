package com.bettor.medlinkduo.domain;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\bf\u0018\u00002\u00020\u0001J\u0019\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u00a6@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010J\u0011\u0010\u0011\u001a\u00020\rH\u00a6@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012J\u001d\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u00140\bH\u00a6@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012R\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0018\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000b\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0015"}, d2 = {"Lcom/bettor/medlinkduo/domain/BleRepository;", "", "connectionState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/bettor/medlinkduo/domain/ConnectionState;", "getConnectionState", "()Lkotlinx/coroutines/flow/StateFlow;", "measurements", "Lkotlinx/coroutines/flow/Flow;", "Lcom/bettor/medlinkduo/domain/Measurement;", "getMeasurements", "()Lkotlinx/coroutines/flow/Flow;", "connect", "", "d", "Lcom/bettor/medlinkduo/domain/BleDevice;", "(Lcom/bettor/medlinkduo/domain/BleDevice;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "disconnect", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "scan", "", "app_debug"})
public abstract interface BleRepository {
    
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.StateFlow<com.bettor.medlinkduo.domain.ConnectionState> getConnectionState();
    
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<com.bettor.medlinkduo.domain.Measurement> getMeasurements();
    
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object scan(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends java.util.List<com.bettor.medlinkduo.domain.BleDevice>>> $completion);
    
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object connect(@org.jetbrains.annotations.NotNull
    com.bettor.medlinkduo.domain.BleDevice d, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object disconnect(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}