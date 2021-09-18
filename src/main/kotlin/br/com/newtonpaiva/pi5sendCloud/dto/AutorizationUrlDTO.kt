package br.com.newtonpaiva.pi5sendCloud.dto

data class AutorizationUrlDTO(
        val authorizationToken: String? = null,
        val bucketId: String? = null,
        val uploadUrl: String? = null
)