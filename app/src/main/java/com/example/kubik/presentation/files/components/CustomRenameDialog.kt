package com.example.kubik.presentation.files.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.kubik.presentation.theme.KubikTheme

@Composable
fun CustomRenameDialog(
    fileName: String,
    onDismiss: () -> Unit,
    onRenameClick: (String) -> Unit
){
    var newFileName by remember { mutableStateOf(fileName) }

    Dialog(
        onDismissRequest = onDismiss
    ){
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Привет")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun previewRenameDialog(){
    KubikTheme() {
        CustomRenameDialog(fileName = "Файл", onDismiss = {}, onRenameClick = {})

    }
}