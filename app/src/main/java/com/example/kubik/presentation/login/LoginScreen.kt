package com.example.kubik.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kubik.R


@Composable
fun LoginScreen(viewModel: LoginViewModel = viewModel(), onLoginSuccess: () -> Unit){

    Column(
        modifier = Modifier
            .fillMaxSize() // занимаем весь экран
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // центруем элементы по горизонтали
        verticalArrangement = Arrangement.Center // центруем элементы по вертикали
    )
    {
        Text(
            text = "StudHub",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = viewModel.email, // привязка текста в поле к переменной
            onValueChange = { viewModel.updateEmail(it) }, // изменение текста, когда пользователь печатает
            label = { Text("Email") }, // надпись над полем ввода
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon( imageVector = Icons.Default.Email, contentDescription = "Email Icon")}
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            leadingIcon = {
                Icon( imageVector = Icons.Default.Lock,
                    contentDescription = "Password Icon")
                          },
            trailingIcon = {
                var iconID =
                    if (viewModel.passwordVisible){
                        R.drawable.visibility
                    }
                    else{
                        R.drawable.visibilityoff
                        }
                IconButton(
                    onClick = {viewModel.togglePasswordVisibility()},
                ) {
                    Icon(
                    painter = painterResource(iconID),
                    contentDescription = "Показать/скрыть пароль"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            value = viewModel.password,
            onValueChange = { viewModel.updatePassword(it) },
            label = { Text("Пароль") },
            visualTransformation =
                if (viewModel.passwordVisible)
                {
                VisualTransformation.None
                } else
                {
                PasswordVisualTransformation()
                }, // Замена текста на звездочки
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                // специальная клаиватура для паролей, которая отключает Т9 и подсказки
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onLoginSuccess() },
            modifier = Modifier
                .fillMaxWidth()
            .height(56.dp)
        )
        {
            Text("Войти")
        }
    }
}
