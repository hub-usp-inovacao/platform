package br.usp.inovacao.hubusp.curatorship.sheets

import it.skrape.core.htmlDocument
import it.skrape.fetcher.BlockingFetcher
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.Request
import it.skrape.fetcher.extractIt
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape

@kotlinx.serialization.Serializable
data class DisciplineOffering(
    var classCode: String? = null,
    var startDate: String? = null,
    var endDate: String? = null,
    var professors: Set<String> = emptySet(),
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

        fun trySetFromJanus(
            sgldis: String,
            timeoutMs: Int = 20000,
            fetcher: BlockingFetcher<Request> = HttpFetcher,
        ): Set<DisciplineOffering> {
            try {
                val classCode =
                    skrape(fetcher) {
                        request {
                            url =
                                "https://uspdigital.usp.br/janus/DisciplinaAux?tipo=T&sgldis=${sgldis}"
                            timeout = timeoutMs
                        }
                        response {
                            htmlDocument {
                                // example: publico/turma/SCC5832/2 <- offering number (classCode)
                                "td > a" { findSecond { attribute("href").split('/').last() } }
                            }
                        }
                    }

                return setOf(
                    skrape(fetcher) {
                        request {
                            url =
                                "https://uspdigital.usp.br/janus/TurmaDet?sgldis=SCC5832&ofe=${classCode}"
                            timeout = timeoutMs
                        }
                        extractIt<DisciplineOffering> {
                            it.classCode = classCode
                            htmlDocument {
                                it.startDate =
                                    findFirst("tr:nth-of-type(12) font:nth-of-type(2)") { text }
                                it.endDate =
                                    findFirst("tr:nth-of-type(12) font:nth-of-type(4)") { text }
                                it.professors =
                                    findAll(
                                        "tr:nth-of-type(16) tr:not(:first-child)",
                                    ) {
                                        map { it.text }.toSet()
                                    }
                            }
                        }
                    },
                )
            } catch (e: Exception) {
                // If there's no offering or if the selection fails for whatever reason
                return emptySet()
            }
        }
    }
}
