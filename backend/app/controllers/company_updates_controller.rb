# frozen_string_literal: true

class CompanyUpdatesController < ApplicationController
  def request_update
    params = request_update_params

    cnpj = params[:cnpj]
    @company = Company.where({ cnpj: cnpj }).first
    if @company.nil?
      render json: { error: 'company not found' }, status: :bad_request
      return
    end

    email = @company.emails.first
    token = TokenManager.create_token({ cnpj: cnpj })
    ApplicationMailer.company_update_token(email, token).deliver_now
    render json: { message: 'ok' }, status: :ok
  end

  # rubocop:disable Metrics/AbcSize
  def create
    prms = create_params
    @comp_update = CompanyUpdateRequest.new
    @comp_update.timestamp = Time.zone.now
    @comp_update.dna_usp_stamp = DnaUspStamp.new(prms[:dna_usp_stamp])
    @comp_update.company_data = CompanyDatum.new(prms[:data])
    @comp_update.about_company = AboutCompany.new(prms[:about])
    @comp_update.investment = Investment.new(prms[:investment])
    @comp_update.revenue = Revenue.new(prms[:revenue])
    @comp_update.incubation = Incubation.new(prms[:incubation])
    @comp_update.staff = Staff.new(prms[:staff])

    prms[:patents].each do |patent_params|
      comp_pat = CompanyPatent.new(patent_params)
      @comp_update.company_patents << comp_pat
    end

    partners = []
    prms[:partners].each_with_index do |raw_partner, index|
      partner = Partner.new(raw_partner.merge(index: index + 1))
      partners << partner
    end

    @comp_update.partners = partners

    if @comp_update.valid?
      @comp_update.save
      render json: { company_update: @comp_update }
    else
      render json: { errors: @comp_update.errors.full_messages }, status: :bad_request
    end
  end
  # rubocop:enable Metrics/AbcSize

  private

  def request_update_params
    params.require(:update_request).permit(:cnpj)
  end

  def create_params
    params.require(:company).permit(
      partners: %i[
        name
        email
        phone
        nusp
        bond
        unity
      ],
      dna_usp_stamp: {},
      data: {},
      about: {},
      investment: {},
      revenue: {},
      incubation: {},
      staff: {},
      patents: %i[name code]
    )
  end
end
