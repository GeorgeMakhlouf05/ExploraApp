package me.georgemakhlouf.exploraapp.ui.theme

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {

    val auth = FirebaseAuth.getInstance()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var acceptedTerms by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val primaryOrange = Color(0xFFE45D25)
    val lightGrayBg = Color(0xFFF8F9FE)
    val inputBg = Color(0xFFE5E5EA)

    Surface(
        modifier = modifier.fillMaxSize(),
        color = lightGrayBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IconButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = primaryOrange
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Crea tu cuenta", fontSize = 32.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(24.dp))

            RegisterField("NOMBRE", name, { name = it }, "Tu nombre", Icons.Default.Person, inputBg)
            Spacer(modifier = Modifier.height(16.dp))

            RegisterField("EMAIL", email, { email = it }, "correo@email.com", Icons.Default.Email, inputBg)
            Spacer(modifier = Modifier.height(16.dp))

            RegisterField("PASSWORD", password, { password = it }, "******", Icons.Default.Lock, inputBg, true)
            Spacer(modifier = Modifier.height(16.dp))

            RegisterField("CONFIRMAR", confirmPassword, { confirmPassword = it }, "******", Icons.Default.Refresh, inputBg, true)

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = acceptedTerms,
                    onCheckedChange = { acceptedTerms = it }
                )
                Text("Acepto términos y condiciones")
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {

                    errorMessage = ""

                    // VALIDACIONES
                    when {
                        name.isBlank() -> {
                            errorMessage = "El nombre es obligatorio"
                            return@Button
                        }
                        email.isBlank() -> {
                            errorMessage = "El correo es obligatorio"
                            return@Button
                        }
                        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                            errorMessage = "Correo inválido"
                            return@Button
                        }
                        password.length < 6 -> {
                            errorMessage = "Mínimo 6 caracteres"
                            return@Button
                        }
                        password != confirmPassword -> {
                            errorMessage = "Las contraseñas no coinciden"
                            return@Button
                        }
                        !acceptedTerms -> {
                            errorMessage = "Debes aceptar los términos"
                            return@Button
                        }
                    }

                    isLoading = true

                    // FIREBASE REGISTER
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            isLoading = false

                            if (task.isSuccessful) {

                                val user = auth.currentUser

                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build()

                                user?.updateProfile(profileUpdates)

                                onRegisterSuccess()

                            } else {
                                errorMessage = task.exception?.message ?: "Error al registrarse"
                            }
                        }

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isLoading) "Cargando..." else "Registrarse")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text("¿Ya tienes cuenta? ")
                Text(
                    "Inicia sesión",
                    color = primaryOrange,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    }
}

@Composable
fun RegisterField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    bg: Color,
    isPassword: Boolean = false
) {
    Column {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            leadingIcon = { Icon(icon, null) },
            placeholder = { Text(placeholder) },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = bg,
                unfocusedContainerColor = bg,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        )
    }
}