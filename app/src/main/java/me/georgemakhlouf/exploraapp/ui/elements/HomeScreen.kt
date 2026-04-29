package me.georgemakhlouf.exploraapp.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onClickAddTouristicPlace: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Explora Colombia") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onClickAddTouristicPlace) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar lugar"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Contenido de la pantalla principal
        }
    }
}

@Composable
fun AddTouristicPlaceScreen(onBackClick: () -> Unit = {}) {
    var nombre by remember { mutableStateOf("") }
    var departamento by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        // Top Bar
        IconButton(onClick = onBackClick) {
            Text(
                text = "← AddPlace",
                color = Color(0xFF8B2E1E),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Gradient Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFD84315),
                            Color(0xFFFF7043)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Comparte tu descubrimiento",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Ayuda a otros viajeros a encontrar los tesoros escondidos de nuestra tierra.",
                    color = Color.White,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Form Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {

            FormField("NOMBRE DEL LUGAR", "Ej: Cascada del Fin del Mundo", nombre) {
                nombre = it
            }

            FormField("DEPARTAMENTO", "Ej: Putumayo", departamento) {
                departamento = it
            }

            FormField("CIUDAD", "Ej: Mocoa", ciudad) {
                ciudad = it
            }

            Text(
                text = "DESCRIPCIÓN",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.padding(top = 12.dp, bottom = 6.dp)
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                placeholder = { Text("Cuéntanos por qué este lugar es especial...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Button
            Button(
                onClick = {
                    // Handle publish
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD84315)
                )
            ) {
                Text("Publicar", color = Color.White)
            }
        }
    }
}

@Composable
fun FormField(
    label: String,
    hint: String,
    value: String,
    onChange: (String) -> Unit
) {
    Text(
        text = label,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.padding(top = 12.dp, bottom = 6.dp)
    )

    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        placeholder = { Text(hint) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    )
}
