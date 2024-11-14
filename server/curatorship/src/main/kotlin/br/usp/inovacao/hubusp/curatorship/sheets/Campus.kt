package br.usp.inovacao.hubusp.curatorship.sheets

enum class Campus(
    private val printableName: String? = null
) {

    Bauru {
        override fun unities(): Set<String> = setOf(
            "Faculdade de Odontologia de Bauru - FOB",
            "Hospital de Reabilitação de Anomalias Craniofaciais - HRAC"
        )
    },

    Butanta("Butantã") {
        override fun unities(): Set<String> = setOf(
            "Escola de Comunicações e Artes - ECA",
            "Escola de Educação Física e Esporte - EEFE",
            "Escola Politécnica - EP",
            "Faculdade de Arquitetura e Urbanismo - FAU",
            "Faculdade de Ciências Farmacêuticas - FCF",
            "Faculdade de Economia, Administração e Contabilidade - FEA",
            "Faculdade de Educação - FE",
            "Faculdade de Filosofia, Letras e Ciências Humanas - FFLCH",
            "Faculdade de Medicina Veterinária e Zootecnia - FMVZ",
            "Faculdade de Odontologia - FO",
            "Hospital Universitário - HU",
            "Instituto de Astronomia, Geofísica e Ciências Atmosféricas - IAG",
            "Instituto de Biociências - IB",
            "Instituto de Ciências Biomédicas - ICB",
            "Instituto de Energia e Ambiente - IEE",
            "Instituto de Estudos Avançados - IEA",
            "Instituto de Estudos Brasileiros - IEB",
            "Instituto de Física - IF",
            "Instituto de Geociências - IGc",
            "Instituto de Matemática e Estatística - IME",
            "Instituto de Psicologia - IP",
            "Instituto de Química - IQ",
            "Instituto de Relações Internacionais - IRI",
            "Instituto Oceanográfico - IO",
            "Instituto de Pesquisas Energéticas e Nucleares - IPEN"
        )
    },

    SaoSebastiao("São Sebastião (Butantã)") {
        override fun unities(): Set<String> = setOf(
            "Centro de Biologia Marinha - CEBIMar"
        )
    },

    LargoSaoFrancisco("Largo São Francisco") {
        override fun unities(): Set<String> = setOf(
            "Faculdade de Direito - FD"
        )
    },

    Lorena {
        override fun unities(): Set<String> = setOf(
            "Escola de Engenharia de Lorena - EEL"
        )
    },

    Museus {
        override fun unities(): Set<String> = setOf(
            "Museu de Arqueologia e Etnologia - MAE",
            "Museu de Arte Contemporânea - MAC",
            "Museu de Zoologia - MZ",
            "Museu Paulista - MP"
        )
    },

    Piracicaba {
        override fun unities(): Set<String> = setOf(
            "Centro de Energia Nuclear na Agricultura - CENA",
            "Escola Superior de Agricultura \"Luiz de Queiroz\" - ESALQ"
        )
    },

    Pirassununga {
        override fun unities(): Set<String> = setOf(
            "Faculdade de Zootecnia e Engenharia de Alimentos - FZEA"
        )
    },

    QuadrilateroDaSaude("Quadrilátero da Saúde") {
        override fun unities(): Set<String> = setOf(
            "Faculdade de Saúde Pública - FSP",
            "Faculdade de Medicina - FM",
            "Escola de Enfermagem - EE",
            "Instituto de Medicina Tropical de São Paulo - IMT"
        )
    },

    RibeiraoPreto("Ribeirão Preto") {
        override fun unities(): Set<String> = setOf(
            "Escola de Educação Física e Esporte de Ribeirão Preto - EEFERP",
            "Escola de Enfermagem de Ribeirão Preto - EERP",
            "Faculdade de Ciências Farmacêuticas de Ribeirão Preto - FCFRP",
            "Faculdade de Direito de Ribeirão Preto - FDRP",
            "Faculdade de Economia, Administração e Contabilidade de Ribeirão Preto - FEARP",
            "Faculdade de Filosofia, Ciências e Letras de Ribeirão Preto - FFCLRP",
            "Faculdade de Medicina de Ribeirão Preto - FMRP",
            "Faculdade de Odontologia de Ribeirão Preto - FORP"
        )
    },

    SaoCarlos("São Carlos") {
        override fun unities(): Set<String> = setOf(
            "Escola de Engenharia de São Carlos - EESC",
            "Instituto de Arquitetura e Urbanismo de São Carlos - IAU",
            "Instituto de Ciências Matemáticas e de Computação - ICMC",
            "Instituto de Física de São Carlos - IFSC",
            "Instituto de Química de São Carlos - IQSC"
        )
    },

    USPLeste("USP Leste") {
        override fun unities(): Set<String> = setOf(
            "Escola de Artes, Ciências e Humanidades - EACH"
        )
    },

    OnLine("On-line") {
        override fun unities(): Set<String> = setOf(
            "Pró-Reitoria de Graduação"
        )
    };

    abstract fun unities(): Set<String>

    override fun toString() = printableName ?: name

    companion object {
        fun allUnities() = Campus
            .values()
            .fold(mutableSetOf<String>()) { acc, curr ->
                (acc + curr.unities()).toMutableSet()
            }

        fun all() = Campus
            .values()
            .map(Campus::toString)

        fun campiNames() = Campus
            .values()
            .map(Campus::toString)
            .plus("Toda a USP")
    }
}
