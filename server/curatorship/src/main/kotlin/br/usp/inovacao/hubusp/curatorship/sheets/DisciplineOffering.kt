package br.usp.inovacao.hubusp.curatorship.sheets

import it.skrape.core.htmlDocument
import it.skrape.fetcher.BlockingFetcher
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.Request
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape

@kotlinx.serialization.Serializable
data class DisciplineOffering(
    val classCode: String?,
    val startDate: String?,
    val endDate: String?,
    val professors: Set<String>,
) {
    companion object {
        fun trySetFromJupiter(
            sgldis: String,
            timeoutMs: Int = 20000,
            fetcher: BlockingFetcher<Request> = HttpFetcher,
        ): Set<DisciplineOffering> {
            val offerings: MutableSet<DisciplineOffering> = mutableSetOf()

            try {
                skrape(fetcher) {
                        request {
                            url = "https://uspdigital.usp.br/jupiterweb/obterTurma?sgldis=${sgldis}"
                            timeout = timeoutMs
                        }
                        response { htmlDocument { findAll("td > div:has(table:nth-of-type(3))") } }
                    }
                    .forEach {
                        val generalTable = it.children.getOrNull(0)
                        val scheduleTable = it.children.getOrNull(2)

                        if (generalTable != null && scheduleTable != null) {
                            val regexPattern =
                                "Código da Turma: ([^ ]*) *Início: ([^ ]*) *Fim: ([^ ]*) *Tipo da Turma: (.*)"

                            val groupValues =
                                Regex(regexPattern).find(generalTable.text)?.groupValues

                            if (groupValues != null) {
                                offerings.add(
                                    DisciplineOffering(
                                        classCode = groupValues.getOrNull(1),
                                        startDate = groupValues.getOrNull(2),
                                        endDate = groupValues.getOrNull(3),
                                        professors =
                                            scheduleTable.children
                                                .getOrNull(0)
                                                ?.children
                                                ?.drop(1)
                                                ?.mapNotNull { it.children.getOrNull(3)?.text }
                                                ?.toSet() ?: emptySet(),
                                    ),
                                )
                            }
                        }
                    }
            } catch (e: Exception) {
                return offerings
            }

            return offerings
        }
    }
}
