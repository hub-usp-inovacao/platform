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

object ResearcherRegister: Constraint

fun Validator<Researcher>.Property<String?>.isValidBond() = this.validate(ResearcherRegister) {
    it == null || Researcher.bonds.contains(it)
}

fun Validator<Researcher>.Property<Iterable<String>?>.isValidUnities() = this.validate(ResearcherRegister) {
    it == null || it.all { unity -> Campus.allUnities().contains(unity) }
}

object ResearcherKnowledgeAreasRegister : Constraint

fun Validator<KnowledgeAreas>.Property<Iterable<String>?>.isValidArea() = this.validate(ResearcherKnowledgeAreasRegister) {
    it == null || it.all { area -> ResearcherAreaValues.allAreas().contains(area) }
}

fun Validator<KnowledgeAreas>.Property<Iterable<String>?>.isValidSubArea(area: Set<String>?) =
    this.validate(ResearcherKnowledgeAreasRegister) {
        it == null || it.all { subArea ->
            area?.any { mainArea -> ResearcherAreaValues.areaToSubArea(mainArea).contains(subArea) } ?: true
        }
    }

class Date : Constraint

fun <E> Validator<E>.Property<String?>.isDate() = this.validate(Date()) {
    it == null || it.matches("""^\d{2}/\d{2}/\d{4}$""".toRegex()) || it.matches("""^N/D$""".toRegex())
}

object DisciplineRegister : Constraint

fun Validator<Discipline>.Property<String?>.isValidName() = this.validate(DisciplineRegister) {
    it == null || it.matches(Regex("\\A(\\w|\\d){2,3}\\d{4} (-|–) .+\\z"))
}

fun Validator<Discipline>.Property<String?>.isValidNature() = this.validate(DisciplineRegister) {
    it == null || Discipline.natures.contains(it)
}

fun Validator<Discipline>.Property<String?>.isValidLevel() = this.validate(DisciplineRegister) {
    it == null || Discipline.levels.contains(it)
}

fun Validator<Discipline>.Property<DisciplineCategory?>.isValidCategory() = this.validate(DisciplineRegister) {
    it == null || it.business == true || it.entrepreneurship == true || it.innovation == true || it.intellectual_property == true
}