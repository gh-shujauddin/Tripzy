package com.qadri.tripzy.data

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.qadri.tripzy.domain.ApiResult
import com.qadri.tripzy.domain.LocationResult
import com.qadri.tripzy.domain.Recents
import com.qadri.tripzy.domain.Resource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface TripzyRepository {

    fun loginUser(email: String, password: String): Flow<Resource<AuthResult>>
    fun registerUser(email: String, password: String): Flow<Resource<AuthResult>>

    fun confirmUser(): Flow<Resource<Boolean>>
    fun googleSignIn(credential: AuthCredential): Flow<Resource<AuthResult>>
    suspend fun getSearchAutoComplete(string: String): Flow<ApiResult<LocationResult>>

    fun getAllRecentSearch(): Flow<List<Recents>>

    suspend fun addRecentSearch(recent: Recents)

    suspend fun deleteRecentSearch(recent: Recents)
}

class TripzyRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val tripzyDao: TripzyDao,
    private val firebaseAuth: FirebaseAuth
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

    //Getting Recent Searches
    override fun getAllRecentSearch(): Flow<List<Recents>> = tripzyDao.getAllRecentSearch()
    override suspend fun addRecentSearch(recent: Recents) = tripzyDao.addRecentSearch(recent)
    override suspend fun deleteRecentSearch(recent: Recents) = tripzyDao.deleteRecentSearch(recent)

}