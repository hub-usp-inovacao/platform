package br.usp.inovacao.hubusp.server.app.modules

import br.usp.inovacao.hubusp.server.catalog.*
import io.ktor.http.Parameters


fun Parameters.toPDISearchParams() = PDISearchParams(
    categories = this["categories"]?.split(",")?.toSet() ?: emptySet(),
    campus = this["campus"],
    term = this["term"],
)

fun Parameters.toResearcherSearchParams() = ResearcherSearchParams(
    majorArea = this["areaMajors"]?.split(",")?.toSet() ?: emptySet(),
    minorArea = this["areaMinors"]?.split(",")?.toSet() ?: emptySet(),
    campus = this["campus"],
    unity = this["unity"],
    bond = this["bond"],
    term = this["term"]
)

fun Parameters.toCompanySearchParams() = CompanySearchParams(
    areaMajors = this["areaMajors"]?.split(',')?.toSet() ?: emptySet(),
    areaMinors = this["areaMinors"]?.split(',')?.toSet() ?: emptySet(),
    city = this["city"],
    ecosystem = this["ecosystem"],
    size = this["size"]
)

fun Parameters.toPatentSearchParams() = PatentSearchParams(
    majorAreas = this["areaMajors"]?.split(",")?.toSet() ?: emptySet(),
    minorAreas = this["areaMinors"]?.split(",")?.toSet() ?: emptySet(),
    status = this["status"]
)

fun Parameters.toInitiativeSearchParams() = InitiativeSearchParams(
    classifications = this["classifications"]?.split(",")?.toSet() ?: emptySet(),
    campus = this["campus"]
)
