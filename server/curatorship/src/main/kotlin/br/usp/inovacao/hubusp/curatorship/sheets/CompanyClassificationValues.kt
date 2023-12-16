package br.usp.inovacao.hubusp.curatorship.sheets

enum class CompanyClassificationValues(
    private val printableName: String? = null
) {

    IndustriaDeTransformacao("Indústria de Transformação") {
        override fun minors() = setOf(
            "Alimentos",
            "Bebidas",
            "Produtos do Fumo",
            "Produtos Têxteis",
            "Vestuário e Acessórios",
            "Artefatos de Couro, Artigos para Viagem e Calçados",
            "Produtos de Madeira",
            "Celulose e Papel",
            "Impressão e Reprodução de Gravações",
            "Coque, Derivados de Petróleo e de Biocombustíveis",
            "Produtos Químicos",
            "Produtos Farmaquímicos e Farmacêuticos",
            "Produtos de Borracha e de Material Plástico",
            "Produtos de Minerais Não-Metálicos",
            "Metalurgia",
            "Produtos de Metal, exceto Máquinas e Equipamentos",
            "Equipamentos de Informática, Produtos Eletrônicos e Ópticos",
            "Máquinas, Aparelhos e Materiais Elétricos",
            "Máquinas e Equipamentos",
            "Automóveis, Reboques e Carrocerias",
            "Equipamentos de Transporte, exceto Veículos Automotores",
            "Móveis",
            "Produtos Diversos",
            "Manutenção, Reparação e Instalação de Máquinas e Equipamentos"
        )
    },

    InfraestruturaEConstrucao("Infraestrutura e Construção") {
        override fun minors() = setOf(
            "Eletricidade e Gás",
            "Água, Esgoto, Atividades de Gestão de Resíduos e Descontaminação",
            "Construção"
        )
    },

    ComercioEServicos("Comércio e Serviços") {
        override fun minors() = setOf(
            "Comércio e Reparação de Veículos Automotores e Motocicletas",
            "Comércio por Atacado, exceto Veículos Automotores e Motocicletas",
            "Comércio Varejista",
            "Alojamento e Alimentação",
            "Informação e Comunicação",
            "Atividades Financeiras, de Seguros e Serviços Relacionados",
            "Atividades Imobiliárias",
            "Outras Atividades de Serviços",
            "Serviços Domésticos"
        )
    },

    AtividadesProfissionaisCientificasETecnicas("Atividades Profissionais, Científicas e Técnicas") {
        override fun minors() = setOf(
            "Atividades Profissionais, Científicas e Técnicas",
            "Atividades Administrativas e Serviços Complementares",
            "Administração Pública, Defesa e Seguridade Social",
            "Organismos Internacionais e outras Instituições Extraterritoriais"
        )
    },

    EducacaoArtesEEsportes("Educação, Artes e Esportes") {
        override fun minors() = setOf(
            "Educação",
            "Artes, Cultura, Esporte e Recreação"
        )
    },

    SaudeEServicosSociais("Saúde e Serviços Sociais") {
        override fun minors() = setOf(
            "Saúde Humana e Serviços Sociais"
        )
    },

    AgriculturaPecuariaPescaEEextrativismo("Agricultura, Pecuária, Pesca e Extrativismo") {
        override fun minors() = setOf(
              "Agricultura, Pecuária, Produção Florestal, Pesca e Aquicultura",
              "Indústrias Extrativas",
              "Extração de Petróleo e Gás Natural"
        )
    },

    DEFAULT("") {
        override fun minors() = setOf("")
    };

    abstract fun minors(): Set<String>

    override fun toString() = printableName ?: name

    companion object {
        fun majorToMinor(major: String?) = values().find { it.printableName == major }?.minors() ?: emptySet()
        fun allMajors() = values().map { it.printableName }.filterNotNull()
    }
}
