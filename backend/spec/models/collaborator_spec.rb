require 'rails_helper'

RSpec.describe Collaborator, type: :model do
  let :attrs do 
    {
      number_of_clt: '4',
      number_of_pj: '4',
      number_of_scholarship: '4'
    }
end

  context 'with CSV preparation' do
    let :handmade do
      [nil] * 58 + [attrs[:number_of_clt], attrs[:number_of_pj], attrs[:number_of_scholarship]]
    end

    it 'prepares to CSV correctly' do
      colab = described_class.new(attrs)
      expect(colab.prepare_to_csv).to match_array(handmade)
    end
  end
end
