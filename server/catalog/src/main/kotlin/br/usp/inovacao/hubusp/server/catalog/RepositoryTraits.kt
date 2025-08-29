package br.usp.inovacao.hubusp.server.catalog

/**
 * Repository that can be collected
 *
 * Requires enough memory to store the whole repository
 *
 * @param RecordModel Represents a valid record in the repository
 * @param ValidationError Validation error returned for each invalid record
 */
interface CollectableRepository<RecordModel, ValidationError> {
    fun collect(): Pair<Set<RecordModel>, List<ValidationError>>
}

/** Repository that can be dropped/cleared */
interface ClearableRepository<RecordModel> {
    fun clear()
}

/** Repository that can append records */
interface AppendableRepository<RecordModel> {
    fun append(record: RecordModel) {
        this.appendAll(setOf(record))
    }

    fun appendAll(records: Set<RecordModel>)
}

/** Repository that can be completely overwritten */
interface OverwritableRepository<RecordModel> :
    ClearableRepository<RecordModel>, AppendableRepository<RecordModel> {
    /** Overwrite repository with set of records */
    fun overwriteAll(records: Set<RecordModel>) {
        this.clear()
        this.appendAll(records)
    }

    /** Overwrite repository with records from another repository */
    fun <ValidationError> sync(
        referenceRepository: CollectableRepository<RecordModel, ValidationError>
    ): List<ValidationError> {
        val (validRecords, errors) = referenceRepository.collect()
        this.overwriteAll(validRecords)
        return errors
    }
}

/** Repository that can be searched with filters */
interface SearchableRepository<RecordModel, Filter> {
    fun search(filter: Filter): Set<RecordModel>
}
