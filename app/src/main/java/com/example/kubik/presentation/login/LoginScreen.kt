package com.example.kubik.presentation.login

import android.graphics.BlurMaskFilter
import android.graphics.BlurMaskFilter.Blur
import android.graphics.Paint
import android.util.Log
import android.widget.Space
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kubik.R
import com.example.kubik.presentation.login.component.LoginBackground
import com.example.kubik.presentation.navigation.KubikApp
import com.example.kubik.presentation.theme.KubikTheme
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.compose.onetap.OneTap
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
){
    val scope = rememberCoroutineScope()
    val isLoading by viewModel.isLoading.collectAsState()
    val vkAuthCallback = remember {
        object : VKIDAuthCallback {
            override fun onAuth(accessToken: AccessToken) {
                Log.d("KUBIK_AUTH", "1. ВК успешно вошел. ID: ${accessToken.userID}")
                val userId = accessToken.userID
                val firstName = accessToken.userData.firstName ?: "MAX"
                val lastName = accessToken.userData.lastName ?: ""
                viewModel.authWithVk(
                    userId,
                    firstName = firstName,
                    lastName = lastName,

                    {
                        Log.d("KUBIK_AUTH", "2. Supabase успешно авторизовал!")
                        onLoginSuccess()
                    },
                    {error ->
                        Log.e("KUBIK_AUTH", "ОШИБКА Supabase: $error")
                        println(error)
                    })

            }

            override fun onFail(fail: VKIDAuthFail) {
                println("VK auth failed: $fail")
            }
        }
    }
    LoginBackground {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(128.dp))


            Image(
                painter = painterResource(id = R.drawable.kubik),
                contentDescription = "Логотип",
                modifier = Modifier
                    .size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "КУБик",
                color = Color.White,
                fontFamily = FontFamily(
                    Font(R.font.inter_black, FontWeight.Normal)
                ),
                fontWeight = FontWeight.Normal,
                fontSize = 36.sp)
            Spacer(modifier = Modifier.height(12.dp))
            
            Box(
                modifier = Modifier
                    .width(52.dp)
                    .height(6.dp)
                    .background(Color(0xFF615FFF)
                    , RoundedCornerShape(2.dp)
                    )

            )

            Spacer(Modifier.height(12.dp))

            Text(
                "Комплексно Учебная База Информации и Коммуникации",
                color = Color(0xFF90A1B9),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(
                    Font(R.font.inter_medium, FontWeight.Normal)
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(48.dp))

            Button(
                onClick = {
                    if (!isLoading) {
                        scope.launch {
                            VKID.instance.authorize(
                                callback = vkAuthCallback
                            )
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0077FF),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFF51A2FF),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .drawBehind{
                        drawIntoCanvas { canvas ->
                            val paint = Paint()
                            paint.apply {
                                color = Color(0xFF2B7FFF).toArgb()
                                maskFilter = BlurMaskFilter(15f,Blur.NORMAL)
                            }

                            canvas.nativeCanvas.drawRoundRect(
                                0f,
                                0f,
                                size.width,
                                size.height + 2.dp.toPx(),
                                40f, 40f,
                                paint
                            )

                        }
                    },
                shape = RoundedCornerShape(12.dp)
            ) {
                if(isLoading){
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }else{
                    Icon(
                        painter = painterResource(
                            R.drawable.socialvkontakte
                        ),
                        contentDescription = "Войти через VK",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Войти через VK",
                        color = Color.White,
                        fontFamily = FontFamily(
                            Font(R.font.inter_bold, FontWeight.Normal)
                        ),
                        fontSize = 16.sp
                    )
                }

            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { onLoginSuccess() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2AABEE),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0x00BCFF),
                        shape = RoundedCornerShape(12.dp)
                    ).drawBehind{
                        drawIntoCanvas { canvas ->
                            val paint = Paint()
                            paint.apply {
                                color = Color(0xFF00A6F4).toArgb()
                                maskFilter = BlurMaskFilter(15f,Blur.NORMAL)
                            }

                            canvas.nativeCanvas.drawRoundRect(
                                0f,
                                0f,
                                size.width,
                                size.height + 2.dp.toPx(),
                                40f, 40f,
                                paint
                            )

                        }
                    },
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    painter = painterResource(
                        R.drawable.telegram
                    ),
                    contentDescription = "Войти через Telegram"
                )
                Spacer(Modifier.width(8.dp))
                Text("Войти через Telegram",
                    color = Color.White,
                    fontFamily = FontFamily(
                        Font(R.font.inter_bold, FontWeight.Normal)
                    ),
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { onLoginSuccess() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0F172B),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFF1D293D),
                        shape = RoundedCornerShape(12.dp)
                    ).drawBehind{
                        drawIntoCanvas { canvas ->
                            val paint = Paint()
                            paint.apply {
                                color = Color(0xFF000000).toArgb()
                                maskFilter = BlurMaskFilter(15f,Blur.NORMAL)
                            }

                            canvas.nativeCanvas.drawRoundRect(
                                0f,
                                0f,
                                size.width,
                                size.height + 2.dp.toPx(),
                                40f, 40f,
                                paint
                            )

                        }
                    },
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    painter = painterResource(
                        R.drawable.gmail
                    ),
                    contentDescription = "Войти через Gmail",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Red
                )
                Spacer(Modifier.width(8.dp))
                Text("Войти через Gmail",
                    color = Color.White,
                    fontFamily = FontFamily(
                        Font(R.font.inter_bold, FontWeight.Normal)
                    ),
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.height(32.dp))

            Text(
                "Built for Students by Students",
                fontFamily = FontFamily(
                    Font(R.font.inter_bold, FontWeight.Normal)
                ),
                fontSize = 10.sp,
                color = Color(0xFF45556C)
            )
        }
    }
//    Column(
//        modifier = Modifier
//            .fillMaxSize() // занимаем весь экран
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally, // центруем элементы по горизонтали
//        verticalArrangement = Arrangement.Center // центруем элементы по вертикали
//    )
//    {
//        Text(
//            text = "StudHub",
//            style = MaterialTheme.typography.headlineLarge,
//            color = MaterialTheme.colorScheme.primary
//        )
//        Spacer(modifier = Modifier.height(32.dp))
//        OutlinedTextField(
//            value = viewModel.email, // привязка текста в поле к переменной
//            onValueChange = { viewModel.updateEmail(it) }, // изменение текста, когда пользователь печатает
//            label = { Text("Email") }, // надпись над полем ввода
//            modifier = Modifier.fillMaxWidth(),
//            leadingIcon = { Icon( imageVector = Icons.Default.Email, contentDescription = "Email Icon")}
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        OutlinedTextField(
//            leadingIcon = {
//                Icon( imageVector = Icons.Default.Lock,
//                    contentDescription = "Password Icon")
//                          },
//            trailingIcon = {
//                var iconID =
//                    if (viewModel.passwordVisible){
//                        R.drawable.visibility
//                    }
//                    else{
//                        R.drawable.visibilityoff
//                        }
//                IconButton(
//                    onClick = {viewModel.togglePasswordVisibility()},
//                ) {
//                    Icon(
//                    painter = painterResource(iconID),
//                    contentDescription = "Показать/скрыть пароль"
//                    )
//                }
//            },
//            modifier = Modifier
//                .fillMaxWidth(),
//            value = viewModel.password,
//            onValueChange = { viewModel.updatePassword(it) },
//            label = { Text("Пароль") },
//            visualTransformation =
//                if (viewModel.passwordVisible)
//                {
//                VisualTransformation.None
//                } else
//                {
//                PasswordVisualTransformation()
//                }, // Замена текста на звездочки
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
//                // специальная клаиватура для паролей, которая отключает Т9 и подсказки
//        )
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        Button(
//            onClick = { onLoginSuccess() },
//            modifier = Modifier
//                .fillMaxWidth()
//            .height(56.dp)
//        )
//        {
//            Text("Войти")
//        }
//    }
}

@Preview(showBackground = true)
@Composable
fun previewLoginScreen(){
    KubikTheme() {
        LoginScreen(viewModel = viewModel(), onLoginSuccess = {})
    }
}