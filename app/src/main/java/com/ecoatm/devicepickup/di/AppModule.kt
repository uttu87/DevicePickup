package com.ecoatm.devicepickup.di

import android.content.Context
import androidx.room.Room
import com.ecoatm.devicepickup.BuildConfig
import com.ecoatm.devicepickup.repository.api.RetrofitAPI
import com.ecoatm.devicepickup.repository.db.DevicePickupDatabase
import com.ecoatm.devicepickup.utils.Constants.BASE_URL
import com.ecoatm.devicepickup.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun injectRoomDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
            context,
            DevicePickupDatabase::class.java,
            DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun injectShippingBoxDao(database: DevicePickupDatabase) = database.shippingBoxDao()

    @Singleton
    @Provides
    fun injectRetrofitAPI() : RetrofitAPI {
        val httpClient = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(logging)
        }
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .build()
            .create(RetrofitAPI::class.java)
    }
}