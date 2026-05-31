package com.example.apitarea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TareasApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareasApp(vm: TareasViewModel = viewModel()) {
    val tareas by vm.tareas.collectAsState()
    val error by vm.error.collectAsState()

    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis Tareas") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarDialogo = true }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            error?.let {
                Text(
                    text = "Error: $it",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }

            LazyColumn(Modifier.fillMaxSize()) {
                items(tareas) { tarea ->
                    TareaItem(
                        tarea = tarea,
                        onToggle = { vm.toggleCompletada(tarea) },
                        onEliminar = { vm.eliminarTarea(tarea.id) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }

    if (mostrarDialogo) {
        NuevaTareaDialog(
            onDismiss = { mostrarDialogo = false },
            onCrear = { titulo, desc ->
                vm.crearTarea(titulo, desc)
                mostrarDialogo = false
            }
        )
    }
}

@Composable
fun TareaItem(tarea: Tarea, onToggle: () -> Unit, onEliminar: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = tarea.completada, onCheckedChange = { onToggle() })
        Column(
            Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(tarea.titulo, style = MaterialTheme.typography.bodyLarge)
            tarea.descripcion?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Button(onClick = onEliminar) {
            Text("X")
        }
    }
}

@Composable
fun NuevaTareaDialog(onDismiss: () -> Unit, onCrear: (String, String) -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva tarea") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (titulo.isNotBlank()) onCrear(titulo, descripcion) },
                enabled = titulo.isNotBlank()
            ) { Text("Crear") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}