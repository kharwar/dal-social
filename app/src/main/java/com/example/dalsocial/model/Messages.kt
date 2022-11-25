package com.example.dalsocial.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Messages(
    var fromUserId: String?=null,
    var toUserId: String?=null,
    var messsage: String?=null,
    @ServerTimestamp
    var timestamp: Date?=null
)
