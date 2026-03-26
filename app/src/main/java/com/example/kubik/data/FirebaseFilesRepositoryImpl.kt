package com.example.kubik.data


import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.PutObjectRequest
import com.example.kubik.BuildConfig
import com.example.kubik.domain.models.FileFolderItem
import com.example.kubik.domain.models.FileItem
import com.example.kubik.domain.repository.FilesRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import javax.inject.Inject

class FirebaseFilesRepositoryImpl @Inject constructor
    (
    @ApplicationContext private val context: Context,
    private val firestore: FirebaseFirestore
): FilesRepository {
    private val storage = FirebaseStorage.getInstance()

    override fun getFolders(): Flow<List<FileFolderItem>> = callbackFlow{
        val collection = firestore.collection("folders")
        val listener = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null){
                val folders = snapshot.documents.mapNotNull { it.toFileFolderItem() }
                trySend(folders)

            }

        }
        awaitClose {
            listener.remove()

        }
    }

    override suspend fun addFolder(name: String, semester: Int, newId: Int) {
        val newFolderMap = hashMapOf(
            "id" to newId,
            "name" to name,
            "countFiles" to 0,
            "semester" to semester
        )
        firestore.collection("folders").add(newFolderMap)
    }

    override suspend fun deleteFolder(folderId: Int) {
        firestore.collection("folders").whereEqualTo("id", folderId).get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    document.reference.delete()
                }
            }

    }

    override fun getFiles(folderId: Int): Flow<List<FileItem>> = callbackFlow{
        val collection = firestore.collection("files")
        val query = collection.whereEqualTo("folderId", folderId)
        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null){
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null){
                val files = snapshot.documents.mapNotNull { it.toFileItem() }
                trySend(files)

            }
        }
        awaitClose {
            listener.remove()
        }
    }



    @SuppressLint("Recycle")
    override suspend fun addFile(file: FileItem, fileUri: Uri) {
        withContext(Dispatchers.IO){                                                                // Переносим всю работу с главного потока в фоновый (передача заказа повару)
            try{
                val accessKey = BuildConfig.AWS_ACCESS_KEY
                val secretKey = BuildConfig.AWS_SECRET_KEY
                val bucketName = "795ec9d1-a8dc-4c2f-9c69-33e40f61f256"
                val credentials = BasicAWSCredentials(accessKey, secretKey)                                 // Паспорт из ключей
                val s3client = AmazonS3Client(credentials)                                  // передача паспорта клиенту
                s3client.setEndpoint("https://s3.twcstorage.ru")                                            // работаем с таймвеб

                val tmpFile = File.createTempFile("tmp", null, context.cacheDir)  // временный пустой файл, так как андроид дает только Uri
                val inputStream = context.contentResolver.openInputStream(fileUri)                          // открываем поток для чтения (подключился к оригинальному файлу)
                val outputStream = FileOutputStream(tmpFile)                                                // открываем поток для записи (подключился к пустышке)
                inputStream?.copyTo(outputStream)                                                           // закачали пустышку байтами ориги
                inputStream?.close()                                                                        // закрываем поток чтения (кран)
                outputStream.close()                                                                        // закрываем поток записи (кран)

                val objectKey = "files/${file.folderId}/${System.currentTimeMillis()}_${file.name}"         // придумываем уникальный путь внутри бакета (files/1/170348534_Файл.pdf) для уникальности файлов
                val putRequest = PutObjectRequest(bucketName, objectKey, tmpFile)                           // собираем запрос (кладем файл, адрес)
                s3client.putObject(putRequest)                                            // отправляем файл на сервер
                val downloadUrl = "https://s3.twcstorage.ru/$bucketName/$objectKey"                         // получаем ссылку
                val newFileMap = hashMapOf(
                "id" to file.id,
                "folderId" to file.folderId,
                "name" to file.name,
                "size" to file.size,
                "type" to file.type.name,
                "date" to file.date,
                "author" to file.author,
                "category" to file.category.name,
                "fileUrl" to downloadUrl
            )
                firestore.collection("files").add(newFileMap).await()
                tmpFile.delete()


            } catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    override suspend fun deleteFile(fileId: Int) {
        firestore.collection("files").whereEqualTo("id", fileId).get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    document.reference.delete()
                }
            }

    }
}