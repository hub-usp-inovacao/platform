package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.Company
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.IndexOptions
import org.bson.Document

internal const val TEXT_INDEX_LANGUAGE = "portuguese"

internal data class TextIndexSpec(
    val collectionName: String,
    val textFields: List<String>,
    val ascendingFields: List<String> = emptyList(),
) {
    val name = "${collectionName}_fts_pt_v1"

    fun keys() = Document().apply {
        textFields.forEach { append(it, "text") }
        ascendingFields.forEach { append(it, 1) }
    }

    fun options() = IndexOptions()
        .name(name)
        .defaultLanguage(TEXT_INDEX_LANGUAGE)
}

internal val TEXT_INDEX_SPECS = listOf(
    TextIndexSpec(
        collectionName = "pdis",
        textFields = listOf("name", "description", "coordinator", "tags"),
    ),
    TextIndexSpec(
        collectionName = "companies",
        textFields = Company.INDEXABLE_PROPERTIES,
    ),
    TextIndexSpec(
        collectionName = "disciplines",
        textFields = listOf("name", "description"),
        ascendingFields = listOf("beingOffered"),
    ),
    TextIndexSpec(
        collectionName = "skills",
        textFields = listOf("name", "skills", "equipments", "services", "keywords"),
    ),
    TextIndexSpec(
        collectionName = "initiatives",
        textFields = listOf("description", "name", "tags"),
    ),
    TextIndexSpec(
        collectionName = "patents",
        textFields = listOf("name", "summary", "owners", "inventors"),
    ),
)

class TextIndexMigrationRequiredException(
    collectionName: String,
    indexName: String,
) : IllegalStateException(
    "Text index '$indexName' in '$collectionName' is incompatible. " +
        "Run the migrate-text-indexes CLI command before starting the application.",
)

enum class TextIndexMigrationStatus {
    CREATED,
    REBUILT,
    UNCHANGED,
}

data class TextIndexMigrationResult(
    val collectionName: String,
    val status: TextIndexMigrationStatus,
)

internal fun ensureTextIndexes(database: MongoDatabase) {
    TEXT_INDEX_SPECS.forEach { spec ->
        val collection = database.getCollection(spec.collectionName)
        val current = collection.currentTextIndex()

        when {
            current == null -> collection.createIndex(spec.keys(), spec.options())
            current.matches(spec) -> Unit
            else -> throw TextIndexMigrationRequiredException(
                collectionName = spec.collectionName,
                indexName = current.getString("name"),
            )
        }
    }
}

fun migrateTextIndexes(database: MongoDatabase): List<TextIndexMigrationResult> =
    TEXT_INDEX_SPECS.map { spec -> migrateTextIndex(database, spec) }

private fun migrateTextIndex(
    database: MongoDatabase,
    spec: TextIndexSpec,
): TextIndexMigrationResult {
    val collection = database.getCollection(spec.collectionName)
    val current = collection.currentTextIndex()

    val status = when {
        current == null -> TextIndexMigrationStatus.CREATED
        current.matches(spec) -> TextIndexMigrationStatus.UNCHANGED
        else -> {
            collection.dropIndex(current.getString("name"))
            TextIndexMigrationStatus.REBUILT
        }
    }

    if (status != TextIndexMigrationStatus.UNCHANGED) {
        collection.createIndex(spec.keys(), spec.options())
        check(collection.currentTextIndex()?.matches(spec) == true) {
            "Text index migration could not be verified for '${spec.collectionName}'"
        }
    }

    return TextIndexMigrationResult(spec.collectionName, status)
}

private fun MongoCollection<Document>.currentTextIndex(): Document? =
    listIndexes().firstOrNull { it["weights"] is Document }

private fun Document.matches(spec: TextIndexSpec): Boolean {
    val weights = get("weights", Document::class.java) ?: return false
    val keys = get("key", Document::class.java) ?: return false
    val actualAscendingFields = keys
        .filterKeys { it != "_fts" && it != "_ftsx" }
        .mapValues { (_, value) -> (value as? Number)?.toInt() }
    val expectedAscendingFields = spec.ascendingFields.associateWith { 1 }

    return getString("default_language") == TEXT_INDEX_LANGUAGE &&
        weights.keys == spec.textFields.toSet() &&
        weights.values.all { (it as? Number)?.toInt() == 1 } &&
        actualAscendingFields == expectedAscendingFields
}
