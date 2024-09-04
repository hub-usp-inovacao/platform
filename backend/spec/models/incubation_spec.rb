# frozen_string_literal: true

require 'rails_helper'

RSpec.describe Incubation, type: :model do
  let :attrs do
    {
      was_incubated: 'Sim. A empresa já está graduada',
      ecosystems: 'Porto Digital'
    }
  end

  context 'without validation problems' do
    it 'works when was not incubated' do
      attrs[:was_incubated] = 'Não'
      attrs[:ecosystems] = 'Direto para o Mercado'

      expect(described_class.new(attrs)).to be_valid
    end

    it 'works when no ecosystems is provided' do
      attrs[:was_incubated] = 'Não'
      attrs.delete :ecosystems
      expect(described_class.new(attrs)).to be_valid
    end

    it 'works when empty ecosystems is provided' do
      attrs[:was_incubated] = 'Não'
      attrs[:ecosystems] = ''
      expect(described_class.new(attrs)).to be_valid
    end
  end

  context 'with validation problems' do
    it 'on absense of was_incubated' do
      attrs.delete :was_incubated
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on unexpected answer' do
      attrs[:was_incubated] = 'qualquer coisa'
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on inconsistent ecosystems based on being incubated' do
      attrs[:was_incubated] = 'Sim. A empresa está incubada'
      attrs[:ecosystems] = 'Direto para o Mercado'
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on inconsistent incubated based on ecosystems' do
      attrs[:was_incubated] = 'Não'
      attrs[:ecosystems] = 'Porto Digital'
      expect(described_class.new(attrs)).to be_invalid
    end
  end

  context 'with CSV preparation' do
    let :handmade do
      [nil] * 24 + [
        attrs[:was_incubated],
        attrs[:ecosystems]
      ]
    end

    it 'prepares to CSV correctly' do
      incub = described_class.new(attrs)
      expect(incub.prepare_to_csv).to match_array(handmade)
    end
  end
end
