# frozen_string_literal: true

require 'rails_helper'

RSpec.describe Staff, type: :model do
  let :attrs do
    {
      number_of_CLT_employees: 4,
      number_of_PJ_colaborators: 5,
      number_of_interns: 6,
      last_update: 10.seconds.ago
    }
  end

  context 'with validation problems' do
    %i[number_of_CLT_employees number_of_PJ_colaborators number_of_interns].each do |k|
      it 'field not a number' do
        attrs[k] = 'ten'
        expect(described_class.new(attrs)).to be_invalid
      end

      it 'field its empty' do
        attrs[k] = ''
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
        attrs[:number_of_CLT_employees],
        attrs[:number_of_PJ_colaborators],
        attrs[:number_of_interns]
      ] + [nil] * 24 + [attrs[:last_update]]
    end

    it 'prepares to CSV correctly' do
      colab = described_class.new(attrs)
      expect(colab.prepare_to_csv).to match_array(handmade)
    end
  end
end
