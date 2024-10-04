package br.usp.inovacao.hubusp.server.catalog
@kotlinx.serialization.Serializable

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import org.litote.kmongo.*

data class Iniciative(val id: String, val name: String, val classification: String, val localization: String)

fun Route.iniciativeRouting() {
    route("/iniciatives") {
        get {
            val iniciatives = getAllIniciatives()
            call.respond(iniciatives)
        }
    }
}

suspend fun getAllIniciatives(): List<Iniciative> {
    val client = KMongo.createClient() 
    val database = client.getDatabase("yourDatabaseName")
    val collection = database.getCollection<Iniciative>("iniciatives")
    return collection.find().toList()
}
