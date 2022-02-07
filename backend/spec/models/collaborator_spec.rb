require 'rails_helper'

RSpec.describe Collaborator, type: :model do
  let :attrs do 
    {
      numberOfCTLEmployees: '4',
      numberOfPJColaborators: '5',
      numberOfInterns: '6',
      last_update: 10.seconds.ago
    }
end

  context 'with validation problems' do
    it 'field not a number' do
      %i[numberOfCTLEmployees numberOfPJColaborators numberOfInterns].each do |k|
        attrs[k] = 'ten'
      expect(described_class.new(attrs)).to be_invalid
      end
    end

    it 'on future last_update' do
      attrs[:last_update] = 10.seconds.from_now
      expect(described_class.new(attrs)).to be_invalid
    end
  end

  context 'with CSV preparation' do
    let :handmade do
      [nil] * 58 + [
        attrs[:numberOfCTLEmployees], attrs[:numberOfPJColaborators], attrs[:numberOfInterns]
        ] + [nil] * 24 + [attrs[:last_update]]
    end

    it 'prepares to CSV correctly' do
      colab = described_class.new(attrs)
      expect(colab.prepare_to_csv).to match_array(handmade)
    end
  end
end
