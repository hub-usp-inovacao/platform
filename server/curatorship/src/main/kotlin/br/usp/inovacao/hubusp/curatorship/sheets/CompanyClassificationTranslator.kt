package br.usp.inovacao.hubusp.curatorship.sheets

class CompanyClassificationTranslator {

    private val classificationMap: Map<String, Map<String, String>>

    init {
        classificationMap = mapOf(
            "01" to mapOf(
                "major" to "Agricultura, Pecuária, Pesca e Extrativismo",
                "minor" to "Agricultura, Pecuária, Produção Florestal, Pesca e Aquicultura"
            ),
            "02" to mapOf(
                "major" to "Agricultura, Pecuária, Pesca e Extrativismo",
                "minor" to "Agricultura, Pecuária, Produção Florestal, Pesca e Aquicultura"
            ),
            "03" to mapOf(
                "major" to "Agricultura, Pecuária, Pesca e Extrativismo",
                "minor" to "Agricultura, Pecuária, Produção Florestal, Pesca e Aquicultura"
            ),
            "04" to mapOf(
                "major" to "Agricultura, Pecuária, Pesca e Extrativismo",
                "minor" to "Agricultura, Pecuária, Produção Florestal, Pesca e Aquicultura"
            ),
            "05" to mapOf(
                "major" to "Agricultura, Pecuária, Pesca e Extrativismo",
                "minor" to "Indústrias Extrativas"
            ),
            "06" to mapOf(
                "major" to "Agricultura, Pecuária, Pesca e Extrativismo",
                "minor" to "Extração de Petróleo e Gás Natural"
            ),
            "07" to mapOf(
                "major" to "Agricultura, Pecuária, Pesca e Extrativismo",
                "minor" to "Indústrias Extrativas"
            ),
            "08" to mapOf(
                "major" to "Agricultura, Pecuária, Pesca e Extrativismo",
                "minor" to "Indústrias Extrativas"
            ),
            "09" to mapOf(
                "major" to "Agricultura, Pecuária, Pesca e Extrativismo",
                "minor" to "Indústrias Extrativas"
            ),
            "10" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Alimentos"
            ),
            "11" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Bebidas"
            ),
            "12" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Produtos do Fumo"
            ),
            "13" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Produtos Têxteis"
            ),
            "14" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Vestuário e Acessórios"
            ),
            "15" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Artefatos de Couro, Artigos para Viagem e Calçados"
            ),
            "16" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Produtos de Madeira"
            ),
            "17" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Celulose e Papel"
            ),
            "18" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Impressão e Reprodução de Gravações"
            ),
            "19" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Coque, Derivados de Petróleo e de Biocombustíveis"
            ),
            "20" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Produtos Químicos"
            ),
            "21" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Produtos Farmaquímicos e Farmacêuticos"
            ),
            "22" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Produtos de Borracha e de Material Plástico"
            ),
            "23" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Produtos de Minerais Não-Metálicos"
            ),
            "24" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Metalurgia"
            ),
            "25" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Produtos de Metal, exceto Máquinas e Equipamentos"
            ),
            "26" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Equipamentos de Informática, Produtos Eletrônicos e Ópticos"
            ),
            "27" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Máquinas, Aparelhos e Materiais Elétricos"
            ),
            "28" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Máquinas e Equipamentos"
            ),
            "29" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Automóveis, Reboques e Carrocerias"
            ),
            "30" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Equipamentos de Transporte, exceto Veículos Automotores"
            ),
            "31" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Móveis"
            ),
            "32" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Produtos Diversos"
            ),
            "33" to mapOf(
                "major" to "Indústria de Transformação",
                "minor" to "Manutenção, Reparação e Instalação de Máquinas e Equipamentos"
            ),
            "35" to mapOf(
                "major" to "Infraestrutura e Construção",
                "minor" to "Eletricidade e Gás"
            ),
            "36" to mapOf(
                "major" to "Infraestrutura e Construção",
                "minor" to "Água, Esgoto, Atividades de Gestão de Resíduos e Descontaminação"
            ),
            "37" to mapOf(
                "major" to "Infraestrutura e Construção",
                "minor" to "Água, Esgoto, Atividades de Gestão de Resíduos e Descontaminação"
            ),
            "38" to mapOf(
                "major" to "Infraestrutura e Construção",
                "minor" to "Água, Esgoto, Atividades de Gestão de Resíduos e Descontaminação"
            ),
            "39" to mapOf(
                "major" to "Infraestrutura e Construção",
                "minor" to "Água, Esgoto, Atividades de Gestão de Resíduos e Descontaminação"
            ),
            "40" to mapOf(
                "major" to "Infraestrutura e Construção",
                "minor" to "Água, Esgoto, Atividades de Gestão de Resíduos e Descontaminação"
            ),
            "41" to mapOf(
                "major" to "Infraestrutura e Construção",
                "minor" to "Construção"
            ),
            "42" to mapOf(
                "major" to "Infraestrutura e Construção",
                "minor" to "Construção"
            ),
            "43" to mapOf(
                "major" to "Infraestrutura e Construção",
                "minor" to "Construção"
            ),
            "45" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Comércio e Reparação de Veículos Automotores e Motocicletas"
            ),
            "46" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Comércio por Atacado, exceto Veículos Automotores e Motocicletas"
            ),
            "47" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Comércio Varejista"
            ),
            "48" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Comércio Varejista"
            ),
            "49" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Comércio Varejista"
            ),
            "50" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Comércio Varejista"
            ),
            "51" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Comércio Varejista"
            ),
            "52" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Comércio Varejista"
            ),
            "53" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Comércio Varejista"
            ),
            "54" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Comércio Varejista"
            ),
            "55" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Alojamento e Alimentação"
            ),
            "56" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Alojamento e Alimentação"
            ),
            "57" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Alojamento e Alimentação"
            ),
            "58" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Informação e Comunicação"
            ),
            "59" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Informação e Comunicação"
            ),
            "60" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Informação e Comunicação"
            ),
            "61" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Informação e Comunicação"
            ),
            "62" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Informação e Comunicação"
            ),
            "63" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Informação e Comunicação"
            ),
            "64" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Atividades Financeiras, de Seguros e Serviços Relacionados"
            ),
            "65" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Atividades Financeiras, de Seguros e Serviços Relacionados"
            ),
            "66" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Atividades Financeiras, de Seguros e Serviços Relacionados"
            ),
            "67" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Atividades Financeiras, de Seguros e Serviços Relacionados"
            ),
            "68" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Atividades Imobiliárias"
            ),
            "94" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Outras Atividades de Serviços"
            ),
            "95" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Outras Atividades de Serviços"
            ),
            "96" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Outras Atividades de Serviços"
            ),
            "97" to mapOf(
                "major" to "Comércio e Serviços",
                "minor" to "Serviços Domésticos"
            ),
            "69" to mapOf(
                "major" to "Atividades Profissionais, Científicas e Técnicas",
                "minor" to "Atividades Profissionais, Científicas e Técnicas"
            ),
            "70" to mapOf(
                "major" to "Atividades Profissionais, Científicas e Técnicas",
                "minor" to "Atividades Profissionais, Científicas e Técnicas"
            ),
            "71" to mapOf(
                "major" to "Atividades Profissionais, Científicas e Técnicas",
                "minor" to "Atividades Profissionais, Científicas e Técnicas"
            ),
            "72" to mapOf(
                "major" to "Atividades Profissionais, Científicas e Técnicas",
                "minor" to "Atividades Profissionais, Científicas e Técnicas"
            ),
            "73" to mapOf(
                "major" to "Atividades Profissionais, Científicas e Técnicas",
                "minor" to "Atividades Profissionais, Científicas e Técnicas"
            ),
            "74" to mapOf(
                "major" to "Atividades Profissionais, Científicas e Técnicas",
                "minor" to "Atividades Profissionais, Científicas e Técnicas"
            ),
            "75" to mapOf(
                "major" to "Atividades Profissionais, Científicas e Técnicas",
                "minor" to "Atividades Profissionais, Científicas e Técnicas"
            ),
            "76" to mapOf(
                "major" to "Atividades Profissionais, Científicas e Técnicas",
                "minor" to "Atividades Profissionais, Científicas e Técnicas"
            ),
            "77" to mapOf(
                "major" to "Atividades Profissionais, Científicas e Técnicas",
                "minor" to "Atividades Administrativas e Serviços Complementares"
            ),
            "78" to mapOf(
                "major" to "Atividades Profissionais, Científicas e Técnicas",
                "minor" to "Atividades Administrativas e Serviços Complementares"
            ),
            "79" to mapOf(
                "major" to "Atividades Profissionais, Científicas e Técnicas",
                "minor" to "Atividades Administrativas e Serviços Complementares"
            ),
            "80" to mapOf(
                "major" to "Atividades Profissionais, Científicas e Técnicas",
                "minor" to "Atividades Administrativas e Serviços Complementares"
            ),
            "81" to mapOf(
                "major" to "Atividades Profissionais, Científicas e Técnicas",
                "minor" to "Atividades Administrativas e Serviços Complementares"
            ),
            "82" to mapOf(
                "major" to "Atividades Profissionais, Científicas e Técnicas",
                "minor" to "Atividades Administrativas e Serviços Complementares"
            ),
            "83" to mapOf(
                "major" to "Atividades Profissionais, Científicas e Técnicas",
                "minor" to "Atividades Administrativas e Serviços Complementares"
            ),
            "84" to mapOf(
                "major" to "Atividades Profissionais, Científicas e Técnicas",
                "minor" to "Administração Pública, Defesa e Seguridade Social"
            ),
            "99" to mapOf(
                "major" to "Atividades Profissionais, Científicas e Técnicas",
                "minor" to "Organismos Internacionais e outras Instituições Extraterritoriais"
            ),
            "85" to mapOf(
                "major" to "Educação, Artes e Esportes",
                "minor" to "Educação"
            ),
            "90" to mapOf(
                "major" to "Educação, Artes e Esportes",
                "minor" to "Artes, Cultura, Esporte e Recreação"
            ),
            "91" to mapOf(
                "major" to "Educação, Artes e Esportes",
                "minor" to "Artes, Cultura, Esporte e Recreação"
            ),
            "92" to mapOf(
                "major" to "Educação, Artes e Esportes",
                "minor" to "Artes, Cultura, Esporte e Recreação"
            ),
            "93" to mapOf(
                "major" to "Educação, Artes e Esportes",
                "minor" to "Artes, Cultura, Esporte e Recreação"
            ),
            "86" to mapOf(
                "major" to "Saúde e Serviços Sociais",
                "minor" to "Saúde Humana e Serviços Sociais"
            ),
            "87" to mapOf(
                "major" to "Saúde e Serviços Sociais",
                "minor" to "Saúde Humana e Serviços Sociais"
            ),
            "88" to mapOf(
                "major" to "Saúde e Serviços Sociais",
                "minor" to "Saúde Humana e Serviços Sociais"
            )
        )
    }

    fun translate(code: String): Map<String, String> {
        val major = classificationMap[code]?.get("major") ?: ""
        val minor = classificationMap[code]?.get("minor") ?: ""
        return mapOf("major" to major, "minor" to minor)
    }

    companion object {
        fun translate(code: String): Map<String, String> {
            return CompanyClassificationTranslator().translate(code)
        }
    }
}
