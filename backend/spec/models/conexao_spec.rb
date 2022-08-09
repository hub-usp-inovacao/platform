# frozen_string_literal: true

require 'rails_helper'

RSpec.describe Conexao, type: :model do
  let(:valid_attr) do
    {
      requestId: 'request-id',
      personal: {
        name: 'Neymar',
        email: 'ney10@hotmail.com',
        represent: 'Empresa'
      },
      org: {
        name: 'Desempedidos',
        cnpj: '14.952.400/0002-31',
        size: 'Média',
        phone: '11 961442245',
        address: 'rua do Matão, 1010',
        city: 'São Paulo',
        site: 'www.joga10.com.br'
      },
      demand: {
        cnae: {
          major: 'Indústria de Transformação',
          minor: 'Bebidas'
        },
        description: 'Quero elaborar um novo produto para jogadores de futebol',
        expectation: 'Novo produto',
        wantedProfile: 'Saúde',
        necessity: 'Desenvolvimento de P&D em parceria',
        knownForm: 'Facebook, Linkedin'
      }
    }
  end

  let(:valid_attr_to_s) do
    <<~MULTILINE
      Dados Pessoais:
      \tNome: #{valid_attr[:personal][:name]}
      \tEmail: #{valid_attr[:personal][:email]}
      \tRepresenta uma: #{valid_attr[:personal][:represent]}
      Dados da organização:
      \tNome: #{valid_attr[:org][:name]}
      \tCNPJ: #{valid_attr[:org][:cnpj]}
      \tTamanho da empresa: #{valid_attr[:org][:size]}
      \tTelefone: #{valid_attr[:org][:phone]}
      \tEndereço: #{valid_attr[:org][:address]}
      \tCidade: #{valid_attr[:org][:city]}
      \tSite: #{valid_attr[:org][:site]}
      Demanda:
      \tÁrea Primária: #{valid_attr[:demand][:cnae][:major]}
      \tÁrea Secundária: #{valid_attr[:demand][:cnae][:minor]}
      \tDescrição: #{valid_attr[:demand][:description]}
      \tExpectativa: #{valid_attr[:demand][:expectation]}
      \tPerfil de pesquisador desejado: #{valid_attr[:demand][:wantedProfile]}
      \tQual é a sua necessidade em relação a esses pesquisadores?: #{valid_attr[:demand][:necessity]}
      \tComo ficou sabendo do Hub USP Inovação?:  #{valid_attr[:demand][:knownform]}

    MULTILINE
  end

  it 'is valid with valid attributes' do
    conexao = described_class.new(valid_attr)
    expect(conexao).to be_valid
  end

  describe 'Org Hash' do
    %i[cnpj size].each do |attr_name|
      it "is invalid with invalid #{attr_name}" do
        attr = valid_attr
        attr[:org][attr_name] = '1127829784'
        conexao = described_class.new(attr)
        expect(conexao).to be_invalid
      end
    end

    it 'is valid with Microempresa as size' do
      attr = valid_attr
      attr[:org][:size] = 'Microempresa'
      conexao = described_class.new(attr)
      expect(conexao).to be_valid
    end
  end

  describe 'Demand Hash' do
    %i[major minor].each do |attr_name|
      it "is invalid with invalid cnae #{attr_name} area" do
        attr = valid_attr
        attr[:demand][:cnae][attr_name] = '1127829784'
        conexao = described_class.new(attr)
        expect(conexao).to be_invalid
      end
    end

    it 'is invalid with invalid wantedProfile' do
      attr = valid_attr
      attr[:demand][:wantedProfile] = '1127829784'
      conexao = described_class.new(attr)
      expect(conexao).to be_invalid
    end
  end
end
