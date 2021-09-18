package br.com.newtonpaiva.pi5sendCloud.dto

data class AllowedDTO(
        val bucketId: String? = null,
        val bucketName: String? = null,
        val capabilities: List<String>,
        val namePrefix: String? = null
)