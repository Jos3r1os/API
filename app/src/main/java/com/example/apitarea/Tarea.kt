package com.example.apitarea

data class Tarea(
    val id: Int = 0,
    val titulo: String,
    val descripcion: String? = null,
    val completada: Boolean = false
)