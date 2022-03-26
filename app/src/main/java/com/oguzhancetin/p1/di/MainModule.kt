package com.oguzhancetin.p1.di

import com.oguzhancetin.p1.util.Constants
import com.oguzhancetin.p1.remote.FirebaseServiceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ActivityComponent::class)
object  MainModule {
    @Provides
    fun provideAnalyticsService(
    ): FirebaseServiceApi {
        val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit.create(FirebaseServiceApi::class.java)
    }
}
