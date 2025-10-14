package com.bettor.medlinkduo.ui.viewmodel;

/**
 * 말하기 큐에 실어 보낼 항목
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B#\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J)\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00052\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0015\u001a\u00020\u0016H\u00d6\u0001J\t\u0010\u0017\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u0018"}, d2 = {"Lcom/bettor/medlinkduo/ui/viewmodel/Speech;", "", "text", "", "await", "", "tag", "Lcom/bettor/medlinkduo/ui/viewmodel/SpeechTag;", "(Ljava/lang/String;ZLcom/bettor/medlinkduo/ui/viewmodel/SpeechTag;)V", "getAwait", "()Z", "getTag", "()Lcom/bettor/medlinkduo/ui/viewmodel/SpeechTag;", "getText", "()Ljava/lang/String;", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class Speech {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String text = null;
    
    /**
     * true면 speakAndWait, false면 speak(비대기)
     */
    private final boolean await = false;
    
    /**
     * 재생이 끝난 뒤 UI가 처리할 태그(네비게이션 등)
     */
    @org.jetbrains.annotations.Nullable
    private final com.bettor.medlinkduo.ui.viewmodel.SpeechTag tag = null;
    
    public Speech(@org.jetbrains.annotations.NotNull
    java.lang.String text, boolean await, @org.jetbrains.annotations.Nullable
    com.bettor.medlinkduo.ui.viewmodel.SpeechTag tag) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getText() {
        return null;
    }
    
    /**
     * true면 speakAndWait, false면 speak(비대기)
     */
    public final boolean getAwait() {
        return false;
    }
    
    /**
     * 재생이 끝난 뒤 UI가 처리할 태그(네비게이션 등)
     */
    @org.jetbrains.annotations.Nullable
    public final com.bettor.medlinkduo.ui.viewmodel.SpeechTag getTag() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component1() {
        return null;
    }
    
    public final boolean component2() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.bettor.medlinkduo.ui.viewmodel.SpeechTag component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.bettor.medlinkduo.ui.viewmodel.Speech copy(@org.jetbrains.annotations.NotNull
    java.lang.String text, boolean await, @org.jetbrains.annotations.Nullable
    com.bettor.medlinkduo.ui.viewmodel.SpeechTag tag) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String toString() {
        return null;
    }
}