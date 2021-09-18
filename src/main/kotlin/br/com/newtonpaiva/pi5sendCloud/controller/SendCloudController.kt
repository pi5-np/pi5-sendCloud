package br.com.newtonpaiva.pi5sendCloud.controller

import br.com.newtonpaiva.pi5sendCloud.dto.ObjectDTO
import br.com.newtonpaiva.pi5sendCloud.entity.MessageEntity
import br.com.newtonpaiva.pi5sendCloud.service.SendCloudService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/send")
class SendCloudController(
        private val sendCloudService: SendCloudService
) {

    @PostMapping
    fun endpointAll(@RequestBody messageEntity: MessageEntity){
        val responseConnection: ObjectDTO? = sendCloudService.createConnection()
        val accountId = responseConnection?.accountId
        val apiUrl = responseConnection?.apiUrl
        val authorizationToken = responseConnection?.authorizationToken
        if(accountId != null && apiUrl != null && authorizationToken != null){
            val responseBucket = sendCloudService.listBuckets(apiUrl, authorizationToken, accountId)
            val responseFindBucket = sendCloudService.findBucketName(responseBucket, messageEntity.username!!)
            if(responseFindBucket != ""){
                println(("BUCKET ID: $responseFindBucket"))
                sendCloudService.uploadFile(responseFindBucket!!, messageEntity.message!!, messageEntity.name!!)
            } else {
                val responseCreatedBucket = sendCloudService.createBucket(apiUrl, accountId, authorizationToken, messageEntity.username)
                if(responseCreatedBucket != ""){
                    println(("BUCKET ID: $responseCreatedBucket"))
                    sendCloudService.uploadFile(responseCreatedBucket!!, messageEntity.message!!, messageEntity.name!!)
                } else {
                    println("ERROR!")
                }
            }
        }
    }
}