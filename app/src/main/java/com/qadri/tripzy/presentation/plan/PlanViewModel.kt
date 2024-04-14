package com.qadri.tripzy.presentation.plan

import android.app.Application
import android.icu.util.Calendar
import android.util.Base64
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.qadri.tripzy.data.DatabaseRepository
import com.qadri.tripzy.data.TripsEntity
import com.qadri.tripzy.data.TripzyRepository
import com.qadri.tripzy.domain.ApiPrompt
import com.qadri.tripzy.domain.PalmApi
import com.qadri.tripzy.domain.Prompt
import com.qadri.tripzy.domain.getPlaceId.PlaceIdBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit
import javax.inject.Inject


enum class TimeSlot {
    MORNING,
    AFTERNOON,
    EVENING,
    NIGHT
}

@HiltViewModel
class PlanViewModel @Inject constructor(
    application: Application,
    private val repository: TripzyRepository,
    private val databaseRepository: DatabaseRepository
) : AndroidViewModel(application) {

    private val _imageState = MutableStateFlow<ApiState>(ApiState.NotStarted)
    val imageState: StateFlow<ApiState> = _imageState.asStateFlow()

    private val _message = MutableStateFlow("")
    private val _location = MutableStateFlow("")
    private val _budget = MutableStateFlow("")
    private val noOfDays = MutableStateFlow("")

    private val _arrivalDate = MutableStateFlow("")
    private val _arrivalTime = MutableStateFlow("")
    private val _departureDate = MutableStateFlow("")
    private val _departureTime = MutableStateFlow("")

    private val _data = MutableStateFlow(emptyList<Map<String, String>>())
    val data: StateFlow<List<Map<String, String>>> = _data.asStateFlow()

    private val _currentTime = MutableStateFlow<TimeSlot?>(null)
    val currentTime: StateFlow<TimeSlot?> = _currentTime.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private val _geoCodesData = MutableStateFlow(emptyList<TourDetails>().toMutableList())
    val geoCodesData: StateFlow<List<TourDetails>> = _geoCodesData.asStateFlow()

    fun getTrips(day: String, destination: String): Flow<List<TripsEntity?>> =
        databaseRepository.getTrips(day, destination)

    fun getDepartureDate(day: String, destination: String): Flow<List<String?>> =
        databaseRepository.getDepartureDate(day, destination)

    fun getArrivalDate(day: String, destination: String): Flow<List<String?>> =
        databaseRepository.getArrivalDate(day, destination)

    fun getMoreInfo(destination: String): Flow<List<TripsEntity?>> =
        databaseRepository.getMoreInfo(destination)

    val allTrips: Flow<List<TripsEntity?>> = databaseRepository.allTrips
    fun getCurrentTrip(destination: String): Flow<List<TripsEntity?>> =
        databaseRepository.getCurrentTrip(destination)

    fun uniqueDays(destination: String): Flow<List<String?>> =
        databaseRepository.distinctDays(destination)

    val tripName = mutableStateOf(TextFieldValue(""))
    val tripBudget = mutableStateOf(TextFieldValue(""))
    val tripNoOfDays = mutableStateOf(TextFieldValue(""))
    val tags = mutableStateListOf<String>()
    val travelMode = mutableStateListOf<String>()
    val source = mutableStateOf(TextFieldValue(""))
    val destination = mutableStateOf(TextFieldValue(""))
    val isAnimationVisible = mutableStateOf(false)

    val currentDestination = mutableStateOf("")
    val currentNewDestination = mutableStateOf("")
    val currentDay = mutableStateOf("")
    val currentTimeOfDay = mutableStateOf("")

    private val _result = MutableStateFlow("")
    val result: StateFlow<String> = _result.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userPfp = MutableStateFlow("")
    val userPfp: StateFlow<String> = _userPfp.asStateFlow()

    private val _gender = MutableStateFlow("")
    val gender: StateFlow<String> = _gender.asStateFlow()

    private val _userPhoneNumber = MutableStateFlow("")
    val userPhoneNumber: StateFlow<String> = _userPhoneNumber.asStateFlow()
    fun totalBudget(destination: String): Flow<List<Double?>> =
        databaseRepository.getTotalBudget(destination)

    private val _remainingBudget = MutableStateFlow<Double>(0.0)
    val remainingBudget: StateFlow<Double> = _remainingBudget.asStateFlow()


    private val _isReorderLoading = MutableStateFlow(false)
    val isReorderLoading: StateFlow<Boolean> = _isReorderLoading.asStateFlow()



    fun updateDates(
        arrivalDate: String,
        arrivalTime: String,
        departureDate: String,
        departureTime: String,
    ) {
        _arrivalDate.value = arrivalDate
        _arrivalTime.value = arrivalTime
        _departureDate.value = departureDate
        _departureTime.value = departureTime
    }


    fun calculateTimeSlotUpdates() {
        val currentTime = System.currentTimeMillis()
        val timeSlotFlow = MutableStateFlow(determineTimeSlot(currentTime))

        // Create a coroutine to update the time slot every 15 minutes
        val coroutine = determineTimeSlot(System.currentTimeMillis())
        println("coroutineeeee: $coroutine")
        _currentTime.value = coroutine
    }

    private fun determineTimeSlot(currentTime: Long): TimeSlot {
        val instant = Instant.ofEpochMilli(currentTime)
        val zoneOffset = ZoneOffset.UTC // Change this if you want to use a different time zone

        val localDateTime = LocalDateTime.ofInstant(instant, zoneOffset)
        println("localDateTimeeeee: $localDateTime")
        val hour = localDateTime.hour
        println("localDateTimeeeee 11: $hour")

        return when (getHourFromMillis(currentTime)) {
            in 9..11 -> TimeSlot.MORNING // 9 AM to 11 AM is morning
            in 12..16 -> TimeSlot.AFTERNOON // 12 PM to 4 PM is afternoon
            in 17..20 -> TimeSlot.EVENING // 5 PM to 8 PM is evening
            else -> TimeSlot.NIGHT // Other times are considered night
        }
    }

    private fun getHourFromMillis(systemTimeMillis: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = systemTimeMillis
        return calendar.get(Calendar.HOUR_OF_DAY)
    }


    fun getApiData() {
        viewModelScope.launch {
            println("messageeeee: ${_message.value}")
            withContext(Dispatchers.IO) {
                val apiData =
                    repository.getApiData(
                        ApiPrompt(
                            prompt = Prompt(
                                text = _message.value
                            )
                        )
                    )
                _imageState.value = ApiState.Loaded(apiData)
                println("dataaaaaaaaa message: ${_message.value}")
                println("dataaaaaaaaa value: ${apiData.candidates?.get(0)?.output ?: ""}")
                extractTourDetails(apiData.candidates?.get(0)?.output ?: "")
                println("dataaaaaaaaa: ${_data.value}")
                _data.value.forEachIndexed { index, location ->
                    val geoCodes = mutableMapOf<String, String>()
                    val day = location.getOrDefault("Day", "-2")
                    val locationName = location.getOrDefault("Name", "")
//                    if (locationName != "") {
                    val apiData =
                        repository.getGeocodingData(
                            query = "$locationName, ${_location.value}",
                        )
                    geoCodes["latitude"] =
                        apiData.items?.get(0)?.position?.lat?.toString() ?: ""
                    geoCodes["longitude"] =
                        apiData.items?.get(0)?.position?.lng?.toString() ?: ""
                    _geoCodesData.value[index].geoCode = GeoCode(
                        latitude = geoCodes["latitude"] ?: "",
                        longitude = geoCodes["longitude"] ?: ""
                    )
//                    }

                }
                _imageState.value = ApiState.ReceivedGeoCodes
                println("geoCodesDataaaaa: ${_geoCodesData.value}")
                _geoCodesData.value.forEachIndexed { index, tourDetails ->
                    if (index != 0) {
                        val apiData =
                            repository.getDistanceMatrix(
                                origins = _geoCodesData.value[index - 1].name,
                                destinations = _geoCodesData.value[index].name,
                            )
                        if (apiData.rows?.size!! > 0 && apiData.rows[0]?.elements?.size!! > 0) {
                            _geoCodesData.value[index].distance =
                                apiData.rows[0]?.elements?.get(0)?.distance?.text ?: "0 m"
                            _geoCodesData.value[index].duration =
                                apiData.rows[0]?.elements?.get(0)?.duration?.text ?: "0 hrs"
                        }
                    }
                }
                _imageState.value = ApiState.CalculatedDistance
                _geoCodesData.value.forEachIndexed { index, location ->
                    val apiData =
                        repository.getPlaceIdData(
                            PlaceIdBody(
                                textQuery = location.name
                            )
                        )
                    _geoCodesData.value[index].placeId = apiData.places?.get(0)?.id ?: ""
                    println("placeIddddd: ${_geoCodesData.value[index].placeId}")
                }
                _imageState.value = ApiState.ReceivedPlaceId
                _geoCodesData.value.forEachIndexed { index, location ->
                    val apiData =
                        repository.getPhotoId(
                            photoId = _geoCodesData.value[index].placeId ?: ""
                        )
                    _geoCodesData.value[index].photoID =
                        apiData.result?.photos?.get(0)?.photo_reference ?: ""
                    println("photoIddddd: ${apiData.result}")
                    println("photoIddddd 111: ${_geoCodesData.value[index].placeId}")
                }
                _imageState.value = ApiState.ReceivedPhotoId
                _geoCodesData.value.forEachIndexed { index, location ->
                    val apiData =
                        repository.getPhoto(
                            photoReference = _geoCodesData.value[index].photoID ?: "",
                            maxWidth = 1200,
                        )
                    _geoCodesData.value[index].photo = apiData
                }
                databaseRepository.insertAllTrips(_geoCodesData.value.take(8).map {
                    TripsEntity(
                        day = it.day,
                        timeOfDay = it.timeOfDay,
                        name = it.name,
                        budget = it.budget,
                        latitude = it.geoCode?.latitude?.toDouble(),
                        longitude = it.geoCode?.longitude?.toDouble(),
                        photoBase64 = byteArrayToBase64(it.photo ?: ByteArray(0)),
                        source = source.value.text,
                        destination = destination.value.text,
                        travelActivity = "",
                        distance = it.distance,
                        duration = it.duration,
                        totalBudget = tripBudget.value.text.toDoubleOrNull(),
                        departureDate = _departureDate.value,
                        arrivalDate = _arrivalDate.value,
                        departureTime = _departureTime.value,
                        arrivalTime = _arrivalTime.value,
                    )
                })
                _imageState.value = ApiState.ReceivedPhoto
                _geoCodesData.value = mutableListOf()
            }
        }
    }

    fun calculateProgress() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val totalAvailableTimeMinutes = TimeUnit.HOURS.toMinutes(12)
        val elapsedMinutes =
            ((currentHour - 9) * 60) + currentMinute

        _progress.value = (elapsedMinutes.toFloat() / totalAvailableTimeMinutes).coerceIn(0f, 1f)
    }

    private suspend fun getGeoCodes() {
        _data.value.forEachIndexed { index, location ->
            val geoCodes = mutableMapOf<String, String>()
            val day = location.getOrDefault("Day", "-2")
            if (day != "-2") {
                val locationName = location.getOrDefault("Name", "")
                if (locationName != "") {
                    val apiData =
                        repository.getGeocodingData(
                            query = "$locationName, ${_location.value}",
                        )
                    geoCodes["latitude"] =
                        apiData.items?.get(0)?.position?.lat?.toString() ?: ""
                    geoCodes["longitude"] =
                        apiData.items?.get(0)?.position?.lng?.toString() ?: ""
                    _geoCodesData.value[index].geoCode = GeoCode(
                        latitude = geoCodes["latitude"] ?: "",
                        longitude = geoCodes["longitude"] ?: ""
                    )
                }
            }

        }
        _imageState.value = ApiState.ReceivedGeoCodes
    }

    private suspend fun getPlaceId() {
        _geoCodesData.value.forEachIndexed { index, location ->
            val apiData =
                repository.getPlaceIdData(
                    PlaceIdBody(
                        textQuery = location.name
                    )
                )
            _geoCodesData.value[index].placeId = apiData.places?.get(0)?.id ?: ""
            println("placeIddddd: ${_geoCodesData.value[index].placeId}")
        }
        _imageState.value = ApiState.ReceivedPlaceId
    }

    private suspend fun getPhoto() {
        _geoCodesData.value.forEachIndexed { index, location ->
            val apiData =
                repository.getPhoto(
                    photoReference = _geoCodesData.value[index].photoID ?: "",
                    maxWidth = 1200,
                )
            _geoCodesData.value[index].photo = apiData
        }
    }

    fun swapTripPositions(day: String, fromIndex: Int, toIndex: Int, destination: String) {
        viewModelScope.launch {
            databaseRepository.swapTripPositions(day, fromIndex, toIndex, destination)
        }
    }

    fun updateTrips(
        name: String, budget: String?, latitude: Double?, longitude: Double?,
        photoBase64: String?, distance: String, duration: String, timeOfDay: String,
        fromId: Long, fromDay: String, fromDestination: String,
    ) {
        viewModelScope.launch {
            databaseRepository.updateTrips(
                name,
                budget,
                latitude,
                longitude,
                photoBase64,
                distance,
                duration,
                timeOfDay,
                fromId,
                fromDay,
                fromDestination
            )
        }
    }

    fun updateTripsWithDistance(
        newTripsEntity: MutableList<TripsEntity?>,
        oldTrips: MutableList<TripsEntity?>, fromDay: String, fromDestination: String,
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _isReorderLoading.value = true
                val tempList = mutableListOf<TripsEntity>()
                newTripsEntity.forEach {
                    if (it != null) {
                        tempList.add(it)
                    }
                }
                newTripsEntity.forEachIndexed { index, tourDetails ->
                    if (index != 0) {
                        println("fromDistancee = ${newTripsEntity[index - 1]?.name}, toooo = ${newTripsEntity[index]?.name}")
                        val apiData =
                            repository.getDistanceMatrix(
                                origins = "${newTripsEntity[index - 1]?.name}",
                                destinations = "${newTripsEntity[index]?.name}",
                            )
                        tempList[index].distance =
                            apiData.rows?.get(0)?.elements?.get(0)?.distance?.text ?: "0 m"
                        tempList[index].duration =
                            apiData.rows?.get(0)?.elements?.get(0)?.duration?.text ?: "0 hrs"

                        println(
                            "fromDistanceeApidata = ${apiData.rows?.get(0)?.elements?.get(0)?.distance?.text}, " +
                                    "toooo = ${apiData.rows?.get(0)?.elements?.get(0)?.duration?.text}"
                        )

                        println(
                            "fromDistanceeValue = ${newTripsEntity[index - 1]?.name}, " +
                                    "toooo = ${newTripsEntity[index]?.name}," +
                                    " distance = ${tempList[index].distance}, " +
                                    "duration = ${tempList[index].duration}"
                        )
                    }
                }
                newTripsEntity.forEachIndexed { index, it ->
                    databaseRepository.updateTrips(
                        it?.name ?: "",
                        it?.budget ?: "",
                        it?.latitude,
                        it?.longitude,
                        it?.photoBase64,
                        tempList[index].distance ?: "",
                        tempList[index].duration ?: "",
                        oldTrips[index]?.timeOfDay ?: "",
                        oldTrips[index]?.id ?: 0,
                        fromDay,
                        fromDestination
                    )
                }
                _isReorderLoading.value = false
            }
        }
    }

    fun addTripToDatabase(tripsEntity: List<TripsEntity?>) {
        viewModelScope.launch {
            val tempList = mutableListOf<TripsEntity>()
            tripsEntity.forEach {
                if (it != null) {
                    tempList.add(it)
                }
            }
            databaseRepository.insertAllTrips(tempList)
        }

//            _geoCodesData.value.forEachIndexed { _, location ->
//                databaseRepository.insertTrip(
//                    TripsEntity(
//                        day = location.day,
//                        timeOfDay = location.timeOfDay,
//                        name = location.name,
//                        budget = location.budget,
//                        latitude = location.geoCode?.latitude?.toDouble(),
//                        longitude = location.geoCode?.longitude?.toDouble(),
//                        photoBase64 = byteArrayToBase64(location.photo ?: ByteArray(0)),
//                        source = source.value.text,
//                        destination = destination.value.text,
//                        travelActivity = "",
//                    )
//                )
//            }
    }

    private fun byteArrayToBase64(byteArray: ByteArray): String {
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private suspend fun getPhotoId() {
        _geoCodesData.value.forEachIndexed { index, location ->
            val apiData =
                repository.getPhotoId(
                    photoId = _geoCodesData.value[index].placeId ?: ""
                )
            _geoCodesData.value[index].photoID =
                apiData.result?.photos?.get(0)?.photo_reference ?: ""
            println("photoIddddd: ${apiData.result}")
            println("photoIddddd 111: ${_geoCodesData.value[index].placeId}")
        }
        _imageState.value = ApiState.ReceivedPhotoId
    }

    fun updateMessage(message: String, location: String, noOfDays: String) {
        _location.value = location
        this.noOfDays.value = noOfDays
        _message.value = message
        _imageState.value = ApiState.Loading
    }

    fun extractBudgetValue(destination: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _remainingBudget.value = 0.0
                databaseRepository.getBudget(destination).collectLatest { budgets ->
                    println("budgetssss: $budgets")
                    budgets.forEach {
                        val regex = Regex("[^\\d]")
                        val output = it?.replace(regex, "")
                        println("budgetssss: $output")
                        _remainingBudget.value +=
                            output?.toDoubleOrNull() ?: 0.0

                    }
                }
            }

        }
    }

    private fun extractTourDetails(output: String) {
        val tourDetails = mutableListOf<Map<String, String>>()

        val days = """Day (\d+) ([A-Za-z]+)""".toRegex(
            options = setOf(
                RegexOption.IGNORE_CASE
            )
        ).findAll(output)

        val names = """Name:(.{0,50})""".toRegex(
            options = setOf(
                RegexOption.IGNORE_CASE
            )
        ).findAll(output)

        val budgets = """Budget:(.{0,20})""".toRegex(
            options = setOf(
                RegexOption.IGNORE_CASE
            )
        ).findAll(output)

        println("Output: $output")

        println(
            "daysssss: ${
                days.forEachIndexed { index, matchResult ->
                    println("daysssss: $index, matchResult: ${matchResult.groupValues}")
                }
            }"
        )

        println(
            "names: ${
                names.forEachIndexed { index, matchResult ->
                    println("names: $index, matchResult: ${matchResult.groupValues}")
                }
            }"
        )

        println(
            "btssss: ${
                budgets.forEachIndexed { index, matchResult ->
                    println("btssss: $index, matchResult: ${matchResult.groupValues}")
                }
            }"
        )


        val namesList = names.map { it.groupValues[1] }.toList()
        val budgetsList = budgets.map { it.groupValues[1] }.toList()

        println("namesList: $namesList")
        println("budgetsList: $budgetsList")
        days.forEachIndexed { index, dayMatch ->
            val dayNumber = dayMatch.groupValues[1]
            val timeOfDay = dayMatch.groupValues[2]

            val dayInfo = mutableMapOf<String, String>()
            dayInfo["Day"] = dayNumber
            dayInfo["Time of Day"] = timeOfDay

            // Check if the index exists in namesList before accessing
            if (index < namesList.size) {
                dayInfo["Name"] = namesList[index]
            } else {
                // Handle the case where the index does not exist, e.g., by setting a default value or skipping
                dayInfo["Name"] = "Default Name" // or any other default value or handling
            }

            // Check if the index exists in budgetsList before accessing
            if (index < budgetsList.size) {
                dayInfo["Budget"] = budgetsList[index]
            } else {
                // Handle the case where the index does not exist
                dayInfo["Budget"] = "Default Budget" // or any other default value or handling
            }

            tourDetails.add(dayInfo)
            _geoCodesData.value.add(
                TourDetails(
                    day = dayNumber,
                    timeOfDay = timeOfDay,
                    name = dayInfo["Name"] ?: "",
                    budget = dayInfo["Budget"] ?: ""
                )
            )
        }
        println("Tourrr detail: $tourDetails")
        _data.value = tourDetails

    }


}

sealed class ApiState {
    object Loading : ApiState()
    data class Loaded(val data: PalmApi) : ApiState()

    data class Error(val exception: Exception) : ApiState()

    object NotStarted : ApiState()
    object ReceivedGeoCodes : ApiState()
    object ReceivedPlaceId : ApiState()
    object ReceivedPhotoId : ApiState()
    object CalculatedDistance : ApiState()

    object ReceivedPhoto : ApiState()
}

data class GeoCode(
    val latitude: String,
    val longitude: String,
)

data class TourDetails(
    val day: String,
    val timeOfDay: String,
    val name: String,
    val budget: String,
    var geoCode: GeoCode? = null,
    var placeId: String? = null,
    var photoID: String? = null,
    var photo: ByteArray? = null,
    var distance: String = "0",
    var duration: String = "0",
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TourDetails

        if (photo != null) {
            if (other.photo == null) return false
            if (!photo.contentEquals(other.photo)) return false
        } else if (other.photo != null) return false

        return true
    }

    override fun hashCode(): Int {
        return photo?.contentHashCode() ?: 0
    }
}

fun parseStringToDouble(str: String?): Double? {
    return try {
        str?.toDouble()
    } catch (e: NumberFormatException) {
        null
    }
}