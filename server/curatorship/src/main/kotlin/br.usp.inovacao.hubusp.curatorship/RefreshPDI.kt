package br.usp.inovacao.hubusp.curatorship

class RefreshPDI(
    val pdiSpreadsheetReader: PDISpreadsheetReader,
    val pdiRepository: PDIRepository,
    val validationErrorRepository: ValidationErrorRepository
    ) {

    fun play() {
        // fetching data from the spreadsheet
        // validate data from the spreadsheet
        // store valid data as PDI in MongoDB
        // store invalid data as validation errors in MongoDB
        fetchData()
            .map { validate(it) } // List<Pair<PDI?, ValidationError?>>
    }

    fun fetchData() = pdiSpreadsheetReader.fetch()

    fun validate(rawPDI: List<String>) = try {
        val pdi = PDI.from(
            category = rawPDI[1],
            name = rawPDI[0]
        )
        Pair(pdi, null)
    } catch (e: ValidationException) {
        Pair(null, ValidationError(e.message!!))
    }
}