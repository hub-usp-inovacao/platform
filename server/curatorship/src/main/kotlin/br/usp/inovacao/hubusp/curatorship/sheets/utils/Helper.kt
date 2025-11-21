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