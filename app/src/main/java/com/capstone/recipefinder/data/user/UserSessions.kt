package com.capstone.recipefinder.data.user

data class UserSessions(
    val name: String,
    val token: String,
    val userId: String,
    val isLogin: Boolean
)