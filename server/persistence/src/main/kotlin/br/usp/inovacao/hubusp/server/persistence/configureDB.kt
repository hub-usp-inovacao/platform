package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.PDI
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo
import org.litote.kmongo.createIndex
import org.litote.kmongo.getCollection

fun configureDB(protocol: String, host: String, port: String, dbName: String): MongoDatabase {
    val client = KMongo.createClient("$protocol://$host:$port")

    val db =  client.getDatabase(dbName)

    db.getCollection<PDI>("pdis")
        .createIndex("""{"name":"text","description":"text","coordinator":"text","tags":"text"}""")

    db.getCollection("skills")
        .createIndex("""{"name":"text","skills":"text","equipments":"text","services":"text","keywords":"text"}""")

    return db
}
