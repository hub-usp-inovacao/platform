require 'rails_helper'

RSpec.describe AboutCompany, type: :model do
  let :attrs do
    {
      description: 'long text about the company',
      services: %w[foo bar baz],
      technologies: %w[baz bla foo],
      site: 'https://www.google.com',
      odss: [
        "1 - Erradicação da Pobreza",
        "2 - Fome Zero",
        "3 - Saúde e Bem Estar",
        "4 - Educação de Qualidade",
        "5 - Igualdade de Gênero",
        "6 - Água Potável e Saneamento",
        "7 - Energia Limpa e Acessível",
        "8 - Trabalho Decente e Crescimento Econômico",
        "9 - Indústria, Inovação e Infraestrutura",
        "10 - Redução das Desigualdades",
        "11 - Cidades e Comunidades Sustentáveis",
        "12 - Consumo e Produção Responsáveis",
        "13 - Ação Contra a Mudança Global do Clima",
        "14 - Vida na Água",
        "15 - Vida Terrestre",
        "16 - Paz, Justiça e Instituições Eficazes",
        "17 - Parcerias e Meios de Implementação",
      ],
      social_medias: ['https://instagram.com/foo-bar']
    }
  end

  context 'with validation errors' do
    it 'on unknown ODS' do
      attrs[:odss] = ['foo']
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on malformed url' do
      attrs[:site] = 'foobar'
      expect(described_class.new(attrs)).to be_invalid
    end
  end

  context 'with CSV preparation' do
    let :handmade do
      [nil] * 13 + [
        attrs[:description],
        attrs[:services].join(';'),
        attrs[:technologies].join(';'),
        nil,
        attrs[:site]
      ] + [nil] * 4 + [
        attrs[:social_medias].join(';')
      ] + [nil] * 65 + [
        attrs[:odss].join(';')
      ]
    end

    it 'prepares CSV correctly' do
      about = AboutCompany.new(attrs)
      expect(about.prepare_to_csv).to match_array(handmade)
    end
  end
end
