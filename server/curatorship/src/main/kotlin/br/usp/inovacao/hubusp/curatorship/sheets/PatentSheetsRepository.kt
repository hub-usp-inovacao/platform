package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.server.catalog.Area
import br.usp.inovacao.hubusp.server.catalog.CollectableRepository
import br.usp.inovacao.hubusp.server.catalog.DualClassification
import br.usp.inovacao.hubusp.server.catalog.Patent
import kotlin.Result

fun splitAndTrim(raw: String?, separator: Char): Set<String>? {
    return raw?.split(separator)?.map { it.trim() }?.toSet()
}

fun Area.Companion.fromRow(row: List<String?>) =
    try {
        Result.success(
            Area(
                cip = row[0]!!,
                subarea = row[1]!!,
            ))
    } catch (e: Exception) {
        Result.failure(e)
    }

fun DualClassification.Companion.fromRow(row: List<String?>) =
    try {
        Result.success(
            DualClassification(
                primary = Area.fromRow(row).getOrThrow(),
                secondary = Area.fromRow(row).getOrNull()))
    } catch (e: Exception) {
        Result.failure(e)
    }

// 0  Cip Principal (Classificação Internacional de Patente)
// 1  Sub-Areas
// 2  CIP secundario
// 3  sub-area secundaria
// 4  Número da Patente
// 5  Title
// 6  CIP (ipcs?)
// 7  Application Year
// 8  Depositante (owners)
// 9  Inventor
// 10 Abstract
// 11 Países de Depósito
// 12 STATUS concedia OU em analise
// 13 Hiperlink
// 14 Link extraido
// 15 ID flyer
fun Patent.Companion.fromRow(row: List<String?>) =
    try {
        Result.success(
            Patent(
                classification = DualClassification.fromRow(row).getOrThrow(),
                countries_with_protection = row[11]!!.split("; ?".toRegex()).toSet(),
                inventors = row[9]!!.split(" ?\\| ?".toRegex()).toSet(),
                ipcs = row[6]!!.split(" ?\\| ?".toRegex()).toSet(),
                name = row[5]!!,
                owners = row[8]!!.split(" ?\\| ?".toRegex()).toSet(),
                photo =
                    when (val id = row[15]) {
                        "N/D" -> null
                        else -> "https://drive.google.com/uc?export=view&id=$id"
                    },
                status = row[12]!!,
                summary = row[10]!!,
                url =
                    when (val url = row[14]) {
                        "N/D" -> null
                        else -> url
                    }))
    } catch (e: Exception) {
        Result.failure(e)
    }

sealed class PatentSheetsError {
    class ValidationError(val messages: List<String>, val spreadsheetLineNumber: Int)

    class UniquenessError(val messages: List<String>, val spreadsheetLineNumber: Int)

    class UnknownError(val messages: List<String>, val spreadsheetLineNumber: Int)
}

class PatentSheetsRepository(val spreadsheetReader: SpreadsheetReader) :
    CollectableRepository<Patent, PatentSheetsError> {
    companion object {
        /**
         * Index of the first row that contains data.
         *
         * Google Sheets' rows are 1-based indexed and we skip the first row, since it contains the
         * labels, so we end up with 2.
         */
        private const val INDEX_CORRECTION_FACTOR = 2
    }

    override fun collect(): Pair<Set<Patent>, List<PatentSheetsError>> {
        val validPatents = mutableSetOf<Patent>()
        val errors = mutableListOf<PatentSheetsError>()

        this.spreadsheetReader.read(Sheets.Patents).drop(1).forEach { row ->
            Patent.fromRow(row)
                .onSuccess { v -> validPatents += v }
                .onFailure { e ->
                    when (e) {
                        is ValidationException -> {
                            errors +=
                                PatentSheetsError.ValidationError(
                                    message = e.message, spreadsheetLineNumber = 3)
                        }
                        else -> {
                            errors +=
                                PatentSheetsError.ValidationError(
                                    message = e.message, spreadsheetLineNumber = 3)
                        }
                    }
                }
        }

        return validPatents.toSet() to errors
    }
}
