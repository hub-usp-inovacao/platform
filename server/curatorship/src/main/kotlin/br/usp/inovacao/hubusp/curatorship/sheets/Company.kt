package br.usp.inovacao.hubusp.curatorship.sheets

import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.valiktor.Constraint
import org.valiktor.Validator
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.*
import org.valiktor.i18n.mapToMessage
import org.valiktor.validate
import java.time.LocalDate

@kotlinx.serialization.Serializable
data class Partner(
    val name: String?,
    val nusp: String?,
    val bond: String?,
    val unity: String?,
    val email: String?,
    val phone: String?,
) {
    companion object {
        fun fromRow(subRow: List<String?>) = Partner(
            name = subRow[0],
            nusp = subRow[1],
            bond = subRow[2],
            unity = subRow[3],
            email = if (subRow.size > 5) subRow[5] else "",
            phone = if (subRow.size > 6) subRow[6] else ""
        )
    }

    init {
        try {
            validate(this) {
                validate(Partner::name)
                    .isNotBlank()
            }
        } catch (cve: ConstraintViolationException) {
            val violations: List<String> = cve.constraintViolations
                .mapToMessage(baseName = "messages")
                .map { "${it.property}: ${it.message}" }

            throw ValidationException(messages = violations)

        }
    }
}

@kotlinx.serialization.Serializable
data class CompanyClassification(
    val major: String?,
    val minor: String?
) {
    companion object {
        fun classify(cnae: String?): CompanyClassification {
            val emptyClassification = CompanyClassification(
              major = "",
              minor = ""
            )

            if (cnae == "") {
                return emptyClassification
            }

            val regex = Regex("(\\d+)\\.+")
            val matches = regex.find(cnae ?: "")
            val code = matches?.groupValues?.get(1)

            if (code == null || code.length > 2) {
                return emptyClassification
            }

            val major_minor = CompanyClassificationTranslator.translate(code)

            return CompanyClassification(
              major = major_minor.get("major"),
              minor = major_minor.get("minor")
            )
        }
    }

    init {
        try {
            validate(this) {
                validate(CompanyClassification::major)
                    .isValidMajor()

                validate(CompanyClassification::minor)
                    .isValidMinor(major)

            }
        } catch (cve: ConstraintViolationException) {
            val violations: List<String> = cve.constraintViolations
                .mapToMessage(baseName = "messages")
                .map { "${it.property}: ${it.message}" }

            throw ValidationException(messages = violations)
        }
    }
}

@kotlinx.serialization.Serializable
data class CompanyAddress(
    val cep: String?,
    val city: String?,
    val neighborhood: String?,
    val state: String?,
    val venue: String?
) {
    companion object {
        fun fromRow(subRow: List<String?>) = CompanyAddress(
            cep = subRow[0],
            city = subRow[1],
            neighborhood = subRow[2],
            state = subRow[3],
            venue = subRow[4]
        )
    }

    init {
        try {
            validate(this) {
                validate(CompanyAddress::city)
                    .isNotBlank()
            }
        } catch (cve: ConstraintViolationException) {
            val violations: List<String> = cve.constraintViolations
                .mapToMessage(baseName = "messages")
                .map { "${it.property}: ${it.message}" }

            throw ValidationException(messages = violations)
        }
    }
}

@kotlinx.serialization.Serializable
data class Company(
    val active: Boolean?,
    val address: CompanyAddress,
    val allowed: Boolean?,
    val classification: CompanyClassification,
    val cnae: String?,
    val cnpj: String?,
    val companySize: Set<String>?,
    val corporateName: String?,
    val description: String?,
    val ecosystems: Set<String>?,
    val emails: Set<String>?,
    val incubated: String?,
    val logo: String?,
    val name: String?,
    val phones: Set<String>?,
    val services: Set<String>?,
    val technologies: Set<String>?,
    val partners: Set<Partner>?,
    val url: String?,
    val year: String?
) {
    companion object {
        fun createPartners(subRow: List<String?>): Set<Partner> {
            var partners = mutableSetOf<Partner>()
            val subRowIndexes = listOf(0..6, 10..13, 15..18, 20..23, 25..28)

            for (subRowIndex in subRowIndexes) {
                val partnerSubRow = subRow.slice(subRowIndex)
                val partner = Partner.fromRow(partnerSubRow)

                if (partnerSubRow.any { it != "" }) {
                    partners.add(partner)
                }
            }
            return partners
        }

        fun fromRow(row: List<String?>) = Company(
            active = true,
            address = CompanyAddress.fromRow(row.subList(8, 13)),
            allowed = true,
            classification = CompanyClassification.classify(row[5]),
            cnae = row[5],
            cnpj = row[1],
            companySize = setOf("Unicórnio", "Microempresa"), // fix-ze
            corporateName = row[3],
            description = row[13],
            ecosystems = row[19]?.split(";")?.toSet(),
            emails = row[7]?.split(";")?.toSet(),
            incubated = row[18], // fix-ze
            logo = row[16], // fix-ze
            name = row[2],
            phones = row[6]?.split(";")?.toSet(), // fix-ze
            services = row[14]?.split(";")?.toSet(),
            technologies = row[15]?.split(";")?.toSet(),
            partners = if (row.size < 62) emptySet() else createPartners(row.subList(33, 62)),
            url = row[17], // fix-ze
            year = row[4]
        )
    }

    init {
        try {
            validate(this) {
                validate(Company::cnpj)
                    .isValidCnpj()
                    .isNotNull()

                validate(Company::name)
                    .isNotNull()

                validate(Company::year)
                    .hasSize(4)
                    .isLessThanOrEqualTo(LocalDate.now().year.toString())
            }
        } catch (cve: ConstraintViolationException) {
            val violations: List<String> = cve.constraintViolations
                .mapToMessage(baseName = "messages")
                .map { "${it.property}: ${it.message}" }

            throw ValidationException(messages = violations)
        }
    }
}
