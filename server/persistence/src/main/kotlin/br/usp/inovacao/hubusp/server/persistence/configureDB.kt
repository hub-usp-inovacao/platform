package br.usp.inovacao.hubusp.server.persistence

import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.IndexOptions
import org.litote.kmongo.KMongo
import org.litote.kmongo.createIndex
import org.litote.kmongo.getCollection

fun connectToDB(protocol: String, host: String, port: String, dbName: String): MongoDatabase {
    val client = KMongo.createClient("$protocol://$host:$port")
    return client.getDatabase(dbName)
}

fun configureDB(protocol: String, host: String, port: String, dbName: String): MongoDatabase {
    val db = connectToDB(protocol, host, port, dbName)
    ensureTextIndexes(db)

    db.getCollection("companies")
        .createIndex("""{"cnpj":1}""", indexOptions = IndexOptions().unique(true))

    db.getCollection("disciplines")
        .createIndex("""{"name":1}""", indexOptions = IndexOptions().unique(true))

    db.getCollection("skills")
        .createIndex("""{"name":1}""", indexOptions = IndexOptions().unique(true))

    db.getCollection("patents")
        .createIndex("""{"name":1}""", indexOptions = IndexOptions().unique(true))

    db.getCollection("initiatives")
        .createIndex("""{"name":1}""", indexOptions = IndexOptions().unique(true))

    return db
}
