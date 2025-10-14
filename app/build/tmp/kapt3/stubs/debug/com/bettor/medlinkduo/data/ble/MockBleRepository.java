package com.bettor.medlinkduo.data.ble;

@javax.inject.Singleton
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001bJ\u0019\u0010\u001c\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001bJ\u0011\u0010\u001d\u001a\u00020\u0018H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001eJ\u0018\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020 2\u0006\u0010\"\u001a\u00020 H\u0002J\u001d\u0010#\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0$0\u000eH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001eR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u000eX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\n0\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006%"}, d2 = {"Lcom/bettor/medlinkduo/data/ble/MockBleRepository;", "Lcom/bettor/medlinkduo/domain/BleRepository;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "(Lkotlinx/coroutines/CoroutineScope;)V", "_measurements", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Lcom/bettor/medlinkduo/domain/Measurement;", "connectionState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/bettor/medlinkduo/domain/ConnectionState;", "getConnectionState", "()Lkotlinx/coroutines/flow/StateFlow;", "measurements", "Lkotlinx/coroutines/flow/Flow;", "getMeasurements", "()Lkotlinx/coroutines/flow/Flow;", "notifyJob", "Lkotlinx/coroutines/Job;", "rnd", "Ljava/util/Random;", "state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "backoffReconnect", "", "d", "Lcom/bettor/medlinkduo/domain/BleDevice;", "(Lcom/bettor/medlinkduo/domain/BleDevice;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "connect", "disconnect", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "rand", "", "min", "maxInclusive", "scan", "", "app_debug"})
public final class MockBleRepository implements com.bettor.medlinkduo.domain.BleRepository {
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.bettor.medlinkduo.domain.ConnectionState> state = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.bettor.medlinkduo.domain.ConnectionState> connectionState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableSharedFlow<com.bettor.medlinkduo.domain.Measurement> _measurements = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<com.bettor.medlinkduo.domain.Measurement> measurements = null;
    @org.jetbrains.annotations.Nullable
    private kotlinx.coroutines.Job notifyJob;
    @org.jetbrains.annotations.NotNull
    private final java.util.Random rnd = null;
    
    @javax.inject.Inject
    public MockBleRepository(@com.bettor.medlinkduo.core.di.AppScope
    @org.jetbrains.annotations.NotNull
    kotlinx.coroutines.CoroutineScope scope) {
        super();
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.StateFlow<com.bettor.medlinkduo.domain.ConnectionState> getConnectionState() {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<com.bettor.medlinkduo.domain.Measurement> getMeasurements() {
        return null;
    }
    
    /**
     * inclusive 범위 난수 (min..maxInclusive)
     */
    private final int rand(int min, int maxInclusive) {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object scan(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends java.util.List<com.bettor.medlinkduo.domain.BleDevice>>> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object connect(@org.jetbrains.annotations.NotNull
    com.bettor.medlinkduo.domain.BleDevice d, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object backoffReconnect(com.bettor.medlinkduo.domain.BleDevice d, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object disconnect(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}