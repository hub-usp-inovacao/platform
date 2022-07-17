package br.usp.inovacao.hubusp.server.app.modules

import br.usp.inovacao.hubusp.server.catalog.CompanySearchParams
import br.usp.inovacao.hubusp.server.catalog.PatentSearchParams
import br.usp.inovacao.hubusp.server.catalog.ResearcherSearchParams
import io.ktor.http.Parameters

fun Parameters.toResearcherSearchParams() = ResearcherSearchParams(
    majorArea = this["areaMajors"]?.split(",")?.toSet() ?: emptySet(),
    minorArea = this["areaMinors"]?.split(",")?.toSet() ?: emptySet(),
    campus = this["campus"],
    unity = this["unity"],
    bond = this["bond"]
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
