package com.example.kubik.presentation.files.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.domain.models.FileFolderItem
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.LocalIsDarkTheme

@Composable
fun FolderCard(
    folder: FileFolderItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    val isDarkTheme = LocalIsDarkTheme.current
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(24.dp),
        elevation = cardElevation(
            2.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        shape = RoundedCornerShape(16.dp),
                        color = if(isDarkTheme) Color(0xFF0D1B39) else Color(0xFFEFF6FF)
                    )
                    .size(48.dp),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = painterResource(id = R.drawable.folder),
                    contentDescription = "folder",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF2B7FFF)
                )
            }
            Spacer(Modifier.width(10.dp))
            Column() {
                Text(
                    folder.name,
                    fontFamily = FontFamily(
                        Font(R.font.inter_semibold)
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 15.sp
                )
                Text(
                    "${folder.countFiles} файлов",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Открыть")
        }
    }
}

@PreviewLightDark
@Composable
fun preiewFolderCard(){
    KubikTheme() {
        FolderCard(
            FileFolderItem(2,"Дискретная математика", 12, 2),
            {}
        )
    }
}