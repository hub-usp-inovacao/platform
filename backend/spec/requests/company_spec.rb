# frozen_string_literal: true

require 'rails_helper'

RSpec.describe 'Companies', type: :request do
  cnpj = '12.123.123/0001-21'

  let(:companies) do
    [
      {
        cnpj: cnpj,
        name: 'Uber 99',
        year: '2019',
        services: %w[foo bar baz],
        incubated: 'Não',
        emails: [
          'foo@exmaple.com',
          'bar@exmaple.com'
        ],
        ecosystems: [
          'ESALQTec'
        ],
        description: {
          long: 'foo bar baz'
        },
        allowed: true,
        address: {
          cep: '13414-157',
          city: 'Piracicaba',
          neighborhood: 'Loteamento Santa Rosa',
          state: 'São Paulo',
          venue: 'Rua Cezira Giovanni'
        },
        active: true,
        url: 'https://techagr.com',
        odss: [
          '4 - Educação de Qualidade',
          '5 - Igualdade de Gênero',
          '8 - Trabalho Decente e Crescimento Econômico',
          '10 - Redução das Desigualdades'
        ],
        technologies: ['foo bar baz'],
        phones: ['(11) 987288877'],
        logo: 'https://drive.google.com/...',
        companySize: ['Média Empresa'],
        cnae: '66.13-4-00',
        classification: {
          major: 'Comércio e Serviços',
          minor: 'Informação e Comunicação'
        },
        partners: [
          {
            name: 'Fulano de Tal',
            nusp: '1234567',
            bond: 'Pesquisador',
            unity: 'Faculdade de Odontologia de Bauru - FOB',
            email: 'fulano@detal.com',
            phone: '(11) 99999-9999'
          },
          {
            name: '',
            nusp: '',
            bond: '',
            unity: '',
            email: '',
            phone: ''
          },
          {
            name: '',
            nusp: '',
            bond: '',
            unity: '',
            email: '',
            phone: ''
          },
          {
            name: '',
            nusp: '',
            bond: '',
            unity: '',
            email: '',
            phone: ''
          },
          {
            name: '',
            nusp: '',
            bond: '',
            unity: '',
            email: '',
            phone: ''
          }
        ],
        corporate_name: 'razão social',
        last_year: 'R$ 60.000,00',
        number_of_CLT_employees: 1,
        number_of_PJ_colaborators: 1,
        number_of_interns: 0,
        received: 'Sim',
        investments: ['Investimento próprio', 'PIPE-FAPESP'],
        investments_values: {
          own: 'R$ 12.000',
          angel: 'R$ 0,00',
          venture: 'R$ 0,00',
          equity: 'R$ 0,00',
          pipe: 'R$ 20.000',
          others: 'R$ 0,00'
        }
      }
    ]
  end

  def company_keys
    %w[_id name year services incubated emails ecosystems
       description allowed address active url odss technologies
       phones logo companySize classification created_at
       partners corporate_name cnae cnpj last_year
       number_of_CLT_employees number_of_PJ_colaborators
       number_of_interns received investments investments_values]
  end

  describe 'get all' do
    before do
      Company.delete_all
      Company.create!(companies)
      get '/companies'
    end

    after do
      Company.delete_all
    end

    describe 'GET /companies' do
      it 'returns a response with success http status' do
        expect(response).to have_http_status(:success)
      end

      it 'returns a list of all companies with expected format' do
        resp = JSON.parse(response.body)
        resp.each do |company|
          expect(company.keys.difference(company_keys)).to eq([])
        end
      end
    end
  end

  describe 'GET /companies?cnpj=' do
    it 'does not work anymore' do
      get '/companies', params: { cnpj: cnpj }
      expect(response).to have_http_status(:bad_request)
    end
  end

  describe 'GET /company' do
    it 'returns bad_request when no token is given' do
      post '/company'
      expect(response).to have_http_status(:bad_request)
    end

    it 'returns bad_request when token is invalid' do
      allow(TokenManager).to receive(:decode_token).and_return(nil)
      post '/company', params: { security: { token: 'foo' } }

      expect(response).to have_http_status(:bad_request)
    end

    it 'returns a company when token is valid' do
      mock_payload = { cnpj: '12.123.123/0001-21' }
      allow(TokenManager).to receive(:decode_token).and_return(mock_payload)
      allow(Company).to receive(:where).and_return([mock_payload])

      post '/company', params: { security: { token: 'foo' } }

      body = JSON.parse(response.body)

      expect(body['cnpj']).to eql(mock_payload[:cnpj])
    end
  end
end
