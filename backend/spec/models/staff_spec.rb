# frozen_string_literal: true

require 'rails_helper'

RSpec.describe Staff, type: :model do
  let :attrs do
    {
      number_of_CLT_employees: 4,
      number_of_PJ_colaborators: 5,
      number_of_interns: 6
    }
  end

  context 'with validation passing' do
    %i[number_of_CLT_employees number_of_PJ_colaborators number_of_interns].each do |k|
      it 'field not a number' do
        attrs[k] = 'ten'
        expect(described_class.new(attrs)).to be_valid
      end

      it 'field its empty' do
        attrs[k] = ''
        expect(described_class.new(attrs)).to be_valid
      end
    end
  end

  context 'with CSV preparation' do
    let :handmade do
      [nil] * 71 + [
        attrs[:number_of_CLT_employees],
        attrs[:number_of_PJ_colaborators],
        attrs[:number_of_interns]
      ]
    end

    it 'prepares to CSV correctly' do
      colab = described_class.new(attrs)
      expect(colab.prepare_to_csv).to match_array(handmade)
    end
  end
end
