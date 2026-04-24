package me.georgemakhlouf.exploraapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    // Validation error states
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var firebaseError by remember { mutableStateOf<String?>(null) }

    val auth = FirebaseAuth.getInstance()
    val primaryOrange = Color(0xFFE45D25)
    val lightGrayBg = Color(0xFFF8F9FE)
    val inputBg = Color(0xFFE5E5EA)
    val errorRed = Color(0xFFD32F2F)

    // Validation functions
    fun validateEmail(): Boolean {
        return when {
            email.isBlank() -> {
                emailError = "El correo es obligatorio."
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> {
                emailError = "Ingresa un correo válido."
                false
            }
            else -> {
                emailError = null
                true
            }
        }
    }

    fun validatePassword(): Boolean {
        return when {
            password.isBlank() -> {
                passwordError = "La contraseña es obligatoria."
                false
            }
            password.length < 6 -> {
                passwordError = "La contraseña debe tener al menos 6 caracteres."
                false
            }
            else -> {
                passwordError = null
                true
            }
        }
    }

    fun handleLogin() {
        firebaseError = null
        val emailValid = validateEmail()
        val passwordValid = validatePassword()

        if (!emailValid || !passwordValid) return

        isLoading = true

        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    onLoginSuccess()
                } else {
                    firebaseError = when {
                        task.exception?.message?.contains("no user record") == true ->
                            "No existe una cuenta con este correo."
                        task.exception?.message?.contains("password is invalid") == true ->
                            "Contraseña incorrecta."
                        task.exception?.message?.contains("badly formatted") == true ->
                            "El correo no tiene un formato válido."
                        task.exception?.message?.contains("blocked") == true ->
                            "Demasiados intentos. Intenta más tarde."
                        else -> "Error al iniciar sesión. Intenta de nuevo."
                    }
                }
            }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = lightGrayBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(bottomStart = 60.dp, bottomEnd = 60.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(primaryOrange),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Logo",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Explorando Colombia",
                        color = primaryOrange,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Bienvenido",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Explora la magia de Colombia hoy.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                // Email field
                Text(
                    text = "CORREO ELECTRÓNICO",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (emailError != null) errorRed else Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null
                        firebaseError = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(28.dp)),
                    placeholder = { Text("nombre@ejemplo.com", color = Color.Gray) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = if (emailError != null) errorRed else Color.Gray
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = if (emailError != null) Color(0xFFFFEBEE) else inputBg,
                        unfocusedContainerColor = if (emailError != null) Color(0xFFFFEBEE) else inputBg,
                        disabledContainerColor = inputBg,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !isLoading
                )
                // Email error text
                if (emailError != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = emailError!!,
                        color = errorRed,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Password field
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "CONTRASEÑA",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (passwordError != null) errorRed else Color.Gray
                    )
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryOrange,
                        modifier = Modifier.clickable { }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null
                        firebaseError = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(28.dp)),
                    placeholder = { Text("........", color = Color.Gray) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (passwordError != null) errorRed else Color.Gray
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                tint = Color.Gray
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = if (passwordError != null) Color(0xFFFFEBEE) else inputBg,
                        unfocusedContainerColor = if (passwordError != null) Color(0xFFFFEBEE) else inputBg,
                        disabledContainerColor = inputBg,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true,
                    enabled = !isLoading
                )
                // Password error text
                if (passwordError != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = passwordError!!,
                        color = errorRed,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                // Firebase error (shown below both fields)
                if (firebaseError != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = firebaseError!!,
                            color = errorRed,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Login button
                Button(
                    onClick = { handleLogin() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    enabled = !isLoading
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = if (isLoading)
                                        listOf(Color.Gray, Color.Gray)
                                    else
                                        listOf(primaryOrange, Color(0xFFFF8A65))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "Iniciar Sesión",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), thickness = 0.5.dp, color = Color.LightGray)
                    Text(
                        text = " O CONTINUAR CON ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), thickness = 0.5.dp, color = Color.LightGray)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SocialButton(
                        text = "Google",
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Email
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row {
                Text(text = "¿No tienes cuenta? ", color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = "Regístrate",
                    color = primaryOrange,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }
    }
}

@Composable
fun SocialButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    OutlinedButton(
        onClick = { },
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ExploraAppTheme {
        LoginScreen(onLoginSuccess = {}, onNavigateToRegister = {})
    }
}