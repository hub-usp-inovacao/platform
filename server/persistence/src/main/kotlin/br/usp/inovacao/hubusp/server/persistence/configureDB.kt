package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.Company
import br.usp.inovacao.hubusp.server.catalog.PDI
import com.mongodb.MongoCommandException
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo
import org.litote.kmongo.createIndex
import org.litote.kmongo.getCollection

fun configureDB(protocol: String, host: String, port: String, dbName: String): MongoDatabase {
    val client = KMongo.createClient("$protocol://$host:$port")

    val db =  client.getDatabase(dbName)

//    try {
//        db.getCollection<PDI>("pdis")
//            .createIndex("""{"name":"text","description":"text","coordinator":"text","tags":"text"}""")
//    } catch (_: MongoCommandException) {}

//    try {
//        db.getCollection<Company>("companies")
//            .createIndex("{" + Company.INDEXABLE_PROPERTIES.joinToString(",") { """"$it":"text"""" } + "}")
//    } catch (_: MongoCommandException) {}

    createIndexOrNothing(
        database = db,
        collectionName = "pdis",
        indexQuery = """{"name":"text","description":"text","coordinator":"text","tags":"text"}"""
    )

    createIndexOrNothing(
        database =db,
        collectionName = "companies",
        indexQuery = "{" + Company.INDEXABLE_PROPERTIES.joinToString(",") { """"$it":"text"""" } + "}"
    )

    return db
}

private fun createIndexOrNothing(database: MongoDatabase, collectionName: String, indexQuery: String) = try {
    database
        .getCollection(collectionName)
        .createIndex(indexQuery)
} catch (_: MongoCommandException) {}
