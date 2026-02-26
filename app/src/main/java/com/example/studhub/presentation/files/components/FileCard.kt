package com.example.studhub.presentation.files.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.unit.sp
import com.example.studhub.R
import com.example.studhub.domain.models.FileCategory
import com.example.studhub.domain.models.FileItem
import com.example.studhub.domain.models.FileType
import com.example.studhub.presentation.theme.StudHubTheme

@Composable
fun FileCard(
    file: FileItem,
    onDownloadClick: () -> Unit,
    onShareClick: () -> Unit,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
){

    val iconId = when(file.type){
        FileType.PDF -> R.drawable.pdf
        FileType.DOCX -> R.drawable.docx
        FileType.TXT -> R.drawable.txt
        FileType.PPTX -> R.drawable.pptx
        FileType.JPEG -> R.drawable.img
        FileType.RAR -> R.drawable.archive
        FileType.ZIP -> R.drawable.archive
    }
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ){
        Row(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(painter = painterResource(iconId),
                contentDescription = "Иконка файла",
                modifier = Modifier.size(52.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(Modifier.weight(50f)) {
                Text(
                    text = file.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${file.size} | ${file.date} | ${file.author}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(500.dp).horizontalScroll(rememberScrollState())
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onDownloadClick) {
                Icon(
                    painter = painterResource(R.drawable.download),
                    contentDescription = "Скачать файл",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Box(){
                IconButton(onClick = { expanded = true } ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Меню",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = {Text("Скачать")},
                        onClick = {
                            expanded = false
                            onDownloadClick()
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.download),
                                contentDescription = "Скачать файл",
                                modifier = Modifier.size(20.dp))
                        }
                    )
                    DropdownMenuItem(
                        text = {Text("Поделиться")},
                        onClick = {
                            expanded = false
                            onShareClick()
                        },
                        leadingIcon = {
                            Icon( Icons.Default.Share,
                                contentDescription = "Поделиться файлом",
                                modifier = Modifier.size(20.dp))
                        }
                    )
                    DropdownMenuItem(
                        text = {Text("Переименовать")},
                        onClick = {
                            expanded = false
                            onRenameClick()
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Create,
                                contentDescription = "Переименовать файл",
                                modifier = Modifier.size(20.dp))
                        }
                    )
                    DropdownMenuItem(
                        text = {Text("Удалить")},
                        onClick = {
                            expanded = false
                            onDeleteClick()
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Удалить файл",
                                modifier = Modifier.size(20.dp))
                        }
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun FileCardPreview() {
    StudHubTheme() {
        FileCard(
            FileItem(
                1,
                1,
                "Лекция №1",
                "2.5 MB",
                FileType.PDF,
                "01.02.2024",
                "Иванов И.И.",
                FileCategory.LECTURES
            ), {}, {}, {}, {})
    }
}