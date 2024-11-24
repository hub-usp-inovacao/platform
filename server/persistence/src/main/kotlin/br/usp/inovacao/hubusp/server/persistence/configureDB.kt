package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.Company
import br.usp.inovacao.hubusp.server.catalog.PDI
import br.usp.inovacao.hubusp.server.catalog.Patent
import com.mongodb.MongoCommandException
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.IndexOptions
import org.litote.kmongo.KMongo
import org.litote.kmongo.createIndex
import org.litote.kmongo.getCollection

fun configureDB(protocol: String, host: String, port: String, dbName: String): MongoDatabase {
    val client = KMongo.createClient("$protocol://$host:$port")

    val db =  client.getDatabase(dbName)

    createIndexOrNothing(
        database = db,
        collectionName = "pdis",
        indexQuery = """{"name":"text","description":"text","coordinator":"text","tags":"text"}"""
    )

    createIndexOrNothing(
        database = db,
        collectionName = "companies",
        indexQuery = "{" + Company.INDEXABLE_PROPERTIES.joinToString(",") { """"$it":"text"""" } + "}"
    )

    createIndexOrNothing(
        database = db,
        collectionName = "disciplines",
        indexQuery = """{"name":"text", "description":"text"}"""
    )

    createIndexOrNothing(
        database = db,
        collectionName = "skills",
        indexQuery = """{"name":"text","skills":"text","equipments":"text","services":"text","keywords":"text"}"""
    )

    createIndexOrNothing(
        database = db,
        collectionName = "initiatives",
        indexQuery = """{"description":"text","name":"text","tags":"text"}"""
    )

    db.getCollection("companies")
        .createIndex("""{"cnpj":1}""", indexOptions = IndexOptions().unique(true))

    db.getCollection("disciplines")
        .createIndex("""{"name":1}""", indexOptions = IndexOptions().unique(true))

    db.getCollection("skills")
        .createIndex("""{"name":1}""", indexOptions = IndexOptions().unique(true))

    db.getCollection<Patent>("patents")
        .createIndex("""{"name":"text","summary":"text","owners":"text","inventors":"text"}""")

    db.getCollection("initiatives")
        .createIndex("""{"name":1}""", indexOptions = IndexOptions().unique(true))

    return db
}

private fun createIndexOrNothing(database: MongoDatabase, collectionName: String, indexQuery: String) = try {
    database
        .getCollection(collectionName)
        .createIndex(indexQuery)
} catch (_: MongoCommandException) {}
