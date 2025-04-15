package io.github.shevavarya.avito_tech_internship.core.di

import io.github.shevavarya.avito_tech_internship.core.network.NetworkClient
import io.github.shevavarya.avito_tech_internship.core.network.NetworkClientImpl
import io.github.shevavarya.avito_tech_internship.core.network.api.DeezerApi
import io.github.shevavarya.avito_tech_internship.core.utils.NetworkChecker
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.deezer.com/"

val dataModule = module {

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    single<DeezerApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(DeezerApi::class.java)
    }

    single<NetworkClient> { NetworkClientImpl(get(), get()) }

    single<NetworkChecker> { NetworkChecker(get()) }

}