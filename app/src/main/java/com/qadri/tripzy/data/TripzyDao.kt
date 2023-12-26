package com.qadri.tripzy.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.qadri.tripzy.domain.Recents
import kotlinx.coroutines.flow.Flow

@Dao
interface TripzyDao {

    @Query("select * from recent_search")
    fun getAllRecentSearch(): Flow<List<Recents>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRecentSearch(recent: Recents)
}