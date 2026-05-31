package com.example.apitarea

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TareasViewModel : ViewModel() {

    private val _tareas = MutableStateFlow<List<Tarea>>(emptyList())
    val tareas: StateFlow<List<Tarea>> = _tareas

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init { cargarTareas() }

    fun cargarTareas() {
        viewModelScope.launch {
            runCatching { RetrofitClient.api.getTareas() }
                .onSuccess { _tareas.value = it }
                .onFailure { _error.value = it.message }
        }
    }

    fun crearTarea(titulo: String, descripcion: String) {
        viewModelScope.launch {
            runCatching { RetrofitClient.api.crearTarea(Tarea(titulo = titulo, descripcion = descripcion)) }
                .onSuccess { cargarTareas() }
                .onFailure { _error.value = it.message }
        }
    }

    fun toggleCompletada(tarea: Tarea) {
        viewModelScope.launch {
            val actualizada = tarea.copy(completada = !tarea.completada)
            runCatching { RetrofitClient.api.actualizarTarea(tarea.id, actualizada) }
                .onSuccess { cargarTareas() }
                .onFailure { _error.value = it.message }
        }
    }

    fun eliminarTarea(id: Int) {
        viewModelScope.launch {
            runCatching { RetrofitClient.api.eliminarTarea(id) }
                .onSuccess { cargarTareas() }
                .onFailure { _error.value = it.message }
        }
    }
}