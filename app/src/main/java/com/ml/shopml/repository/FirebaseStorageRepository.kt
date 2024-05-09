package com.ml.shopml.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class FirebaseStorageRepository {
    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference

    fun uploadImage(imageUri: Uri): Task<String> {
        val imageRef = storageRef.child("product-image/${UUID.randomUUID()}.png")
        return imageRef.putFile(imageUri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                imageRef.downloadUrl
            }
            .continueWith { downloadUrlTask ->
                if (!downloadUrlTask.isSuccessful) {
                    downloadUrlTask.exception?.let { throw it }
                }
                downloadUrlTask.result.toString()
            }
    }

    fun deleteImage(imageUrl: String): Task<Void> {
        return storageRef.child("product-image").child(getImageNameFromUrl(imageUrl)).delete()
    }

    private fun getImageNameFromUrl(imageUrl: String): String {
        return imageUrl.substringAfterLast("/")
    }
}