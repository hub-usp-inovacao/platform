# frozen_string_literal: true

require 'rails_helper'

RSpec.describe Revenue, type: :model do
  let :attrs do
    {
      last_year: 'R$ 1.200.000,00',
      last_update: 10.seconds.ago
    }
  end

  context 'with validation errors' do
    it 'on last_year being not money value' do
      attrs[:last_year] = 'twenty'
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on future last_update' do
      attrs[:last_update] = 10.seconds.from_now
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
      [nil] * 69 + [
        attrs[:last_year]
      ] + [nil] * 16 + [
        attrs[:last_update]
      ]
    end

    it 'prepares to CSV correctly' do
      invest = described_class.new(attrs)
      expect(invest.prepare_to_csv).to match_array(handmade)
    end
  end
end
