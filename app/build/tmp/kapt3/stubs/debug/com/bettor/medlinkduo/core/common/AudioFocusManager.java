package com.bettor.medlinkduo.core.common;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u000b\u001a\u00020\fJ\u0006\u0010\r\u001a\u00020\u000eR\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/bettor/medlinkduo/core/common/AudioFocusManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "afr", "Landroid/media/AudioFocusRequest;", "am", "Landroid/media/AudioManager;", "listener", "Landroid/media/AudioManager$OnAudioFocusChangeListener;", "abandon", "", "request", "", "app_debug"})
public final class AudioFocusManager {
    @org.jetbrains.annotations.NotNull
    private final android.media.AudioManager am = null;
    @org.jetbrains.annotations.Nullable
    private android.media.AudioFocusRequest afr;
    @org.jetbrains.annotations.NotNull
    private final android.media.AudioManager.OnAudioFocusChangeListener listener = null;
    
    public AudioFocusManager(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    public final boolean request() {
        return false;
    }
    
    public final void abandon() {
    }
}