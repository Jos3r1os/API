package com.example.apitarea

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

private const val BASE_URL = "http://10.0.2.2:3000/"

interface TareasApi {
    @GET("tareas")
    suspend fun getTareas(): List<Tarea>

    @POST("tareas")
    suspend fun crearTarea(@Body tarea: Tarea): Map<String, Any>

    @PUT("tareas/{id}")
    suspend fun actualizarTarea(@Path("id") id: Int, @Body tarea: Tarea): Map<String, Any>

    @DELETE("tareas/{id}")
    suspend fun eliminarTarea(@Path("id") id: Int): Map<String, Any>
}

object RetrofitClient {
    val api: TareasApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TareasApi::class.java)
    }
}