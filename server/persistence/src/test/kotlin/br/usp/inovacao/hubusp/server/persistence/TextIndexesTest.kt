package br.usp.inovacao.hubusp.server.persistence

import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.IndexOptions
import org.bson.Document
import java.util.UUID
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TextIndexesTest {
    private lateinit var database: MongoDatabase
    private lateinit var databaseName: String

    @BeforeTest
    fun setup() {
        databaseName = "test_text_indexes_${UUID.randomUUID().toString().replace("-", "")}"
        database = connect(databaseName)
    }

    @AfterTest
    fun teardown() {
        database.drop()
    }

    @Test
    fun `it creates named Portuguese text indexes on an empty database`() {
        configure(databaseName)

        TEXT_INDEX_SPECS.forEach { spec ->
            val index = database.textIndex(spec.collectionName)

            assertEquals(spec.name, index.getString("name"))
            assertEquals(TEXT_INDEX_LANGUAGE, index.getString("default_language"))
        }
    }

    @Test
    fun `it preserves an incompatible text index during startup`() {
        val spec = TEXT_INDEX_SPECS.first()
        database.getCollection(spec.collectionName).createIndex(
            spec.keys(),
            IndexOptions().name("pdis_fts_en").defaultLanguage("english"),
        )

        assertFailsWith<TextIndexMigrationRequiredException> {
            configure(databaseName)
        }

        val index = database.textIndex(spec.collectionName)
        assertEquals("pdis_fts_en", index.getString("name"))
        assertEquals("english", index.getString("default_language"))
    }

    @Test
    fun `it rebuilds English text indexes and is idempotent`() {
        val oldSpec = TEXT_INDEX_SPECS.first()
        database.getCollection(oldSpec.collectionName).createIndex(
            oldSpec.keys(),
            IndexOptions().name("pdis_fts_en").defaultLanguage("english"),
        )

        val firstMigration = migrateTextIndexes(database)

        assertEquals(TextIndexMigrationStatus.REBUILT, firstMigration.first().status)
        assertTrue(firstMigration.drop(1).all { it.status == TextIndexMigrationStatus.CREATED })
        TEXT_INDEX_SPECS.forEach { spec ->
            val index = database.textIndex(spec.collectionName)
            assertEquals(spec.name, index.getString("name"))
            assertEquals(TEXT_INDEX_LANGUAGE, index.getString("default_language"))
        }

        val secondMigration = migrateTextIndexes(database)

        assertTrue(secondMigration.all { it.status == TextIndexMigrationStatus.UNCHANGED })
    }

    @Test
    fun `it applies Portuguese stemming to text searches`() {
        migrateTextIndexes(database)
        val collection = database.getCollection("pdis")
        collection.insertOne(Document("name", "Programa de inovações"))

        val matches = collection.countDocuments(Filters.text("inovação"))

        assertEquals(1L, matches)
    }

    private fun configure(dbName: String) = configureDB(
        protocol = testProtocol,
        host = testHost,
        port = testPort,
        dbName = dbName,
    )

    private fun connect(dbName: String) = connectToDB(
        protocol = testProtocol,
        host = testHost,
        port = testPort,
        dbName = dbName,
    )

    private fun MongoDatabase.textIndex(collectionName: String): Document =
        getCollection(collectionName).listIndexes().first { it["weights"] is Document }

    private companion object {
        val testProtocol = System.getenv("HUB_TEST_DATASOURCE_PROTOCOL") ?: "mongodb"
        val testHost = System.getenv("HUB_TEST_DATASOURCE_HOST") ?: "localhost"
        val testPort = System.getenv("HUB_TEST_DATASOURCE_PORT") ?: "27017"
    }
}
