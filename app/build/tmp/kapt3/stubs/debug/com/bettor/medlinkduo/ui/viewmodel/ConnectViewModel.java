package com.bettor.medlinkduo.ui.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u001f\u001a\u00020 J\u000e\u0010!\u001a\u00020\u00182\u0006\u0010\"\u001a\u00020\bJ\u0006\u0010#\u001a\u00020 J&\u0010$\u001a\u00020 2\u0006\u0010%\u001a\u00020\n2\b\b\u0002\u0010&\u001a\u00020\u000f2\n\b\u0002\u0010\'\u001a\u0004\u0018\u00010(H\u0002R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u001d\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\n0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0014R\u0017\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\r0\u001c\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001e\u00a8\u0006)"}, d2 = {"Lcom/bettor/medlinkduo/ui/viewmodel/ConnectViewModel;", "Landroidx/lifecycle/ViewModel;", "repo", "Lcom/bettor/medlinkduo/domain/BleRepository;", "(Lcom/bettor/medlinkduo/domain/BleRepository;)V", "_devices", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/bettor/medlinkduo/domain/BleDevice;", "_scanPhase", "", "_speech", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Lcom/bettor/medlinkduo/ui/viewmodel/Speech;", "announcedSynced", "", "connection", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/bettor/medlinkduo/domain/ConnectionState;", "getConnection", "()Lkotlinx/coroutines/flow/StateFlow;", "devices", "getDevices", "scanJob", "Lkotlinx/coroutines/Job;", "scanPhase", "getScanPhase", "speech", "Lkotlinx/coroutines/flow/SharedFlow;", "getSpeech", "()Lkotlinx/coroutines/flow/SharedFlow;", "ensureScan", "", "onConnect", "d", "onScan", "say", "text", "await", "tag", "Lcom/bettor/medlinkduo/ui/viewmodel/SpeechTag;", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class ConnectViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.bettor.medlinkduo.domain.BleRepository repo = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.bettor.medlinkduo.domain.BleDevice>> _devices = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.bettor.medlinkduo.domain.BleDevice>> devices = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.bettor.medlinkduo.domain.ConnectionState> connection = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _scanPhase = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> scanPhase = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableSharedFlow<com.bettor.medlinkduo.ui.viewmodel.Speech> _speech = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.SharedFlow<com.bettor.medlinkduo.ui.viewmodel.Speech> speech = null;
    @org.jetbrains.annotations.Nullable
    private kotlinx.coroutines.Job scanJob;
    private boolean announcedSynced = false;
    
    @javax.inject.Inject
    public ConnectViewModel(@org.jetbrains.annotations.NotNull
    com.bettor.medlinkduo.domain.BleRepository repo) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.bettor.medlinkduo.domain.BleDevice>> getDevices() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.bettor.medlinkduo.domain.ConnectionState> getConnection() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getScanPhase() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.SharedFlow<com.bettor.medlinkduo.ui.viewmodel.Speech> getSpeech() {
        return null;
    }
    
    /**
     * 화면 진입/복귀 시 자동 스캔 보장
     */
    public final void ensureScan() {
    }
    
    public final void onScan() {
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job onConnect(@org.jetbrains.annotations.NotNull
    com.bettor.medlinkduo.domain.BleDevice d) {
        return null;
    }
    
    private final void say(java.lang.String text, boolean await, com.bettor.medlinkduo.ui.viewmodel.SpeechTag tag) {
    }
}