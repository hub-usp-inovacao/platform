package br.usp.inovacao.hubusp.server.cli.commands.curatorship

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.server.persistence.configureDB
import com.mongodb.client.MongoDatabase

/**
 * For now it acts as a singleton to store the db
 *
 * TODO: organize all the "refresh" subcommands into 1: refresh [SUBCOMMAND]
 */
class Refresh {
    companion object {
        val db: MongoDatabase by lazy {
            configureDB(
                Configuration.database.protocol,
                Configuration.database.host,
                Configuration.database.port,
                Configuration.database.dbName,
            )
        }
    }
}
