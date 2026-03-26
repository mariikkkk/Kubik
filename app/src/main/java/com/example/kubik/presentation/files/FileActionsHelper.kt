package com.example.kubik.presentation.files

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.net.toUri

fun downloadFile(context: Context, url: String, fileName: String){
    if (url.isBlank()) return
    try{
        val request = DownloadManager.Request(url.toUri())
            .setTitle(fileName)                                                                                         // Имя файла в шторке
            .setDescription("Скачивание файла из КУБИК")                                                              // описание в шторке
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)                     // уведомление остаенется после загрузки
            .setDestinationInExternalPublicDir(android.os.Environment.DIRECTORY_DOWNLOADS, fileName) // указываем куда сохранять
            .setAllowedOverMetered(true)                                                                                // разрешение на скачку через мобильный интернет

        val extension = fileName.substringAfterLast(".", "")
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        if (mimeType != null){
            request.setMimeType(mimeType)
        }else{
            request.setMimeType("*/*")
        }

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    } catch (e: Exception){
        e.printStackTrace()
    }
}

fun shareFile(context: Context, url: String, fileName: String){
    val lastPartUrlIndex = url.indexOfLast{it == '/'}
    val lastPartUrl = url.substring(lastPartUrlIndex + 1)
    val basePartUrl = url.take(lastPartUrlIndex + 1)
    val encodedLastUrl = Uri.encode(lastPartUrl).toString()
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_SUBJECT, "Файл из КУБИК")
    intent.putExtra(Intent.EXTRA_TEXT, "Держи файл «$fileName» из StudHub\nСкачать можно по ссылке: ${basePartUrl + encodedLastUrl}")
    val chooser = Intent.createChooser(intent, "Поделиться файлом")
    context.startActivity(chooser)

}