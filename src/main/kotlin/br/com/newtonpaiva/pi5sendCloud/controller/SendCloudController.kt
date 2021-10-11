package br.com.newtonpaiva.pi5sendCloud.controller

import br.com.newtonpaiva.pi5sendCloud.dto.ObjectDTO
import br.com.newtonpaiva.pi5sendCloud.entity.MessageEntity
import br.com.newtonpaiva.pi5sendCloud.service.SendCloudService
import br.com.newtonpaiva.pi5sendCloud.service.ValidateJwt
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/send")
class SendCloudController(
        private val sendCloudService: SendCloudService,
        private val validateJwt: ValidateJwt
) {

    @PostMapping
    fun endpointAll(@RequestBody messageEntity: MessageEntity, request: HttpServletRequest): ResponseEntity<Any>{

        try {
            val tokenResponse = validateJwt.getJwtFromRequest(request)
            if(tokenResponse != ""){
                validateJwt.validateToken(tokenResponse)
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is not found!")
            }
            try {
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
                return ResponseEntity.ok().body("")
            } catch (e: Exception){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
            }
        } catch (e: Exception){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.message)
        }
    }
}