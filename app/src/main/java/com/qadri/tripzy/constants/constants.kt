package com.qadri.tripzy.constants

import com.qadri.tripzy.R
import com.qadri.tripzy.presentation.navigation.BottomNavigationScreens

val bottomNavigationItems = listOf(
    BottomNavigationScreens.Explore,
    BottomNavigationScreens.Search,
    BottomNavigationScreens.Plan,
    BottomNavigationScreens.Account
)

data class Category(
    val imgResId: Int,
    val title: String
)

val categoryList = listOf(
    Category(
        imgResId = R.drawable.lake,
        title = "Beach"
    ),
    Category(
        imgResId = R.drawable.adventure,
        title = "Adventure"
    ),
    Category(
        imgResId = R.drawable.historic,
        title = "Historic"
    ),
    Category(
        imgResId = R.drawable.nature,
        title = "Nature"
    ),
    Category(
        imgResId = R.drawable.trip,
        title = "Trip"
    ),
    Category(
        imgResId = R.drawable.lake,
        title = "Lake"
    )
)

data class SliderList(
    val link: String,
    val title: String,
    val place: String,
    val isBookmarked: Boolean
)

val placesList = listOf(
    SliderList(
        "https://picsum.photos/id/239/500/800",
        title = "Title 1",
        place = "place 1",
        isBookmarked = false
    ),
    SliderList(
        "https://picsum.photos/id/240/500/800",
        title = "Title 2",
        place = "place 2",
        isBookmarked = true
    ),
    SliderList(
        "https://picsum.photos/id/241/500/800",
        title = "Title 3",
        place = "place 3",
        isBookmarked = false
    ),
    SliderList(
        "https://picsum.photos/id/242/500/800",
        title = "Title 4",
        place = "place 4",
        isBookmarked = true
    ),
    SliderList(
        "https://picsum.photos/id/243/500/800",
        title = "Title 5",
        place = "place 5",
        isBookmarked = false
    )
)

val Cities = listOf(
    City(
        name = "New Delhi",
        link = "https://globalgrasshopper.com/wp-content/uploads/2011/01/Mumbai-India-scaled.jpg"
    ),
    City(
        name = "Udaipur",
        link = "https://img.theculturetrip.com/1440x/smart/wp-content/uploads/2015/05/shutterstock_753879010.jpg"
    ),
    City(
        name = "Delhi",
        link = "https://www.mistay.in/travel-blog/content/images/2020/07/travel-4813658_1920.jpg"
    ),
    City(
        name = "Hyderabad",
        link = "https://th.bing.com/th/id/R.5bb9c2541fda824341b2308ce67a443f?rik=l0okElxFfj4b9Q&riu=http%3a%2f%2fwww.worldblaze.in%2fwp-content%2fuploads%2f2016%2f03%2fHyderabad.jpg&ehk=PT3HFKerzLbDo5DkU5jSwxNdzlAH7agZE3tf%2bh0gvks%3d&risl=&pid=ImgRaw&r=0&sres=1&sresct=1"
    ),
    City(
        name = "Jaipur",
        link = "https://img.theculturetrip.com/1440x/wp-content/uploads/2015/05/hctp0011-prakash-india-jaipur-6.jpg"
    ),
    City(
        name = "Agra",
        link = "https://www.jetsetter.com/uploads/sites/7/2018/04/y7lEy9T7-1380x1035.jpeg"
    )
)

data class City(
    val name: String,
    val link: String
)


val places = listOf(
    Place(
        name = "Taj Mahal",
        link = listOf(
            "https://www.jetsetter.com/uploads/sites/7/2018/04/y7lEy9T7-1380x1035.jpeg",
            "https://1.bp.blogspot.com/-3mUa4Z5ria0/ToRV3GwN0GI/AAAAAAAAAQ8/kA71S1kIesE/s1600/Taj+Mahal.jpg"
        ),
        locationName = "Agra",
        description = "The Taj Mahal is located in Agra, India. Agra is a small city in North India a few hours drive or 200 kilometers (125 miles) from the capital of New Delhi. Agra is often visited on the Golden Triangle travel route which includes the most popular stops in India: Delhi, Agra, and Jaipur. \n" +
                "\n" +
                "Agra was a very important city during the Mughal rule of North India, but since then has a decrease in political importance and today only has 1.7 million people. The Taj Mahal is located to the east of the city near the banks of the holy Yamuna River. ",
        rating = 4.0,
        totalReviews = 445,
        budgetPerDay = 2000,
        tags = listOf("Culture", "Historical"),
        averageTemperature = 20.5
    )
)

data class Place(
    val name: String,
    val link: List<String>,
    val locationName: String,
    val description: String,
    val rating: Double,
    val totalReviews: Int = 0,
    val budgetPerDay: Int = 0,
    val tags: List<String> = listOf(),
    val averageTemperature: Double = 0.0,
    val distance: Double = 0.0
)