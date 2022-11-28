package com.example.dalsocial.model.events

import android.os.Parcelable
import java.util.*

data class Event(
    var eventId: String? = null,
    var title: String? = null,
    var description: String? = null,
    var scheduledDate: String? = null,
    var imageUrl: String? = "https://images.pexels.com/photos/1629236/pexels-photo-1629236.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
    var userId: String? = null
)