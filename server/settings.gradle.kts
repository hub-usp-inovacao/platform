rootProject.name = "br.usp.inovacao.hubusp.server"

plugins {
    id("org.jetbrains.kotlinx.kover.aggregation") version "0.9.1"
}

include(":catalog")
include(":config")
include(":curatorship")
include(":discovery")
include(":hub-app")
include(":hub-cli")
include(":mailer")
include(":persistence")
include(":sheets")
include(":techtransfer")

kover{
    enableCoverage()

    reports{
        includedProjects.addAll(
            ":catalog",
            ":config",
            ":curatorship",
            ":discovery",
            ":hub-app",
            ":hub-cli",
            ":mailer",
            ":persistence",
            ":sheets",
            ":techtransfer"
        )

        verify {
            rule {
                name = "Minimal line coverage rate in percentage"
                bound {
                    minValue = 0
                }
            }
        }
    }
}