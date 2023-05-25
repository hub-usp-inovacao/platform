# frozen_string_literal: true

require 'rails_helper'

RSpec.describe Revenue, type: :model do
  let :attrs do
    {
      last_year: 'R$ 1.200.000,00'
    }
  end

  context 'with validation errors' do
    it 'on last_year being not money value' do
      attrs[:last_year] = 'twenty'
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
      [nil] * 70 + [
        attrs[:last_year]
      ]
    end

    it 'prepares to CSV correctly' do
      invest = described_class.new(attrs)
      expect(invest.prepare_to_csv).to match_array(handmade)
    end
  end
end
