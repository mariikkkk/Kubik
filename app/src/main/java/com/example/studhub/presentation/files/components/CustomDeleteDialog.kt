package com.example.studhub.presentation.files.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.studhub.presentation.theme.StudHubTheme

@Composable
fun CustomDeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface
        ){
            Column( modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Icon(
                    Icons.Default.Warning,
                    contentDescription = "Предупреждение перед удалением",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.error

                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Удалить предмет?",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Это действие нельзя отменить. Все материалы внутри папки исчезнут навсегда!",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()){
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ){
                        Text(
                            text = "Отмена"
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    OutlinedButton(
                        onClick = onConfirm,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(
                            text = "Удалить"
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDeleteDialog(){
    StudHubTheme() {
        CustomDeleteDialog({}, {})
    }
}