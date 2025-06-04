package com.damtoy.movieapp.Activity


import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.damtoy.movieapp.R

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Jika Anda memanggil enableEdgeToEdge di sini, pastikan BaseActivity tidak melakukannya juga
        // enableEdgeToEdge()
        setContent {
            LoginScreen (onLoginClick={ username, password -> // Terima username dan password
                // Lakukan sesuatu dengan username dan password di sini (misal: validasi)
                println("Username: $username, Password: $password")
                startActivity(Intent( this@LoginActivity, MainActivity::class.java))
            })
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview(){
    LoginScreen(onLoginClick={ _, _ -> }) // Sesuaikan dengan lambda baru
}

@Composable
fun LoginScreen(onLoginClick: (String, String) -> Unit){ // Ubah lambda untuk mengirim data
    // State untuk username dan password
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.blackbackground)) // Pastikan blackbackground ada di colors.xml
    ){
        Image(
            painter = painterResource(id = R.drawable.bg1), // Pastikan bg1 ada di drawable
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Column (modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp, vertical = 16.dp)
        ){
            Spacer(modifier = Modifier.height(128.dp))

            androidx.compose.material.Text(
                text = "Log in",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(128.dp))
            GradientTextField(
                value = username,
                onValueChange = { username = it },
                hint = "Username",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            GradientTextField(
                value = password,
                onValueChange = { password = it },
                hint = "Password",
                isPassword = true, // Tambahkan parameter untuk password
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            androidx.compose.material.Text( // Menggunakan Material 2 Text
                text = "Forget Your Password?" ,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(64.dp))
            GradientButton(
                text = "Log In",
                onClick = { onLoginClick(username, password) }, // Kirim username dan password
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
        }
    }
}


@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier // modifier ini akan diterapkan ke Button
){
    Button( // Material 3 Button
        onClick = onClick,
        modifier = modifier, // Terapkan modifier yang diteruskan
        shape = RoundedCornerShape(60.dp),
        border = BorderStroke(
            width = 4.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    colorResource(R.color.pink), // Pastikan pink ada di colors.xml
                    colorResource(R.color.green)  // Pastikan green ada di colors.xml
                )
            )
        ),
        colors = ButtonDefaults.buttonColors( // Material 3 ButtonDefaults
            containerColor = Color.Transparent, // Di M3, gunakan containerColor
            contentColor = Color.White
        )
    ) {
        // Gunakan androidx.compose.material3.Text jika ingin konsisten dengan M3 Button
        androidx.compose.material.Text(text = text, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun GradientTextField(
    value: String,                          // Tambahkan value
    onValueChange: (String) -> Unit,        // Tambahkan onValueChange
    hint: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isPassword: Boolean = false             // Tambahkan untuk field password
){
    Box(modifier = modifier // Terapkan modifier yang diteruskan ke Box luar
        .height(60.dp)
        .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    colorResource(R.color.pink),
                    colorResource(R.color.green)
                )
            ),
            shape = RoundedCornerShape(50.dp)
        )
        .padding(4.dp)
    ){
        OutlinedTextField( // Material 2 OutlinedTextField
            value = value,                       // Gunakan state value
            onValueChange = onValueChange,       // Gunakan state onValueChange
            placeholder = {
                androidx.compose.material.Text( // Material 2 Text
                    text = hint,
                    color = Color.White.copy(alpha = 0.7f), // Buat placeholder sedikit transparan
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
                textAlign = TextAlign.Center
            ),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            colors = TextFieldDefaults.outlinedTextFieldColors( // Material 2 TextFieldDefaults
                textColor = Color.White, // Warna teks input
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color.White,
                focusedLabelColor = Color.White, // Tidak relevan jika tidak ada label
                unfocusedLabelColor = Color.White, // Tidak relevan jika tidak ada label
                placeholderColor = Color.White.copy(alpha = 0.7f) // Bisa juga diatur di sini
            ),
            keyboardOptions = keyboardOptions,
            modifier= Modifier
                .fillMaxWidth()
                .background(
                    color = colorResource(R.color.black1), // Pastikan black1 ada di colors.xml
                    shape = RoundedCornerShape(50.dp)
                )
                .align(Alignment.Center)
        )
    }
}

