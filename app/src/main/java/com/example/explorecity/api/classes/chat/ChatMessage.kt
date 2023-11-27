package com.example.explorecity.api.classes.chat

import com.example.explorecity.api.classes.User.SingleUserDetail

data class ChatMessage(
    val sender: SingleUserDetail,
    val text: String,
    val time: Double
)
