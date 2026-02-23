package com.example.studhub.presentation.files

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studhub.R
import com.example.studhub.domain.models.FileCategory
import com.example.studhub.domain.models.FileItem
import com.example.studhub.domain.models.FileType

@Composable
fun FileDetailsScreen(
    folderName: String,
    files: List<FileItem>,
    onBackClick: () -> Unit
){
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(FileCategory.ALL) }

    Box(){
        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 12.dp))
        {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically)
            {
                IconButton(onClick = onBackClick,
                )
                {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Вернуться назад к списку папок")
                }
                Text(
                    text = folderName,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold
                )

            }
        }
        FloatingActionButton(
            onClick = { },
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 16.dp, bottom = 16.dp)
        ){
            Icon(painter = painterResource(R.drawable.download),
                contentDescription = "Добавить")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FileDetailsScreenPreview(){
    FileDetailsScreen("Математический анализ",
        listOf(FileItem(1, "Лекция №1", "2.5 MB", FileType.PDF, "01.02.2024", "Иванов И.И.", FileCategory.LECTURES),
            FileItem(2, "Лекция №2", "1.5 MB", FileType.PDF, "02.02.2024", "Петров П.П.", FileCategory.LECTURES),
            FileItem(3, "ТРПП Практика №1", "1000 MB", FileType.DOCX, "03.12.2026", "Куликов А", FileCategory.PRACTICE),
            FileItem(4, "Скриншот ошибки", "1000 MB", FileType.JPEG, "03.12.2026", "Куликов А", FileCategory.OTHER),
            FileItem(5, "ООП Лекция №1", "1000 MB", FileType.PPTX, "03.12.2026", "Куликов А", FileCategory.LECTURES)),
        {})

}