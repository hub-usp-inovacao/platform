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

        fun calculateSize(employees: String?, unicorn: String?, classification: CompanyClassification): Set<String> {
            val sizes = if (unicorn == "Unicórnio") setOf(unicorn) else emptySet()

            val employeesInt = if (employees == null) 0 else employees!!.toInt()

            if (employeesInt <= 0) {
                return sizes.plus("Não Informado")
            }

            if (classification.major == "Indústria de Transformação") {
                when (employeesInt) {
                    in 1..19 -> return sizes.plus("Microempresa")
                    in 20..99 -> return sizes.plus("Pequena Empresa")
                    in 100..499 -> return sizes.plus("Média Empresa")
                    else -> return sizes.plus("Grande Empresa")
                }
            } else {
                when (employeesInt) {
                    in 1..9 -> return sizes.plus("Microempresa")
                    in 10..49 -> return sizes.plus("Pequena Empresa")
                    in 50..99 -> return sizes.plus("Média Empresa")
                    else -> return sizes.plus("Grande Empresa")
                }
            }
        }

        fun formatIncubated(incubated: String?): String? {
            if (incubated == null) {
                return null
            }

            val regex = Regex("Sim.+")
            val matches = regex.find(incubated)

            return if (matches != null) "Sim" else "Não"
        }

        fun formatLogo(raw: String?): String? {
            if (raw == null || raw == "N/D") {
                return null
            }

            if (raw.length > 3 && raw.substring(0..3) == "http") return raw

            return "https://drive.google.com/uc?export=view&id=$raw"
        }

        fun formatPhones(raw: String?): Set<String>? {
            if (raw == null || raw == "N/D") {
                return null
            }

            val phones = raw.split(";")
            val formattedPhones = mutableSetOf<String>()

            for (phone in phones) {
                val numbers = phone.replace(Regex("\\D"), "")
                when (numbers.length) {
                    13 -> formattedPhones.add("+${numbers.substring(0..1)} (${numbers.substring(2..3)}) ${numbers.substring(4..8)} - ${numbers.substring(9)}")
                    12 -> formattedPhones.add("+${numbers.substring(0..1)} (${numbers.substring(2..3)}) ${numbers.substring(4..7)} - ${numbers.substring(8)}")
                    11 -> formattedPhones.add("(${numbers.substring(0..1)}) ${numbers.substring(2..6)} - ${numbers.substring(7)}")
                    10 -> formattedPhones.add("(${numbers.substring(0..1)}) ${numbers.substring(2..5)} - ${numbers.substring(6)}")
                    9 -> formattedPhones.add("${numbers.substring(0..4)} - ${numbers.substring(5)}")
                    8 -> formattedPhones.add("${numbers.substring(0..3)} - ${numbers.substring(4)}")
                    else -> formattedPhones.add(numbers)
                }
            }

            return formattedPhones
        }

        fun formatUrl(raw: String?): String? {
            if (raw == null || raw == "N/D") {
                return null
            }

            if (raw.length > 3 && raw.substring(0..3) != "http") return "https://$raw"

            return raw
        }

        fun splitAndTrim(raw: String?, separator: Char): Set<String>? {
            return raw?.split(separator)?.map { it.trim() }.toSet()
        }

        fun fromRow(row: List<String?>): Company {
            var classification = CompanyClassification.classify(row[5])

            return Company(
                active = true,
                address = CompanyAddress.fromRow(row.subList(8, 13)),
                allowed = true,
                classification = classification,
                cnae = row[5],
                cnpj = row[1],
                companySize = calculateSize(row[21], row[20], classification),
                corporateName = row[3],
                description = row[13],
                ecosystems = splitAndTrim(row[19], ';'),
                emails = splitAndTrim(row[7], ';'),
                incubated = formatIncubated(row[18]),
                logo = formatLogo(row[16]),
                name = row[2],
                phones = formatPhones(row[6]),
                services = splitAndTrim(row[14], ';'),
                technologies = splitAndTrim(row[15], ';'),
                partners = if (row.size < 62) emptySet() else createPartners(row.subList(33, 62)),
                url = formatUrl(row[17]),
                year = row[4]
            )
        }
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
