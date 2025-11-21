package br.usp.inovacao.hubusp.curatorship.sheets.utils

fun indexToColumnLetter(index: Int): String {
    var i = index
    var letter = ""
    while (i >= 0) {
        letter = ('A' + i % 26) + letter
        i = i / 26 - 1
    }
    return letter
}

fun formatUrl(raw: String?): String? {
    if (raw.isNullOrBlank() || raw == "N/D") {
        return null
    }

    if (raw.length > 3 && raw.substring(0..3) != "http") return "https://$raw"

    return raw
}

fun splitUnlessND(term: String?): Set<String>? {
    if (term == "N/D" || term == null) return emptySet()
    else return term?.split(";")?.map { it.trim() }?.toSet()
}

fun formatPhoto(raw: String?): String? {

    if (raw == null || raw == "N/D") {
        return null
    }

    if (raw.length > 3 && raw.substring(0..3) == "http") return raw

    return "https://drive.google.com/thumbnail?id=$raw"
}

fun handleND(term: String?) : String? {
    if(term == null) return "N/D"
    else return term
}

fun splitAndTrim(raw: String?, separator: Char): Set<String>? {
    return raw?.split(separator)?.map { it.trim() }?.toSet()
}