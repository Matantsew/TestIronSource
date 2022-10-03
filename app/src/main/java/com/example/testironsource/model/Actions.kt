package com.example.testironsource.model

import com.google.gson.annotations.SerializedName

class Actions(
    @field:SerializedName("type") val type: String,
    @field:SerializedName("enabled") val enabled: Boolean,
    @field:SerializedName("priority") val priority: Int,
    @field:SerializedName("valid_days") val validDays: ArrayList<Int>,
    @field:SerializedName("cool_down") val coolDown: Int
)