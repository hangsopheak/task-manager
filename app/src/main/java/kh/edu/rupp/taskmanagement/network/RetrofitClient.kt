package kh.edu.rupp.taskmanagement.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

// one database per student on the server: replace this GUID with your own
private const val DB_NAME = "11111111-1111-4111-8111-111111111111"

private const val BASE_URL = "https://task-management-db.vercel.app/"

// the header from Session 8 is set once here, so the interface stays four lines
private val dbHeader = Interceptor { chain ->
    val request = chain.request().newBuilder()
        .addHeader("X-DB-NAME", DB_NAME)
        .build()
    chain.proceed(request)
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// one client for the whole app, built the first time it is read
val taskApi: TaskApi by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder().addInterceptor(dbHeader).build())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(TaskApi::class.java)
}
