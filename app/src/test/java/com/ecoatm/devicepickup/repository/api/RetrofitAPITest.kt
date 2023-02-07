package com.ecoatm.devicepickup.repository.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ecoatm.devicepickup.repository.DevicePickupRepository
import com.ecoatm.devicepickup.repository.FakeRepository
import com.ecoatm.devicepickup.repository.db.ShippingBoxDao
import com.ecoatm.devicepickup.utils.MockResponseFileReader
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class RetrofitAPITest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val server = MockWebServer()
    lateinit var retrofitAPI: RetrofitAPI
    private lateinit var mockedResponse: String

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    @Before
    fun setup() {
        server.start(8000)

        val BASE_URL = server.url("/").toString()

        val okHttpClient = OkHttpClient
            .Builder()
            .build()
        retrofitAPI = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build().create(RetrofitAPI::class.java)
    }

    @Test
    fun `test Login API return success`() {
        mockedResponse = MockResponseFileReader("devicePickupApi/LoginSuccess.json").content

        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockedResponse)
        )

        val response = runBlocking { retrofitAPI.login("Test", "1111") }
        val json = gson.toJson(response.body())

        val resultResponse = JsonParser.parseString(json)
        val expectedresponse = JsonParser.parseString(mockedResponse)

        Assert.assertNotNull(response)
        Assert.assertTrue(resultResponse.equals(expectedresponse))
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

}