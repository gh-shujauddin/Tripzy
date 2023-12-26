package com.qadri.tripzy.network

import com.qadri.tripzy.data.TripzyDao
import com.qadri.tripzy.domain.ApiResult
import com.qadri.tripzy.domain.LocationResult
import com.qadri.tripzy.domain.Recents
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface TripzyRepository {
    suspend fun getSearchAutoComplete(string: String): Flow<ApiResult<LocationResult>>

    fun getAllRecentSearch(): Flow<List<Recents>>

    suspend fun addRecentSearch(recent: Recents)
}

class TripzyRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val tripzyDao: TripzyDao
) : TripzyRepository {
    override suspend fun getSearchAutoComplete(string: String): Flow<ApiResult<LocationResult>> =
        flow {
            emit(ApiResult.Loading())
            try {
                emit(
                    ApiResult.Success(
                        httpClient.get("/locations/v2/auto-complete") {
                            parameter("query", string)
                            parameter("lang", "en_US")
                            parameter("units", "km")
                        }.body()
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                emit(ApiResult.Error(e.message ?: "Something went wrong"))
            }
        }

    //Getting Recent Searches
    override fun getAllRecentSearch(): Flow<List<Recents>> = tripzyDao.getAllRecentSearch()
    override suspend fun addRecentSearch(recent: Recents) = tripzyDao.addRecentSearch(recent)

}