# frozen_string_literal: true

require 'rails_helper'

RSpec.describe Investment, type: :model do
  let :attrs do
    {
      received: 'Sim',
      investments: [
        'Investimento próprio',
        'Investimento-anjo',
        'Venture capital',
        'Private equity',
        'PIPE-FAPESP',
        'Outro'
      ],
      own: 'R$ 12.000',
      angel: 'R$ 42',
      venture: 'R$ 4.000.000',
      equity: 'R$ 500.000',
      pipe: 'R$ 200.000',
      others: 'R$ 360.000'
    }
  end

  context 'with validation errors' do
    %i[own angel venture equity pipe others].each do |type|
      it 'on no digits for values' do
        attrs[type] = 'twelve'
        expect(described_class.new(attrs)).to be_invalid
      end
    end

    it 'on inconsistent state of received but did not inform values' do
      %i[own angel venture equity pipe others].each do |k|
        attrs.delete k
      end
      expect(described_class.new(attrs)).to be_invalid
    end
  end

  context 'without validation errors' do
    it 'is valid' do
      expect(described_class.new(attrs)).to be_valid
    end
  end

  context 'with CSV parsing' do
    let :handmade do
      [nil] * 62 + [
        'Sim',
        attrs[:investments].join(','),
        attrs[:own],
        attrs[:angel],
        attrs[:venture],
        attrs[:equity],
        attrs[:pipe],
        attrs[:others]
      ]
    end

    it 'prepares to CSV correctly' do
      invest = described_class.new(attrs)
      expect(invest.prepare_to_csv).to match_array(handmade)
    end
  end
end
