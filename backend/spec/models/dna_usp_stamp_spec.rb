# frozen_string_literal: true

require 'rails_helper'

RSpec.describe DnaUspStamp, type: :model do
  let :attrs do
    {
      wants: true,
      truthful_informations: true,
      permissions: [
        # rubocop:disable Layout/LineLength
        'Permito o envio de e-mails para ser avisado sobre eventos e oportunidades relevantes à empresa',
        'Permito a divulgação das informações públicas na plataforma Hub USPInovação',
        'Permito a divulgação das informações públicas na plataforma Hub USPInovação e também para unidades da USP'
        # rubocop:enable Layout/LineLength
      ],
      email: 'fulano@mail.com',
      name: 'fulano de tal'
    }
  end

  context 'with CSV preparation' do
    it 'prepares to CSV correctly' do
      handmade = [nil] * 23 + [
        attrs[:wants],
        attrs[:email],
        attrs[:name],
        nil,
        attrs[:truthful_informations],
        attrs[:permissions].join(';')
      ]

      dna = described_class.new(attrs)
      expect(dna.prepare_to_csv).to match_array(handmade)
    end
  end

  context 'with validation problems' do
    it 'on nil permissions' do
      attrs.delete :permissions
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on unexpected permissions' do
      attrs[:permissions] = [4]
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on empty name when wants is set to true' do
      attrs[:name] = ''
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on empty email when wants is set to true' do
      attrs[:email] = ''
      expect(described_class.new(attrs)).to be_invalid
    end
  end

  context 'with no validation problems' do
    it 'on all required fields expected to be valid' do
      attrs[:name] = 'fulano de tal'
      attrs[:email] = 'fulano@email.com'
      attrs[:truthful_informations] = true
      expect(described_class.new(attrs)).to be_valid
    end
  end

  context 'with type parsing by mongoid' do
    [42, 0, '', {}, []].each do |value|
      %i[wants truthful_informations].each do |attr|
        it "parses #{value} to a valid boolean value for #{attr}" do
          attrs[attr] = value
          expect(described_class.new(attrs)).to be_valid
        end
      end
    end
  end
end
