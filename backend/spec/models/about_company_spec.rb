# frozen_string_literal: true

require 'rails_helper'

RSpec.describe AboutCompany, type: :model do
  let :attrs do
    {
      description: 'long text about the company',
      services: %w[foo bar baz],
      technologies: %w[baz bla foo],
      site: 'https://www.google.com',
      odss: [
        '4 - Educação de Qualidade',
        '5 - Igualdade de Gênero',
        '8 - Trabalho Decente e Crescimento Econômico',
        '10 - Redução das Desigualdades',
        '15 - Vida Terrestre',
        '17 - Parcerias e Meios de Implementação'
      ],
      social_medias: ['https://instagram.com/foo-bar']
    }
  end

  context 'without validation errors' do
    it 'the default attrs are valid' do
      expect(described_class.new(attrs)).to be_valid
    end

    it 'is valid with empty url' do
      attrs[:site] = ''
      expect(described_class.new(attrs)).to be_valid
    end
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
      ] + [nil] * 61 + [
        attrs[:odss].join(';')
      ]
    end

    it 'prepares CSV correctly' do
      about = described_class.new(attrs)
      expect(about.prepare_to_csv).to match_array(handmade)
    end
  end
end
