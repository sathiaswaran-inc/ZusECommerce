package com.example.zussathia.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zussathia.data.model.LoginResponseModel
import com.example.zussathia.ui.MainActivity
import com.example.zussathia.utils.AppUtils.showToast
import com.example.zussathia.utils.CURRENT_USER
import com.example.zussathia.utils.EventObserver
import com.example.zussathia.utils.PrefUtils
import com.example.zussathia.utils.SESSION_TOKEN
import com.example.zussathia.utils.Status
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()
        setContent {
            SplashScreen {
                authViewModel.autoSignIn()
            }
        }
    }

    private fun initObservers() {
        authViewModel.autoSignIn.observe(this, EventObserver {
            when(it.status) {
                Status.SUCCESS -> {
                    processLogin(it.data)
                }
                Status.LOADING -> {
                    //Noting to do since its splash screen only
                }
                Status.ERROR -> {
                    showToast(this, "Something went wrong!")
                }
            }
        })
    }

    private fun processLogin(data: LoginResponseModel?) {
        if (data != null) {
            if (data.message == "success") {
                PrefUtils(this).save(SESSION_TOKEN, data.token)
                PrefUtils(this).save(CURRENT_USER, data.user.name)
                startActivity(
                    Intent(this, MainActivity::class.java)
                )
                finish()
            } else {
                showToast(this, "Login Failed")
            }
        } else {
            showToast(this, "Login Failed")
        }
    }

}

@Composable
private fun SplashScreen(onTimeout: () -> Unit) {

    val scale = remember { Animatable(0.6f) }
    val alpha = remember { Animatable(0f) }
    val rotation = remember { Animatable(-15f) }

    val infiniteTransition = rememberInfiniteTransition(label = "glow")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "glowAnimation"
    )

    LaunchedEffect(Unit) {
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(1200)
            )
        }

        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }

        launch {
            rotation.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 1200,
                    easing = FastOutSlowInEasing
                )
            )
        }

        delay(2500)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF141E46),
                        Color(0xFF1F4172),
                        Color(0xFF2B3A7E)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(260.dp)
                .graphicsLayer(alpha = glowAlpha)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF7F8CFF),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = scale.value,
                        scaleY = scale.value,
                        rotationZ = rotation.value,
                        alpha = alpha.value
                    )
                    .size(120.dp)
                    .background(
                        Color.White.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(32.dp)
                    )
                    .border(
                        1.dp,
                        Color.White.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(32.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Z",
                    color = Color.White,
                    fontSize = 56.sp,
                    fontWeight = FontWeight.ExtraBold,
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "ZusSathia",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.graphicsLayer{ alpha.value }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Experience ECommerce App",
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 14.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.graphicsLayer{ alpha.value }
            )
        }
    }
}