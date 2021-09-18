package br.com.newtonpaiva.pi5sendCloud.service

import br.com.newtonpaiva.pi5sendCloud.dto.*
import com.backblaze.b2.client.B2StorageClientFactory
import com.backblaze.b2.client.contentSources.B2ByteArrayContentSource
import com.backblaze.b2.client.contentSources.B2ContentTypes
import com.backblaze.b2.client.contentSources.B2Headers
import com.backblaze.b2.client.structures.B2UploadFileRequest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*


@Service
class SendCloudService {

    fun uploadFile(bucketId: String, message: String, name: String) {

        val uuidOne = UUID.randomUUID()

        val tmp = System.getProperty("java.io.tmpdir")

        val nameFile = File("$tmp/$name-$uuidOne.txt")
        nameFile.writeText(message)

        val b2Client = B2StorageClientFactory
                .createDefaultFactory()
                .create("003cd5502dfb9f90000000001", "K003k8wiQxhAh5o6q6fVXIUTEsde/nU", B2Headers.USER_AGENT)

        val path = nameFile.absolutePath
        val encoded = Files.readAllBytes(Paths.get(path))

        if(nameFile.exists()) {
            b2Client?.let {
                val source = B2ByteArrayContentSource.build(encoded)
                val request = B2UploadFileRequest
                        .builder(bucketId, "$name-$uuidOne.txt", B2ContentTypes.B2_AUTO, source).build()
                val fileVersion = it.uploadSmallFile(request)
            }
        }
        b2Client.close()

        println("FILE NAME: ${nameFile.name}")

    }


    fun createBucket(apiUrl: String, accountId: String, accountAuthorizationToken: String, username: String): String? {

        val bucketType = "allPublic"

        val url = "$apiUrl/b2api/v2/b2_create_bucket"

        val createBucket = CreateBucketDTO(accountId, username, bucketType)

        val restTemplate = RestTemplate()

        val headers = HttpHeaders()
        headers.add("Authorization", accountAuthorizationToken)
        val request: HttpEntity<CreateBucketDTO> = HttpEntity<CreateBucketDTO>(createBucket, headers)
        val response = restTemplate.exchange(url, HttpMethod.POST, request, AtributesDTO::class.java)

        if(response.statusCode == HttpStatus.OK){
            return response.body!!.bucketId
        }

        return ""

    }

    fun findBucketName(bucketsDTO: BucketsDTO?, username: String): String? {

        val hashmap = bucketsDTO!!.buckets.map { it.bucketName }

        if (hashmap.contains(username)) {
            bucketsDTO.buckets.forEach { item ->
                 if (item.bucketName == username) {
                    return item.bucketId!!
                } else {
                    ""
                }
            }
        }
        return ""
    }


    fun listBuckets(apiUrl: String, authorizationToken: String, accountId: String): BucketsDTO? {

        val url = "$apiUrl/b2api/v2/b2_list_buckets"

        val accountDTO = AccountDTO(accountId)

        val restTemplate = RestTemplate()

        val headers = HttpHeaders()
        headers.add("Authorization", authorizationToken)
        val request: HttpEntity<AccountDTO> = HttpEntity<AccountDTO>(accountDTO, headers)
        val response = restTemplate.exchange(url, HttpMethod.POST, request, BucketsDTO::class.java)

        return response.body
    }

    fun createConnection(): ObjectDTO? {

        val applicationKeyId = "003cd5502dfb9f90000000001" // Obtained from your B2 account page.

        val applicationKey = "K003k8wiQxhAh5o6q6fVXIUTEsde/nU" // Obtained from your B2 account page.

        val headerForAuthorizeAccount = "Basic " + Base64.getEncoder().encodeToString("$applicationKeyId:$applicationKey".toByteArray())

        val url = "https://api.backblazeb2.com/b2api/v2/b2_authorize_account"

        val restTemplate = RestTemplate()

        val headers = HttpHeaders()
        headers.add("Authorization", headerForAuthorizeAccount)

        val request: HttpEntity<String> = HttpEntity<String>(headers)
        val response = restTemplate.exchange(url, HttpMethod.GET, request, ObjectDTO::class.java)

        return response.body

    }
}