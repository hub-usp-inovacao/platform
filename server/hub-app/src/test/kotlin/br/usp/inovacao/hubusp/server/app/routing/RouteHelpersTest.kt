package br.usp.inovacao.hubusp.server.app.routing

import br.usp.inovacao.hubusp.server.discovery.ImproveStepFilter
import br.usp.inovacao.hubusp.server.discovery.LearnStepFilters
import br.usp.inovacao.hubusp.server.discovery.PracticeStepFilter
import br.usp.inovacao.hubusp.server.persistence.journey.toCollectionFilter
import io.ktor.http.Parameters
import io.ktor.http.parametersOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RouteHelpersTest {
    @Test
    fun `it parses PT-BR booleans`() {
        assertTrue("sim".toBooleanPTBR())
        assertFalse("N%C3%A3o".toBooleanPTBR())
    }

    @Test
    fun `it parses search params`() {
        val catalog = parametersOf(
            "categories" to listOf("a,b"),
            "campus" to listOf("Butantã"),
            "term" to listOf("foo"),
            "beingOffered" to listOf("sim"),
        )
        val pdi = catalog.toPDISearchParams()
        val discipline = catalog.toDisciplineSearchParams()

        assertEquals(setOf("a", "b"), pdi.categories)
        assertEquals(true, discipline.beingOffered)
    }

    @Test
    fun `it parses journey params`() {
        val params = parametersOf(
            "nature" to listOf("pub"),
            "level" to listOf("grad"),
            "category" to listOf("cat"),
            "insideUSP" to listOf("true"),
            "type" to listOf("seed"),
        )

        assertEquals("pub", params.toLearnStepParams().nature)
        assertEquals("cat", params.toPracticeStepParams().category)
        assertEquals(true, params.toCreateStepParams().insideUSP)
        assertEquals("cat", params.toImproveStepParams().category)
        assertEquals("seed", params.toFundStepParams().type)
    }

    @Test
    fun `it formats journey filters into collection queries`() {
        assertEquals("{level:\"grad\",nature:\"pub\"}", LearnStepFilters("pub", "grad").toCollectionFilter())
        assertEquals("{classification:\"spin-off\"}", PracticeStepFilter("spin-off").toCollectionFilter())
        assertEquals("{category:\"seed\"}", ImproveStepFilter("seed").toCollectionFilter())
    }
}
