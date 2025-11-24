package br.usp.inovacao.hubusp.curatorship.sheets

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import it.skrape.core.htmlDocument
import it.skrape.selects.DocElement
import kotlinx.coroutines.runBlocking

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
            httpEngine: HttpClientEngine = CIO.create(),
        ): Set<DisciplineOffering> {
            val client = HttpClient(httpEngine)
            val offerings: MutableSet<DisciplineOffering> = mutableSetOf()

            try {

                htmlDocument(
                        runBlocking {
                            client
                                .get(
                                    "https://uspdigital.usp.br/jupiterweb/obterTurma?sgldis=${sgldis}") {
                                        timeout { requestTimeoutMillis = timeoutMs.toLong() }
                                    }
                                .bodyAsText()
                        },
                    ) {
                        findAll("td > div:has(table:nth-of-type(3))")
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
            httpEngine: HttpClientEngine = CIO.create(),
        ): Set<DisciplineOffering> {
            val client = HttpClient(httpEngine)

            try {
                val classCode =
                    htmlDocument(
                        runBlocking {
                            client
                                .get(
                                    "https://uspdigital.usp.br/janus/DisciplinaAux?tipo=T&sgldis=${sgldis}") {
                                        timeout { requestTimeoutMillis = timeoutMs.toLong() }
                                    }
                                .bodyAsText()
                        },
                    ) {
                        // example: publico/turma/SCC5832/2 <- offering number (classCode)
                        "td > a" { findSecond { attribute("href").split('/').last() } }
                    }

                val offering = DisciplineOffering(classCode = classCode)

                htmlDocument(
                    runBlocking {
                        client
                            .get(
                                "https://uspdigital.usp.br/janus/TurmaDet?sgldis=${sgldis}&ofe=${classCode}") {
                                    timeout { requestTimeoutMillis = timeoutMs.toLong() }
                                }
                            .bodyAsText()
                    },
                ) {
                    offering.startDate =
                        findFirst("tr:nth-of-type(12) font:nth-of-type(2)") { text }
                    offering.endDate = findFirst("tr:nth-of-type(12) font:nth-of-type(4)") { text }
                    offering.professors =
                        findAll(
                            "tr:nth-of-type(16) tr:not(:first-child)",
                        ) {
                            map { it.text }.toSet()
                        }
                }

                return setOf(offering)
            } catch (e: Exception) {
                // If there's no offering or if the selection fails for whatever reason
                return emptySet()
            }
        }
    }
}
