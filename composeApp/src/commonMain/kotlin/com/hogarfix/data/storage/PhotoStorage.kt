package com.hogarfix.data.storage

interface PhotoStorage {
    suspend fun savePhoto(interventionId: Long, photoBytes: ByteArray): String
    suspend fun deletePhoto(photoUri: String)
    suspend fun getPhotoBytes(photoUri: String): ByteArray?
}

expect fun createPhotoStorage(): PhotoStorage
