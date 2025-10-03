package com.bettor.medlinkduo.data.local

import androidx.room.RoomDatabase
import com.bettor.medlinkduo.domain.BleDevice
import com.bettor.medlinkduo.domain.BleRepository
import com.bettor.medlinkduo.domain.Measurement
import com.bettor.medlinkduo.domain.TtsController
import javax.inject.Inject
import androidx.room.*


@Database(entities = [MeasurementEntity::class, FeedbackEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun measurementDao(): MeasurementDao
    abstract fun feedbackDao(): FeedbackDao
}

@Entity(tableName="measurement")
data class MeasurementEntity(@PrimaryKey val ts: Long, val value: String, val unit: String)
@Dao
interface MeasurementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(m: MeasurementEntity)
    @Query("SELECT * FROM measurement ORDER BY ts DESC LIMIT :n") suspend fun recent(n: Int): List<MeasurementEntity>
}

@Entity(tableName="feedback")
data class FeedbackEntity(@PrimaryKey(autoGenerate=true) val id: Long=0, val rating: Int, val category: String, val comment: String, val ts: Long)
@Dao interface FeedbackDao { @Insert suspend fun insert(f: FeedbackEntity) }

class StartScanUseCase @Inject constructor(private val repo: BleRepository) { suspend operator fun invoke() = repo.scan() }
class ConnectDeviceUseCase @Inject constructor(private val repo: BleRepository) { suspend operator fun invoke(d: BleDevice) = repo.connect(d) }
class ObserveMeasurementsUseCase @Inject constructor(private val repo: BleRepository) { operator fun invoke() = repo.measurements }
class SpeakMeasurementUseCase @Inject constructor(private val tts: TtsController) { operator fun invoke(m: Measurement) = tts.speak("${m.value} ${m.unit}") }
class SaveFeedbackUseCase @Inject constructor(private val dao: FeedbackDao) {
    suspend operator fun invoke(rating:Int, category:String, comment:String) =
        dao.insert(FeedbackEntity(rating=rating, category=category, comment=comment, ts=System.currentTimeMillis()))
}
