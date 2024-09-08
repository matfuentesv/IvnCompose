package cl.smartsolutions.ivncompose.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.smartsolutions.ivnapp.model.User
import cl.smartsolutions.ivncompose.repository.UserRepository
import cl.smartsolutions.ivncompose.ui.theme.IvnComposeTheme
import cl.smartsolutions.ivncompose.R

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IvnComposeTheme {
                RegisterScreen(
                    onBackPressed = { onBackPressed() }
                )
            }
        }
    }
}

@Composable
fun RegisterScreen(
    onBackPressed: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val gradientColors = listOf(
        Color(0xFFFFFFFF),
        Color(0xFF00BCD4)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(gradientColors))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Start
        )
        {
            Image(
                painter = painterResource(R.drawable.ic_back_arrow),
                contentDescription = "Volver",
                modifier = Modifier
                    .size(65.dp)
                    .padding(start = 20.dp, top = 25.dp)
                    .clickable { onBackPressed() }
            )
        }
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )

        // Campos de texto
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Apellido") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            )
        )

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Edad") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de registro
        Button(
            onClick = {
                if (UserRepository.getUsers().size < 5) {
                    val newUser = User(
                        firstName = name,
                        lastName = lastName,
                        email = email,
                        password = password,
                        age = age.toInt()
                    )
                    UserRepository.createUser(newUser)
                    Toast.makeText(context, "Usuario registrado correctamente", Toast.LENGTH_LONG).show()
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "No se pueden crear más usuarios", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Guardar registro", fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun previewRegisterScreen() {
    IvnComposeTheme {
        RegisterScreen(onBackPressed = {})
    }
}
