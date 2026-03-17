package com.example.kubik.presentation.home.components

import android.media.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.presentation.theme.KubikTheme

@Composable
fun MainCard(
    modifier: Modifier = Modifier,
    icon: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit
    ){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 140.dp)
            .clickable{ onClick() },
        border = BorderStroke(1.2f.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ){
        Box(modifier = Modifier
            .fillMaxWidth()
            .heightIn(min= 140.dp)
            .background(MaterialTheme.colorScheme.surface)){
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                if(isSystemInDarkTheme()){
                    Image(painter =
                        painterResource(icon),
                        contentDescription = title)
                }else{
                    Image(painter =
                        painterResource(icon),
                        contentDescription = title,
                        modifier = Modifier.size(42.dp))
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = title,
                    fontFamily = FontFamily(
                        Font(R.font.inter_semibold, FontWeight.Bold)
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    subtitle,
                    fontFamily = FontFamily(
                        Font(R.font.inter_regular, FontWeight.Normal)),
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun previewCard(){
    KubikTheme() {
        MainCard(
            modifier = Modifier,
            icon = if(isSystemInDarkTheme()) R.drawable.queuedark else R.drawable.queue,
            title = "Очереди",
            subtitle = "Вы не состоите в очереди",
            onClick = {  }
        )
    }
}
