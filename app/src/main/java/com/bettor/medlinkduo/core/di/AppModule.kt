package com.bettor.medlinkduo.core.di

import android.content.Context
import androidx.work.WorkManager
import com.bettor.medlinkduo.core.ui.SensoryFeedback
import com.bettor.medlinkduo.data.ble.MockBleRepository
import com.bettor.medlinkduo.data.prefs.OnboardingStore
import com.bettor.medlinkduo.data.tts.AndroidTtsController
import com.bettor.medlinkduo.domain.BleRepository
import com.bettor.medlinkduo.domain.SpeakMeasurementNumericUseCase
import com.bettor.medlinkduo.domain.TtsController
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppScope

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    @AppScope
    fun appScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Provides
    @Singleton
    fun bleRepo(
        @AppScope scope: CoroutineScope,
    ): BleRepository = MockBleRepository(scope)

    @Provides @Singleton
    fun provideTts(
        @ApplicationContext ctx: Context,
    ): TtsController = AndroidTtsController(ctx)

    @Provides @Singleton
    fun onboardingStore(
        @ApplicationContext ctx: Context,
    ): OnboardingStore = OnboardingStore(ctx)

    @Provides @Singleton
    fun sensory(
        @ApplicationContext ctx: Context,
    ): SensoryFeedback = SensoryFeedback(ctx)
}

@Module
@InstallIn(SingletonComponent::class)
object WorkModule {
    @Provides
    @Singleton
    fun provideWorkManager(
        @ApplicationContext ctx: Context,
    ): WorkManager = WorkManager.getInstance(ctx)
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppDepsEntryPoint {
    fun tts(): TtsController

    fun onboardingStore(): OnboardingStore

    fun speakNumeric(): SpeakMeasurementNumericUseCase

    fun sensory(): SensoryFeedback
}
