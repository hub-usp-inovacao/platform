package br.usp.inovacao.hubusp.curatorship.sheets

import it.skrape.core.*
import it.skrape.fetcher.BlockingFetcher
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.Request
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.DocElement
import it.skrape.selects.html5.*
import org.valiktor.functions.*

@kotlinx.serialization.Serializable
data class DisciplineOffering(
    val classCode: String?,
    val startDate: String?,
    val endDate: String?,
    // TODO: Professors
) {
    companion object {
        fun tryFrom(tableElement: DocElement): DisciplineOffering? {
            val regexPattern =
                "Código da Turma: ([^ ]*) *Início: ([^ ]*) *Fim: ([^ ]*) *Tipo da Turma: (.*)"

            val groupValues = Regex(regexPattern).find(tableElement.text)?.groupValues

            return if (groupValues != null) {
                DisciplineOffering(
                    classCode = groupValues.getOrNull(1),
                    startDate = groupValues.getOrNull(2),
                    endDate = groupValues.getOrNull(3),
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
                            "tr > td > b > font > span" {
                                findAll {
                                    forEach {
                                        if (it.text.contains("Código da Turma")) {
                                            val tableElement = it.parent.parent.parent.parent.parent
                                            DisciplineOffering.tryFrom(tableElement)?.let {
                                                offerings.add(it)
                                            }
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
