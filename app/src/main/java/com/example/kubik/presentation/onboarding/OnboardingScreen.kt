package com.example.kubik.presentation.onboarding

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kubik.presentation.onboarding.components.NameInputStep
import com.example.kubik.presentation.onboarding.components.RoleSelectionStep
import com.example.kubik.presentation.onboarding.components.StarostaJoinStep
import com.example.kubik.presentation.onboarding.components.StepIndicators
import com.example.kubik.presentation.onboarding.components.StudentJoinStep
import com.example.kubik.presentation.theme.KubikTheme


enum class OnboardingStep(val index: Int){
    NAME(0),
    ROLE(1),
    JOIN(2)
}
@Composable
fun OnboardingScreen(
    isDarkTheme: Boolean,
    onFinish: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
){
    var currentStep by remember {  mutableStateOf(OnboardingStep.NAME) }
    val firstName by viewModel.firstName.collectAsStateWithLifecycle()
    val lastName by viewModel.lastName.collectAsStateWithLifecycle()
    var selectedRole by remember { mutableStateOf("") }

    BackHandler(
        enabled = currentStep != OnboardingStep.NAME
    ) {
        currentStep = when(currentStep){
            OnboardingStep.JOIN -> OnboardingStep.ROLE
            OnboardingStep.ROLE -> OnboardingStep.NAME
            else -> OnboardingStep.NAME
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)

    ){
        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                if(targetState.index > initialState.index){
                    (slideInHorizontally(tween(400))
                    { width -> +width } + fadeIn(tween(400))) togetherWith
                            (slideOutHorizontally(tween(400))
                            { width -> -width } + fadeOut(tween(400)))
                } else {
                    (slideInHorizontally(tween(400))
                    { width -> -width } + fadeIn(tween(400))) togetherWith
                            (slideOutHorizontally(tween(400))
                            { width -> width } + fadeOut(tween(400)))
                     }
                },
            label = "onboarding_animation"
        ) { step ->
            when(step) {
                OnboardingStep.NAME -> {
                    NameInputStep(
                        firstName,
                        lastName,
                        { fName, lName ->
                            viewModel.updateUser(fName, lName)
                            currentStep = OnboardingStep.ROLE
                        }
                    )
                }
                OnboardingStep.ROLE -> {
                    RoleSelectionStep(
                        isDarkTheme,
                        {
                            selectedRole = "STAROSTA"
                            currentStep = OnboardingStep.JOIN
                        },
                        {
                            selectedRole = "STUDENT"
                            currentStep = OnboardingStep.JOIN
                        }
                    )
                }
                OnboardingStep.JOIN -> {
                    val context = LocalContext.current
                    if(selectedRole == "STUDENT"){
                        StudentJoinStep{ groupName ->
                            viewModel.submitStudent(
                                groupName,
                                onSuccess = { onFinish() },
                                onError = { errorMessage ->
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }else{
                        StarostaJoinStep{ inviteCode ->
                            viewModel.submitStarosta(inviteCode,
                                onSuccess = { onFinish()},
                                onError = { errorMessage ->
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        }
        StepIndicators(
            currentStep = currentStep.index,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

@PreviewLightDark
@Composable
fun previewScreen(){
    KubikTheme() {
        OnboardingScreen(isSystemInDarkTheme(),{})
    }
}