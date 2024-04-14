package com.qadri.tripzy.data

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.storage
import com.qadri.tripzy.BuildConfig
import com.qadri.tripzy.domain.ApiPrompt
import com.qadri.tripzy.domain.ApiResult
import com.qadri.tripzy.domain.Candidate
import com.qadri.tripzy.domain.GetDetailModel
import com.qadri.tripzy.domain.GetPlacePhotos
import com.qadri.tripzy.domain.GetRecommendedPlace
import com.qadri.tripzy.domain.LocationResult
import com.qadri.tripzy.domain.PalmApi
import com.qadri.tripzy.domain.Recents
import com.qadri.tripzy.domain.Resource
import com.qadri.tripzy.domain.distanceMatrix.DistanceMatrixResponse
import com.qadri.tripzy.domain.geocoding.GeoCodes
import com.qadri.tripzy.domain.getPhotoId.PhotoIdResponse
import com.qadri.tripzy.domain.getPlaceId.PlaceIdBody
import com.qadri.tripzy.domain.getPlaceId.PlaceIdResponse
import com.qadri.tripzy.domain.hereSearch.HereSearchResponse
import com.qadri.tripzy.presentation.account.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.util.InternalAPI
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.net.URLEncoder
import java.util.UUID
import javax.inject.Inject

interface TripzyRepository {

    fun loginUser(email: String, password: String): Flow<Resource<AuthResult>>
    fun registerUser(email: String, password: String): Flow<Resource<AuthResult>>

    fun confirmUser(): Flow<Resource<Boolean>>

    fun getCurrentUser(): Flow<Resource<UserResponse>>

    fun addUserDetail(
        name: String,
        address: String,
        image: ByteArray
    ): Flow<Resource<String>>

    fun googleSignIn(credential: AuthCredential): Flow<Resource<AuthResult>>
    suspend fun getSearchAutoComplete(string: String): Flow<ApiResult<LocationResult>>

    suspend fun getPlaceDetail(id: Int): Flow<ApiResult<GetDetailModel>>

    suspend fun getPlacePhotos(id: Int): Flow<ApiResult<GetPlacePhotos>>

    suspend fun getRecommendedPlace(): Flow<ApiResult<GetRecommendedPlace>>

    suspend fun getRankedPlace(): Flow<ApiResult<GetRecommendedPlace>>

    fun getAllRecentSearch(): Flow<List<Recents>>

    suspend fun addRecentSearch(recent: Recents)

    suspend fun deleteRecentSearch(recent: Recents)


    suspend fun getApiData(apiPrompt: ApiPrompt): PalmApi

    suspend fun getGeocodingData(query: String): GeoCodes

    suspend fun getPlaceIdData(text: PlaceIdBody): PlaceIdResponse
    suspend fun getPhotoId(photoId: String): PhotoIdResponse

    suspend fun getPhoto(
        photoReference: String,
        maxWidth: Int,
        maxHeight: Int = 0,
    ): ByteArray

    suspend fun hereSearch(
        query: String,
        latitude: Double,
        longitude: Double,
        limit: Int = 6,
    ): HereSearchResponse

    suspend fun getDistanceMatrix(
        origins: String,
        destinations: String,
        units: String = "imperial",
    ): DistanceMatrixResponse
}

class TripzyRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val tripzyDao: TripzyDao,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
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


    override fun getCurrentUser(): Flow<Resource<UserResponse>> = callbackFlow {
        trySend(Resource.Loading())
        val currentUserUid = firebaseAuth.currentUser?.uid
        if (currentUserUid != null) {
            firestore.collection("users").document(currentUserUid).get()
                .addOnSuccessListener { mySnapshot ->
                    if (mySnapshot.exists()) {
                        val data = mySnapshot.data

                        if (data != null) {
                            val userResponse = UserResponse(
                                key = currentUserUid,
                                item = UserResponse.CurrentUser(
                                    name = data["name"] as String? ?: "",
                                    email = data["email"] as String? ?: "",
                                    address = data["address"] as String? ?: "",
                                    profileImage = data["image"] as String? ?: ""
                                )
                            )

                            trySend(Resource.Success(userResponse))
                        } else {
                            trySend(Resource.Error(message = "No data found in Database"))

                            println("No data found in Database")
                        }
                    } else {
                        trySend(Resource.Error(message = "No data found in Database"))
                        println("No data found in Database")
                    }
                }.addOnFailureListener { e ->
                    Log.d("ERRor", e.toString())
                    trySend(Resource.Error(message = e.toString()))
                }
        } else {
            trySend(Resource.Error(message = "User not signed up"))
        }
        awaitClose {
            close()
        }
    }


    override fun addUserDetail(
        name: String,
        address: String,
        image: ByteArray
    ): Flow<Resource<String>> =
        callbackFlow {
            trySend(Resource.Loading())
            val storageRef = Firebase.storage.reference
            val uuid = UUID.randomUUID()
            val imagesRef = storageRef.child("images/$uuid")
            val currentUserUid = firebaseAuth.currentUser?.uid

            val uploadTask =
                image.let {
                    imagesRef.putBytes(it)
                }

            uploadTask.addOnSuccessListener {
                imagesRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        val map = HashMap<String, Any>()
                        map["name"] = name
                        map["address"] = address
                        map["image"] = uri.toString()
                        if (currentUserUid != null) {
                            firestore.collection("users")
                                .document(currentUserUid)
                                .set(map, SetOptions.merge())
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        trySend(Resource.Success("Updated Successfully.."))
                                    }
                                }
                                .addOnFailureListener { e ->
                                    trySend(Resource.Error(message = e.message))
                                }
                        } else {
                            trySend(Resource.Error(message = "User not logged in"))
                        }
                    }
                    .addOnFailureListener {
                        trySend(Resource.Error(message = "Updating user failed Successfully: $it"))
                    }
            }.addOnFailureListener {
                trySend(Resource.Error(message = "Image upload failed Successfully: $it"))
            }
            awaitClose { close() }
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
                            parameter("sort", "ranked")
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


    override suspend fun getApiData(apiPrompt: ApiPrompt): PalmApi {
        return try {
            httpClient.post {
                url("${ApiRoutes.BASE_URL}?key=${BuildConfig.API_KEY}")
                setBody(apiPrompt)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }.body<PalmApi>()
        } catch (e: Exception) {
            Log.i("ApiException", e.message.toString())
            return PalmApi(
                candidates = listOf(
                    Candidate(
                        output = e.message.toString(),
                        safetyRatings = null
                    )
                )
            )
        }

    }

    override suspend fun getGeocodingData(query: String): GeoCodes {
        return try {
            httpClient.get {
                val encodedLocation = URLEncoder.encode(query, "UTF-8")
                url("${ApiRoutes.Geocoding_URL}?q=$encodedLocation&apiKey=${BuildConfig.Here_API_KEY}")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                headers {
                    append("Accept", "*/*")
                    append("Content-Type", "application/json")
                }
            }.body()
        } catch (e: Exception) {
            Log.i("ApiException", e.message.toString())
            return GeoCodes(
                items = null
            )
        }
    }

    override suspend fun getPlaceIdData(text: PlaceIdBody): PlaceIdResponse {
        return try {
            httpClient.post {
                url(ApiRoutes.getPlaceIdApi)
                setBody(text)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                headers {
                    append("Content-Type", "application/json")
                    append("X-Goog-Api-Key", BuildConfig.Places_API_KEY)
                    append(
                        "X-Goog-FieldMask",
                        "places.id,places.displayName,places.formattedAddress"
                    )
                }
            }.body()
        } catch (e: Exception) {
            Log.i("ApiException", e.message.toString())
            return PlaceIdResponse(
                places = null
            )
        }
    }

    override suspend fun getPhotoId(photoId: String): PhotoIdResponse {
        return try {
            httpClient.get {
                println("photoIdooooo: $photoId")
                val encodedLocation = URLEncoder.encode(photoId, "UTF-8")
                url("${ApiRoutes.getPhotoIdApi}?placeid=$encodedLocation&key=${BuildConfig.Places_API_KEY}")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                headers {
                    append("Accept", "*/*")
                    append("Content-Type", "application/json")
                }
            }.body()
        } catch (e: Exception) {
            Log.i("ApiException", e.message.toString())
            return PhotoIdResponse(
                result = null,
                status = null,
            )
        }
    }

    override suspend fun getPhoto(
        photoReference: String,
        maxWidth: Int,
        maxHeight: Int,
    ): ByteArray {
        try {
            println("photooooo 111: $photoReference")
            val a = httpClient.get {
                val encodedLocation = URLEncoder.encode(photoReference, "UTF-8")
                url("${ApiRoutes.getPhoto}?maxwidth=$maxWidth&photo_reference=$encodedLocation&key=${BuildConfig.Places_API_KEY}")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                headers {
                    append("Accept", "*/*")
                    append("Content-Type", "application/json")
                }
            }.body<ByteArray>()
            println("photooooo: $a")
            return a
        } catch (e: Exception) {
            Log.i("ApiException", e.message.toString())
            return ByteArray(0)
        }
    }

    override suspend fun hereSearch(
        query: String,
        latitude: Double,
        longitude: Double,
        limit: Int,
    ): HereSearchResponse {
        try {
            val a = httpClient.get {
                val encodedLocation = URLEncoder.encode(query, "UTF-8")
                url(
                    "${ApiRoutes.hereSearch}?at=$latitude,$longitude&q=$encodedLocation" +
                            "&lang=en&apiKey=${BuildConfig.Here_API_KEY}"
                )
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                headers {
                    append("Accept", "*/*")
                    append("Content-Type", "application/json")
                }
            }.body<HereSearchResponse>()
            println("photooooo: $a")
            return a
        } catch (e: Exception) {
            Log.i("ApiException", e.message.toString())
            return HereSearchResponse(
                items = null,
            )
        }
    }

    override suspend fun getDistanceMatrix(
        origins: String,
        destinations: String,
        units: String,
    ): DistanceMatrixResponse {
        try {
            println("$origins oringin $destinations destina")
            val a = httpClient.get {
                val encodedLocation = URLEncoder.encode(origins, "UTF-8")
                val encodedLocation2 = URLEncoder.encode(destinations, "UTF-8")
                url("${ApiRoutes.distanceMatrix}?origins=$encodedLocation&destinations=$encodedLocation2&units=$units&key=${BuildConfig.Places_API_KEY}")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                headers {
                    append("Accept", "*/*")
                    append("Content-Type", "application/json")
                }
            }.body<DistanceMatrixResponse>()
            println("photooooo: $a")
            return a
        } catch (e: Exception) {
            Log.i("ApiException", e.message.toString())
            return DistanceMatrixResponse(
                rows = null,
                status = null,
                destinationAddresses = null,
                originAddresses = null,
            )
        }
    }
}