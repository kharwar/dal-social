package com.example.dalsocial.model.chat

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Messages(
    var fromUserId: String?=null,
    var toUserId: String?=null,
    var message: String?=null,
    @ServerTimestamp
    var timestamp: Date?=null
)
