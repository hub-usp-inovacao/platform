package br.usp.inovacao.hubusp.plugins

import com.mongodb.client.MongoDatabase
import io.ktor.server.application.Application
import org.litote.kmongo.KMongo

fun Application.configureDatabase(): MongoDatabase {
    val proto = environment.config.property("datasource.proto").getString()
    val host = environment.config.property("datasource.host").getString()
    val port = environment.config.property("datasource.port").getString()
    val db = environment.config.property("datasource.database").getString()

    val client = KMongo.createClient("$proto://$host:$port")

    return client.getDatabase(db)
}