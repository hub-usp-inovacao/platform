package br.usp.inovacao.hubusp.server.cli.commands

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.server.persistence.TextIndexMigrationStatus
import br.usp.inovacao.hubusp.server.persistence.connectToDB
import br.usp.inovacao.hubusp.server.persistence.migrateTextIndexes
import com.github.ajalt.clikt.core.CliktCommand

class MigrateTextIndexes : CliktCommand(
    name = "migrate-text-indexes",
    help = "Recria os índices de texto em português. Execute uma vez durante uma janela de manutenção.",
) {
    override fun run() {
        val database = connectToDB(
            protocol = Configuration.database.protocol,
            host = Configuration.database.host,
            port = Configuration.database.port,
            dbName = Configuration.database.dbName,
        )

        migrateTextIndexes(database).forEach { result ->
            echo("${result.collectionName}: ${result.status.description}")
        }
    }
}

private val TextIndexMigrationStatus.description: String
    get() = when (this) {
        TextIndexMigrationStatus.CREATED -> "índice criado"
        TextIndexMigrationStatus.REBUILT -> "índice recriado"
        TextIndexMigrationStatus.UNCHANGED -> "índice já atualizado"
    }
