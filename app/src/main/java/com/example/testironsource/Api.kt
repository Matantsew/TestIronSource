package com.example.testironsource

import retrofit2.Call
import retrofit2.http.GET

interface Api {

    @GET("butto_to_action_config.json")
    fun getActions(): Call<List<Actions>>

    companion object {
        const val BASE_URL = "https://s3-us-west-2.amazonaws.com/androidexam/"
    }
}
