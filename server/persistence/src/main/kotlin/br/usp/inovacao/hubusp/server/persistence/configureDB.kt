package br.usp.inovacao.hubusp.server.persistence

import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo

fun configureDB(protocol: String, host: String, port: String, dbName: String): MongoDatabase {
    val client = KMongo.createClient("$protocol://$host:$port")

    return client.getDatabase(dbName)
}
