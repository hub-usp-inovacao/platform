package br.usp.inovacao.hubusp.server.discovery

@Suppress("unused")
class Journey(private val repo: JourneyRepository) {
    fun learnStep(filter: LearnStepFilters) = repo.find(JourneyStep.Learn, filter)

    fun practiceStep(filter: PracticeStepFilter) = repo.find(JourneyStep.Practice, filter)

    fun createStep(filter: CreateStepFilter) = repo.find(JourneyStep.Create, filter)

    fun improveStep(filter: ImproveStepFilter) = repo.find(JourneyStep.Improve, filter)

    fun fundStep(filter: FundStepFilter) = repo.find(JourneyStep.Fund, filter)
}
