package br.com.newtonpaiva.pi5sendCloud.dto

data class CreateBucketDTO(
        val accountId: String? = null,
        val bucketName: String? = null,
        val bucketType: String? = null
)