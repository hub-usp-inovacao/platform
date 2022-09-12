package br.usp.inovacao.hubusp.server.discovery

interface JourneyRepository {
    fun find(step: JourneyStep, filter: Filter): Set<JourneyRecord>
}