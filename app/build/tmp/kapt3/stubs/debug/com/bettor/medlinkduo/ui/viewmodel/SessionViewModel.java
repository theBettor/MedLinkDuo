package com.bettor.medlinkduo.ui.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u001e\u0010\u0018\u001a\u00020\u00192\u0016\b\u0002\u0010\u001a\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u000b\u0012\u0004\u0012\u00020\u001c0\u001bJ\u0006\u0010\u001d\u001a\u00020\u0019J\u0006\u0010\u001e\u001a\u00020\u0019R\u0016\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\r0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0010\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0014\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\r0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0013R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0013\u00a8\u0006\u001f"}, d2 = {"Lcom/bettor/medlinkduo/ui/viewmodel/SessionViewModel;", "Landroidx/lifecycle/ViewModel;", "startUseCase", "Lcom/bettor/medlinkduo/domain/StartMeasurementUseCase;", "pauseUseCase", "Lcom/bettor/medlinkduo/domain/PauseMeasurementUseCase;", "endUseCase", "Lcom/bettor/medlinkduo/domain/EndMeasurementUseCase;", "(Lcom/bettor/medlinkduo/domain/StartMeasurementUseCase;Lcom/bettor/medlinkduo/domain/PauseMeasurementUseCase;Lcom/bettor/medlinkduo/domain/EndMeasurementUseCase;)V", "_last", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/bettor/medlinkduo/domain/Measurement;", "_summary", "Lcom/bettor/medlinkduo/domain/SessionSummary;", "_ui", "Lcom/bettor/medlinkduo/core/common/MeasureUiState;", "last", "Lkotlinx/coroutines/flow/StateFlow;", "getLast", "()Lkotlinx/coroutines/flow/StateFlow;", "summary", "getSummary", "ui", "getUi", "end", "Lkotlinx/coroutines/Job;", "onSummary", "Lkotlin/Function1;", "", "pause", "remeasure", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class SessionViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.bettor.medlinkduo.domain.StartMeasurementUseCase startUseCase = null;
    @org.jetbrains.annotations.NotNull
    private final com.bettor.medlinkduo.domain.PauseMeasurementUseCase pauseUseCase = null;
    @org.jetbrains.annotations.NotNull
    private final com.bettor.medlinkduo.domain.EndMeasurementUseCase endUseCase = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.bettor.medlinkduo.core.common.MeasureUiState> _ui = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.bettor.medlinkduo.core.common.MeasureUiState> ui = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.bettor.medlinkduo.domain.Measurement> _last = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.bettor.medlinkduo.domain.Measurement> last = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.bettor.medlinkduo.domain.SessionSummary> _summary = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.bettor.medlinkduo.domain.SessionSummary> summary = null;
    
    @javax.inject.Inject
    public SessionViewModel(@org.jetbrains.annotations.NotNull
    com.bettor.medlinkduo.domain.StartMeasurementUseCase startUseCase, @org.jetbrains.annotations.NotNull
    com.bettor.medlinkduo.domain.PauseMeasurementUseCase pauseUseCase, @org.jetbrains.annotations.NotNull
    com.bettor.medlinkduo.domain.EndMeasurementUseCase endUseCase) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.bettor.medlinkduo.core.common.MeasureUiState> getUi() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.bettor.medlinkduo.domain.Measurement> getLast() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.bettor.medlinkduo.domain.SessionSummary> getSummary() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job remeasure() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job pause() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job end(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.bettor.medlinkduo.domain.Measurement, kotlin.Unit> onSummary) {
        return null;
    }
}