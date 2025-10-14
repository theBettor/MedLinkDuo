package com.bettor.medlinkduo.core.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0007\u001a\u00020\bJ\u0006\u0010\t\u001a\u00020\bJ\u0006\u0010\n\u001a\u00020\bJ\u0010\u0010\u000b\u001a\u00020\b2\b\b\u0002\u0010\f\u001a\u00020\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/bettor/medlinkduo/core/ui/SensoryFeedback;", "", "ctx", "Landroid/content/Context;", "(Landroid/content/Context;)V", "tone", "Landroid/media/ToneGenerator;", "error", "", "success", "tick", "vibrate", "ms", "", "app_debug"})
public final class SensoryFeedback {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context ctx = null;
    @org.jetbrains.annotations.NotNull
    private final android.media.ToneGenerator tone = null;
    
    public SensoryFeedback(@org.jetbrains.annotations.NotNull
    android.content.Context ctx) {
        super();
    }
    
    public final void tick() {
    }
    
    public final void success() {
    }
    
    public final void error() {
    }
    
    public final void vibrate(long ms) {
    }
}