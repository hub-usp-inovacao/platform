package br.usp.inovacao.hubusp.curatorship.sheets

import it.skrape.core.*
import it.skrape.fetcher.BlockingFetcher
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.Request
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.*
import it.skrape.selects.html5.*
import org.valiktor.functions.*

@kotlinx.serialization.Serializable
data class DisciplineOffering(
    val classCode: String?,
    val startDate: String?,
    val endDate: String?,
    val professors: Set<String>,
) {
    companion object {
        fun tryFromJupiter(generalTableText: String, professors: Set<String>): DisciplineOffering? {
            val regexPattern =
                "Código da Turma: ([^ ]*) *Início: ([^ ]*) *Fim: ([^ ]*) *Tipo da Turma: (.*)"

            val groupValues = Regex(regexPattern).find(generalTableText)?.groupValues

            return if (groupValues != null) {
                DisciplineOffering(
                    classCode = groupValues.getOrNull(1),
                    startDate = groupValues.getOrNull(2),
                    endDate = groupValues.getOrNull(3),
                    professors,
                )
            } else {
                null
            }
        }

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
                    response {
                        htmlDocument {
                            "td > div:has(table:nth-of-type(3))" {
                                findAll {
                                    forEach {
                                        val generalTable = it.children.getOrNull(0)
                                        val scheduleTable = it.children.getOrNull(2)

                                        if (generalTable != null && scheduleTable != null) {
                                            val professors =
                                                scheduleTable.children
                                                    .getOrNull(0)
                                                    ?.children
                                                    ?.drop(1)
                                                    ?.mapNotNull { it.children.getOrNull(3)?.text }

                                            DisciplineOffering.tryFromJupiter(
                                                    generalTable.text,
                                                    professors?.toSet() ?: emptySet(),
                                                )
                                                ?.let { offerings.add(it) }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                return offerings
            }

            return offerings
        }

        // TODO: Janus
        // fun trySetFromJanus
    }
}
