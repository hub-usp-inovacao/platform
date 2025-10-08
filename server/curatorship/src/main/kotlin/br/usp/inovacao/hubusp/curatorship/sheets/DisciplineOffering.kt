package br.usp.inovacao.hubusp.curatorship.sheets

import it.skrape.core.htmlDocument
import it.skrape.fetcher.BlockingFetcher
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.Request
import it.skrape.fetcher.extractIt
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.DocElement

@kotlinx.serialization.Serializable
data class DisciplineOffering(
    var classCode: String? = null,
    var startDate: String? = null,
    var endDate: String? = null,
    var professors: Set<String> = emptySet(),
) {
    companion object {
        private fun parseHtmlTable(tableElm: DocElement?) =
            tableElm?.findAll("tr")?.map { it.findAll("td").map { it.text } } ?: emptyList()

        private fun getProfessorsFromJupiterTable(
            table: List<List<String>>,
        ): Set<String> {
            var professorCol: Int? = null
            var professorRow: Int? = null

            professorFound@ for ((rowIndex, row) in table.withIndex()) {
                for ((colIndex, col) in row.withIndex()) {
                    if (col.contains("prof", true)) {
                        professorCol = colIndex
                        professorRow = rowIndex
                        break@professorFound
                    }
                }
            }

            if (professorRow == null || professorCol == null) {
                return emptySet()
            }

            return table
                .subList(professorRow + 1, table.size)
                .mapNotNull { it.getOrNull(professorCol) }
                .toSet()
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
                        response { htmlDocument { findAll("td > div:has(table:nth-of-type(3))") } }
                    }
                    .forEach {
                        val generalTable = it.children.getOrNull(0)

                        if (generalTable != null) {
                            val regexPattern =
                                "Código da Turma: ([^ ]*) *Início: ([^ ]*) *Fim: ([^ ]*) *Tipo da Turma: (.*)"

                            val groupValues =
                                Regex(regexPattern).find(generalTable.text)?.groupValues

                            if (groupValues != null) {
                                val scheduleTable = parseHtmlTable(it.children.getOrNull(2))

                                offerings.add(
                                    DisciplineOffering(
                                        classCode = groupValues.getOrNull(1),
                                        startDate = groupValues.getOrNull(2),
                                        endDate = groupValues.getOrNull(3),
                                        professors = getProfessorsFromJupiterTable(scheduleTable),
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
                                "https://uspdigital.usp.br/janus/TurmaDet?sgldis=${sgldis}&ofe=${classCode}"
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
