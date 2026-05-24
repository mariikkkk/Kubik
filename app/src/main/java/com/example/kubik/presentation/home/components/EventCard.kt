package com.example.kubik.presentation.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amazonaws.services.kms.model.KeyUnavailableException
import com.example.kubik.R
import com.example.kubik.presentation.theme.KubikTheme

@Composable
fun EventCard(
    title: String,
    date: String,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(80.dp)
            .clickable{ onClick() },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.7f.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(80.dp)
                .background(MaterialTheme.colorScheme.background)
        ){
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        ),
                    contentAlignment = Alignment.Center
                ){
                    Text(date,
                        fontFamily =
                            FontFamily(
                                Font(
                                    R.font.inter_bold,
                                    FontWeight.Bold
                                )
                        ),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    title,
                    fontFamily = FontFamily(
                        Font(
                            R.font.inter_semibold,
                            FontWeight.SemiBold
                        )
                    ),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    Icons.Default.ArrowForwardIos,
                    contentDescription = "Перейти к событию",
                    modifier = Modifier.size(16.dp)
                )
            }
        }

    }
}

@PreviewLightDark
@Composable
fun prevEventCard(){
    KubikTheme() {
        EventCard("Контрольная по матану", "24.03", {})
    }
}