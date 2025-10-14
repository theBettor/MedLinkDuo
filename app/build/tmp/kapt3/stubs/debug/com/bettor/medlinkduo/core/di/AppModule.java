package com.bettor.medlinkduo.core.di;

@dagger.Module
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007J\u0012\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\u0004H\u0007J\u0012\u0010\b\u001a\u00020\t2\b\b\u0001\u0010\n\u001a\u00020\u000bH\u0007J\u0012\u0010\f\u001a\u00020\r2\b\b\u0001\u0010\n\u001a\u00020\u000bH\u0007\u00a8\u0006\u000e"}, d2 = {"Lcom/bettor/medlinkduo/core/di/AppModule;", "", "()V", "appScope", "Lkotlinx/coroutines/CoroutineScope;", "bleRepo", "Lcom/bettor/medlinkduo/domain/BleRepository;", "scope", "provideTts", "Lcom/bettor/medlinkduo/domain/TtsController;", "ctx", "Landroid/content/Context;", "sensory", "Lcom/bettor/medlinkduo/core/ui/SensoryFeedback;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class AppModule {
    @org.jetbrains.annotations.NotNull
    public static final com.bettor.medlinkduo.core.di.AppModule INSTANCE = null;
    
    private AppModule() {
        super();
    }
    
    @dagger.Provides
    @javax.inject.Singleton
    @AppScope
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.CoroutineScope appScope() {
        return null;
    }
    
    @dagger.Provides
    @javax.inject.Singleton
    @org.jetbrains.annotations.NotNull
    public final com.bettor.medlinkduo.domain.BleRepository bleRepo(@AppScope
    @org.jetbrains.annotations.NotNull
    kotlinx.coroutines.CoroutineScope scope) {
        return null;
    }
    
    @dagger.Provides
    @javax.inject.Singleton
    @org.jetbrains.annotations.NotNull
    public final com.bettor.medlinkduo.domain.TtsController provideTts(@dagger.hilt.android.qualifiers.ApplicationContext
    @org.jetbrains.annotations.NotNull
    android.content.Context ctx) {
        return null;
    }
    
    @dagger.Provides
    @javax.inject.Singleton
    @org.jetbrains.annotations.NotNull
    public final com.bettor.medlinkduo.core.ui.SensoryFeedback sensory(@dagger.hilt.android.qualifiers.ApplicationContext
    @org.jetbrains.annotations.NotNull
    android.content.Context ctx) {
        return null;
    }
}