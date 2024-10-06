package br.usp.inovacao.hubusp.curatorship.sheets

enum class ResearcherAreaValues(
    private val printableName: String? = null
) {

    CienciasAgrarias("Ciências Agrárias") {
        override fun subAreas() = setOf(
            "Agronomia",
            "Recursos Florestais e Engenharia Florestal",
            "Engenharia Agrícola",
            "Zootecnia",
            "Medicina Veterinária",
            "Recursos Pesqueiros e Engenharia de Pesca",
            "Ciência e Tecnologia de Alimentos"
        )
    },

    CienciasBiologicas("Ciências Biológicas") {
        override fun subAreas() = setOf(
            "Biologia Geral",
            "Genética",
            "Botânica",
            "Zoologia",
            "Ecologia",
            "Morfologia",
            "Fisiologia",
            "Bioquímica",
            "Biofísica",
            "Farmacologia",
            "Imunologia",
            "Microbiologia",
            "Parasitologia"
        )
    },

    CienciasDaSaude("Ciências da Saúde") {
        override fun subAreas() = setOf(
            "Medicina",
            "Odontologia",
            "Farmácia",
            "Fonoaudiologia",
            "Fisioterapia e Terapia Ocupacional",
            "Educação Física"
        )
    },

    CienciasExatasEDaTerra("Ciências Exatas e da Terra") {
        override fun subAreas() = setOf(
            "Matemática",
            "Probabilidade e Estatística",
            "Ciência da Computação",
            "Astronomia",
            "Física",
            "Química",
            "GeoCiências",
            "Oceanografia"
        )
    },

    Engenharias("Engenharias") {
        override fun subAreas() = setOf(
            "Engenharia Civil",
            "Engenharia de Minas",
            "Engenharia de Materiais e Metalúrgica",
            "Engenharia Elétrica",
            "Engenharia Mecânica",
            "Engenharia Química",
            "Engenharia Sanitária",
            "Engenharia Produção",
            "Engenharia Nuclear",
            "Engenharia de Transportes",
            "Engenharia Naval e Oceânica",
            "Engenharia Aeroespacial",
            "Engenharia Biomédica"
        )
    },

    CienciasHumanas("Ciências Humanas") {
        override fun subAreas() = setOf(
            "Filosofia",
            "Sociologia",
            "Antropologia",
            "Arqueologia",
            "História",
            "Geografia",
            "Psicologia",
            "Educação",
            "Ciência Política",
            "Teologia"
        )
    },

    CienciasSociaisAplicadas("Ciências Sociais Aplicadas") {
        override fun subAreas() = setOf(
              "Direito",
              "Administração",
              "Economia",
              "Arquitetura e Urbanismo",
              "Planejamento Urbano e Regional",
              "Demografia",
              "Ciência da Informação",
              "Museologia",
              "Comunicação",
              "Serviço Social",
              "Economia Doméstica",
              "Desenho Industrial",
              "Turismo"
        )
    },

    LinguisticaLetrasEArtes("Linguística, Letras e Artes") {
        override fun subAreas() = setOf(
              "Linguística",
              "Letras",
              "Artes"
        )
    },

    DEFAULT("") {
        override fun subAreas() = setOf("")
    };

    abstract fun subAreas(): Set<String>

    override fun toString() = printableName ?: name

    companion object {
        fun areaToSubArea(area: String?) = values().find { it.printableName == area }?.subAreas() ?: emptySet()
        fun allAreas() = values().map { it.printableName }.filterNotNull()
    }
}
