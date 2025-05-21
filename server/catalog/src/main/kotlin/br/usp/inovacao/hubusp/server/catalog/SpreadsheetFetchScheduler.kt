package br.usp.inovacao.hubusp.server.catalog

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SpreadsheetFetchScheduler(
    private val spreadsheetFetchService: SpreadsheetFetchService
) {
    @Scheduled(cron = "0 0 9 ? * MON")
    fun fetchAllWithReport() {
        spreadsheetFetchService.fetchAllWithReport()
    }
}