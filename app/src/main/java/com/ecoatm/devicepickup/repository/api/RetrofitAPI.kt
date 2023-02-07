package com.ecoatm.devicepickup.repository.api

import com.ecoatm.devicepickup.model.LoginUserModel
import com.ecoatm.devicepickup.model.ShippingBoxModel
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitAPI {

    @POST("/login/")
    suspend fun login(
        @Query("user_name")
        userName: String,
        @Query("password")
        password: String
    ) : Response<LoginUserModel>

    @POST("/submit/")
    suspend fun submitShippingBox(
        boxes: List<ShippingBoxModel>
    ) : Response<Boolean>
}