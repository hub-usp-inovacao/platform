package br.usp.inovacao.hubusp.server.discovery

import kotlin.test.Test
import kotlin.test.assertTrue

class JourneyTest {
    @Test
    fun `it delegates learn step`() {
        val calls = mutableListOf<Pair<JourneyStep, Filter>>()
        val underTest = Journey(repo(calls))
        val filter = LearnStepFilters(nature = "Graduação", level = "Inicial")

        assertTrue(setOf(JourneyRecord("learn")) == underTest.learnStep(filter))
        assertTrue(calls == listOf(JourneyStep.Learn to filter))
    }

    @Test
    fun `it delegates practice step`() {
        val calls = mutableListOf<Pair<JourneyStep, Filter>>()
        val underTest = Journey(repo(calls))
        val filter = PracticeStepFilter(category = "Empreendedorismo")

        assertTrue(setOf(JourneyRecord("practice")) == underTest.practiceStep(filter))
        assertTrue(calls == listOf(JourneyStep.Practice to filter))
    }

    @Test
    fun `it delegates create step`() {
        val calls = mutableListOf<Pair<JourneyStep, Filter>>()
        val underTest = Journey(repo(calls))
        val filter = CreateStepFilter(insideUSP = true)

        assertTrue(setOf(JourneyRecord("create")) == underTest.createStep(filter))
        assertTrue(calls == listOf(JourneyStep.Create to filter))
    }

    @Test
    fun `it delegates improve step`() {
        val calls = mutableListOf<Pair<JourneyStep, Filter>>()
        val underTest = Journey(repo(calls))
        val filter = ImproveStepFilter(category = "CEPID")

        assertTrue(setOf(JourneyRecord("improve")) == underTest.improveStep(filter))
        assertTrue(calls == listOf(JourneyStep.Improve to filter))
    }

    @Test
    fun `it delegates fund step`() {
        val calls = mutableListOf<Pair<JourneyStep, Filter>>()
        val underTest = Journey(repo(calls))
        val filter = FundStepFilter(type = "Edital")

        assertTrue(setOf(JourneyRecord("fund")) == underTest.fundStep(filter))
        assertTrue(calls == listOf(JourneyStep.Fund to filter))
    }

    private fun repo(calls: MutableList<Pair<JourneyStep, Filter>>) = object : JourneyRepository {
        override fun find(step: JourneyStep, filter: Filter): Set<JourneyRecord> {
            calls += step to filter
            return setOf(JourneyRecord(name = step.name.lowercase()))
        }
    }
}
