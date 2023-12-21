package com.qadri.tripzy.constants

import com.qadri.tripzy.R
import com.qadri.tripzy.data.BottomNavigationScreens

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