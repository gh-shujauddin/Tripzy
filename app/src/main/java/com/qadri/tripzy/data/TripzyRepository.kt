package com.qadri.tripzy.data

import android.content.Context
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.GeoPoint
import com.qadri.tripzy.domain.ApiResult
import com.qadri.tripzy.domain.GetPlacePhotos
import com.qadri.tripzy.domain.LocationResult
import com.qadri.tripzy.domain.Recents
import com.qadri.tripzy.domain.Resource
import com.qadri.tripzy.domain.UserData
import com.qadri.tripzy.domain.GetDetailModel
import com.qadri.tripzy.domain.GetRecommendedPlace
import com.qadri.tripzy.domain.RecommendedPlace
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.InternalAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface TripzyRepository {

    fun loginUser(email: String, password: String): Flow<Resource<AuthResult>>
    fun registerUser(email: String, password: String): Flow<Resource<AuthResult>>

    fun confirmUser(): Flow<Resource<Boolean>>

    fun addUserDetail(name: String, latitude: Double, longitude: Double): Flow<Resource<Void>>
    fun googleSignIn(credential: AuthCredential): Flow<Resource<AuthResult>>
    suspend fun getSearchAutoComplete(string: String): Flow<ApiResult<LocationResult>>

    suspend fun getPlaceDetail(id: Int): Flow<ApiResult<GetDetailModel>>

    suspend fun getPlacePhotos(id: Int): Flow<ApiResult<GetPlacePhotos>>

    suspend fun getRecommendedPlace(): Flow<ApiResult<GetRecommendedPlace>>

    suspend fun getRankedPlace(): Flow<ApiResult<GetRecommendedPlace>>

    fun getAllRecentSearch(): Flow<List<Recents>>

    suspend fun addRecentSearch(recent: Recents)

    suspend fun deleteRecentSearch(recent: Recents)
}

class TripzyRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val tripzyDao: TripzyDao,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val context: Context
) : TripzyRepository {

    override fun loginUser(email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun confirmUser(): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            val user = firebaseAuth.currentUser
            user?.sendEmailVerification()?.await()
            emit(Resource.Success(true))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }

    }

    override fun addUserDetail(
        name: String,
        latitude: Double,
        longitude: Double
    ): Flow<Resource<Void>> {
        return flow {
            emit(Resource.Loading())
            val geopoint = GeoPoint(latitude, longitude)
            val uid = firebaseAuth.currentUser?.uid
            val databaseRef = firebaseDatabase.getReference("User")
            if (uid != null) {
                val user = UserData(userId = uid, username = name, location = geopoint)
                val result = databaseRef.child(uid).setValue(user).await()
                emit(Resource.Success(result))
            } else {
                emit(Resource.Error("User not logged in"))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }

    }

    override fun registerUser(email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun googleSignIn(credential: AuthCredential): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithCredential(credential).await()
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

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

    @OptIn(InternalAPI::class)
    override suspend fun getPlaceDetail(id: Int): Flow<ApiResult<GetDetailModel>> =
        flow {
            emit(ApiResult.Loading())
            try {
                emit(
                    ApiResult.Success(
                        httpClient.get("/attractions/get-details") {
                            parameter("location_id", id)
                            parameter("currency", "INR")
                            parameter("lang", "en_US")
                        }.body()
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                emit(ApiResult.Error(e.message ?: "Something went wrong"))
            }
        }

    override suspend fun getPlacePhotos(id: Int): Flow<ApiResult<GetPlacePhotos>> =
        flow {
            emit(ApiResult.Loading())
            try {
                emit(
                    ApiResult.Success(
                        httpClient.get("/photos/list") {
                            parameter("location_id", id)
                            parameter("currency", "INR")
                            parameter("lang", "en_US")
                        }.body()
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                emit(ApiResult.Error(e.message ?: "Something went wrong"))
            }
        }

    override suspend fun getRecommendedPlace(): Flow<ApiResult<GetRecommendedPlace>> =
        flow {
            emit(ApiResult.Loading())
            try {
                emit(
                    ApiResult.Success(
                        httpClient.get("/attractions/list") {
                            parameter("location_id", 304551)
                            parameter("currency", "INR")
                            parameter("lang", "en_US")
                            parameter("lunit", "km")
                            parameter("sort", "recommended")
                        }.body()
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                emit(ApiResult.Error(e.message ?: "Something went wrong"))
            }
        }

    override suspend fun getRankedPlace(): Flow<ApiResult<GetRecommendedPlace>> =
        flow {
            emit(ApiResult.Loading())
            try {
                emit(
                    ApiResult.Success(
                        httpClient.get("/attractions/list") {
                            parameter("location_id", 297618)
                            parameter("currency", "INR")
                            parameter("lang", "en_US")
                            parameter("lunit", "km")
                            parameter("sort", "recommended")
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
    override suspend fun deleteRecentSearch(recent: Recents) = tripzyDao.deleteRecentSearch(recent)

}