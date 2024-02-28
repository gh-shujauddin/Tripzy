package com.qadri.tripzy.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.qadri.tripzy.data.TripzyDao
import com.qadri.tripzy.data.TripzyDatabase
import com.qadri.tripzy.data.TripzyRepository
import com.qadri.tripzy.data.TripzyRepositoryImpl
import com.qadri.tripzy.ui.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseDatabase() = FirebaseDatabase.getInstance()

    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    fun providesHttpClient(): HttpClient {
        return HttpClient(Android) {
            // Logging
            install(Logging) {
                level = LogLevel.ALL
            }
            install(DefaultRequest) {
                url(BASE_URL)
                header("X-RapidAPI-Key", "b0513957c3msh8fd3217e1a62e69p15c819jsn1085ad8d1abf")
                header("X-RapidAPI-Host", "travel-advisor.p.rapidapi.com")
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        explicitNulls = false
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }

    @Singleton
    @Provides
    fun provideDao(db: TripzyDatabase) = db.getTripzyDao()

    @Singleton
    @Provides
    fun provideApiService(httpClient: HttpClient, dao: TripzyDao, firebaseAuth: FirebaseAuth, firebaseDatabase: FirebaseDatabase, @ApplicationContext context: Context): TripzyRepository =
        TripzyRepositoryImpl(httpClient, dao, firebaseAuth, firebaseDatabase, context)


    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(
            app,
            TripzyDatabase::class.java,
            "tripzy_db"
        ).build()

}