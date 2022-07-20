package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.PDI
import br.usp.inovacao.hubusp.server.catalog.PDIRepository
import br.usp.inovacao.hubusp.server.catalog.PDISearchParams

class CatalogPDIRepositoryImpl : PDIRepository {
    override fun filter(params: PDISearchParams): Set<PDI> {
        val data = setOf(
            PDI(
                category = "Empresa Jr",
                name = "IME Jr",
                campus = "Butantã",
                unity = "IME",
                coordinator = "Prof. Alfredo Goldman",
                site = "https://www.ime.usp.br/~imejr",
                email = "imejr@ime.usp.br",
                phone = "(11) 3081-6775",
                description = "IME Jr bla bla bal",
                tags = setOf("foo", "bar", "baz")
            ),
            PDI(
                category = "Entidade Estudantil",
                name = "IME Jr",
                campus = "São Carlos",
                unity = "IME",
                coordinator = "Prof. Alfredo Goldman",
                site = "https://www.ime.usp.br/~imejr",
                email = "imejr@ime.usp.br",
                phone = "(11) 3081-6775",
                description = "IME Jr bla bla bal",
                tags = setOf("foo", "bar", "baz")
            )
        )

        val filtered = mutableSetOf<PDI>()

        if (params.categories.isNotEmpty()) {
            data.forEach { pdi ->
                if (pdi.category in params.categories) {
                    filtered.add(pdi)
                }
            }
        } else {
            data.forEach { filtered.add(it) }
        }

        if (params.campus != null) {
            filtered.removeIf { pdi ->
                pdi.campus != params.campus
            }
        }

        return filtered
    }
}