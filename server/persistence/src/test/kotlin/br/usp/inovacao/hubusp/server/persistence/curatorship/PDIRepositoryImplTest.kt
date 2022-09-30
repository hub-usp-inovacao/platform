package br.usp.inovacao.hubusp.server.persistence.curatorship

import br.usp.inovacao.hubusp.server.persistence.connectToTestDb
import br.usp.inovacao.hubusp.server.persistence.models.PDIModel
import com.mongodb.client.MongoCollection
import org.litote.kmongo.getCollection
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PDIRepositoryImplTest {
    private lateinit var collection: MongoCollection<PDIModel>
    private lateinit var underTest: PDIRepositoryImpl

    @BeforeTest
    fun setup() {
        val db = connectToTestDb()
        collection = db.getCollection<PDIModel>("pdis")
        underTest = PDIRepositoryImpl(db)
    }

    @Test
    fun `it empties the database when called`() {
        // given
        populateTestDB()

        // when
        underTest.deleteAll()

        // then
        val expectedCount = 0L
        val actualCount = countDocuments()
        assertEquals(expectedCount, actualCount)
    }

    private fun countDocuments(): Long {
        return collection.countDocuments()
    }

    private fun populateTestDB() {
        val pdiModels = listOf(
            PDIModel(
                category = "CEPID",
                name = "Blaus",
                campus = "USP Leste",
                unity = "Escola de Artes, Ciências e Humanidades - EACH",
                coordinator = "",
                site = "",
                email = "",
                phone = "",
                description = "lorem ipsum",
                tags = setOf("foo", "baz")
            ),
            PDIModel(
                category = "CEPID",
                name = "Flaus",
                campus = "USP Leste",
                unity = "Escola de Artes, Ciências e Humanidades - EACH",
                coordinator = "",
                site = "",
                email = "",
                phone = "",
                description = "lorem ipsum too",
                tags = setOf("foo", "baz")
            )
        )

        collection.insertMany(pdiModels)
    }
}