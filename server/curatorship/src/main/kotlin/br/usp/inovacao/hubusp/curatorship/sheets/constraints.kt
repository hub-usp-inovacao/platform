package br.usp.inovacao.hubusp.curatorship.sheets

import org.valiktor.Constraint
import org.valiktor.Validator

class PDICategory: Constraint

fun <E> Validator<E>.Property<String?>.isPDICategory() = this.validate(PDICategory()) {
    it == null || PDI.categories
        .contains(it)
}

class Phone : Constraint

fun <E> Validator<E>.Property<String?>.isPhone() = this.validate(Phone()) {
    (it == null) || it
        .replace("""\D""".toRegex(), "")
        .matches("""^\d{8,13}$""".toRegex())
}

class Campi : Constraint

fun <E> Validator<E>.Property<String?>.isCampus() = this.validate(Campi()) {
    (it == null) || Campus
        .all()
        .contains(it)
}

class Unity : Constraint

fun <E> Validator<E>.Property<String?>.isUnity() = this.validate(Unity()) {
    it == null || it in Campus.allUnities()
}

object CompanyRegister : Constraint

fun Validator<Company>.Property<String?>.isValidCnpj() = this.validate(CompanyRegister) {
    it == null || it.matches(Regex("^[0-9]{2}.[0-9]{3}.[0-9]{3}/[0-9]{4}-[0-9]{2}$")) || it.matches(Regex("^Exterior[0-9]*$"))
}

object CompanyClassificationRegister : Constraint

fun Validator<CompanyClassification>.Property<String?>.isValidMajor() = this.validate(CompanyClassificationRegister) {
    it == null || CompanyClassificationValues.allMajors().contains(it)
}

fun Validator<CompanyClassification>.Property<String?>.isValidMinor(major: String?) = this.validate(CompanyClassificationRegister) {
    it == null || CompanyClassificationValues.majorToMinor(major).contains(it)
}


object ResearcherKnowledgeAreasRegister : Constraint

fun Validator<KnowledgeAreas>.Property<String?>.isValidArea() = this.validate(ResearcherKnowledgeAreasRegister) {
    it == null || ResearcherAreaValues.allAreas().contains
}

fun Validator<KnowledgeAreas>.Property<String?>.isValidSubArea(area: String?) = this.validate(ResearcherKnowledgeAreasRegister) {
    it == null || ResearcherAreaValues.areaToSubArea(area).contains(it)
}