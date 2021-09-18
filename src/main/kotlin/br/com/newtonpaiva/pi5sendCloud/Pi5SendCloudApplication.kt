package br.com.newtonpaiva.pi5sendCloud

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Pi5SendCloudApplication

fun main(args: Array<String>) {
	runApplication<Pi5SendCloudApplication>(*args)
}
