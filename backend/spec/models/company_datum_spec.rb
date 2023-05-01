# frozen_string_literal: true

require 'rails_helper'

RSpec.describe CompanyDatum, type: :model do
  let(:attrs) do
    {
      cnpj: '42.420.420/0001-21',
      public_name: 'test inc.',
      corporate_name: 'benefit of testing rails and other apps',
      year: 2019,
      cnae: '99.21-3-54',
      registry_status: 'Ativa',
      phones: [
        '(11) 99999-4433'
      ],
      emails: [
        'test@testinc.com'
      ],
      street: 'rua das couves, 37 - apt 51',
      neighborhood: 'vila vegetal',
      city: 'fito',
      size: 'MEI',
      state: 'plantae',
      zipcode: '04331-000'
    }
  end

  context 'with validation errors' do
    it 'on invalid registry status' do
      attrs[:registry_status] = 'bar'
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on inexistent attribute registry status' do
      expect(attrs).to include(:registry_status)
    end

    it 'on invalid size name' do
      attrs[:size] = 'Abacate'
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on malformed zipcode' do
      attrs[:zipcode] = 'absd'
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on cnpj format' do
      attrs[:cnpj] = '1231231asdb'
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on missing cnpj' do
      attrs.delete :cnpj
      expect(described_class.new(attrs)).to be_invalid
    end

    ['', 'N/D', nil].each do |attr|
      %i[public_name corporate_name].each do |field|
        it "on #{field} being \"#{attr}\"" do
          attrs[field] = ''
          expect(described_class.new(attrs)).to be_invalid
        end
      end
    end

    it 'on future year' do
      attrs[:year] = Time.zone.today.year + 1
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on nil cnae' do
      attrs[:cnae] = nil
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on empty cnae' do
      attrs[:cnae] = ''
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on malformed cnae' do
      attrs[:cnae] = '123.123'
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on malformed phones' do
      attrs[:phones] = ['123']
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on malformed emails' do
      attrs[:emails] = ['123']
      expect(described_class.new(attrs)).to be_invalid
    end
  end

  context 'with validations passing' do
    %i[phones emails].each do |attr|
      it "when #{attr} is empty" do
        attrs[attr] = []
        expect(described_class.new(attrs)).to be_valid
      end
    end

    ['', nil, 'any string'].each do |val|
      %i[city state street neighborhood].each do |attr|
        it "with any #{attr}" do
          attrs[:street] = val
          expect(described_class.new(attrs)).to be_valid
        end
      end
    end

    ['Ativa', 'Ativa Não Regular', 'Baixada',
     'Inapta', 'Nula', 'Suspensa'].each do |val|
      it "with registry status #{val}" do
        attrs[:registry_status] = val
        expect(described_class.new(attrs)).to be_valid
      end
    end

    it 'on missing registry_status' do
      attrs[:registry_status] = nil
      expect(described_class.new(attrs)).to be_valid
    end

    %w[MEI DEMAIS ME EPP].each do |val|
      it 'on company size' do
        attrs[:size] = val
        expect(described_class.new(attrs)).to be_valid
      end
    end
  end

  context 'with CSV preparation' do
    let :handmade do
      [
        nil,
        attrs[:cnpj],
        attrs[:public_name],
        attrs[:corporate_name],
        attrs[:year],
        attrs[:size],
        attrs[:cnae],
        attrs[:registry_status],
        attrs[:phones].join(';'),
        attrs[:emails].join(';'),
        attrs[:street],
        attrs[:neighborhood],
        attrs[:city],
        attrs[:state],
        attrs[:zipcode]
      ]
    end

    it 'prepares CSV correctly' do
      data = described_class.new(attrs)
      expect(data.prepare_to_csv).to match_array(handmade)
    end
  end
end
