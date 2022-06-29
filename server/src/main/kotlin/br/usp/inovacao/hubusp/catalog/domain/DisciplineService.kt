package br.usp.inovacao.hubusp.catalog.domain

import com.mongodb.client.MongoCollection
import org.litote.kmongo.find

class DisciplineService(
    private val disciplineCollection: MongoCollection<Discipline>
) {
    fun search(params: Map<String, List<String>>): Set<Discipline> {
        val filter = DisciplineQueryFilter.from(params)
            .toJson()

        return disciplineCollection
            .find(filter)
            .toSet()
    }
}
