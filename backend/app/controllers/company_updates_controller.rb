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
    email = format_email(email)

    render json: { message: 'ok', email: email }, status: :ok
  end

  # rubocop:disable Metrics/AbcSize
  # rubocop:disable Metrics/MethodLength
  # rubocop:disable Metrics/PerceivedComplexity
  def create
    prms = create_params
    errors = {}
    has_error = false

    dna_usp_stamp = DnaUspStamp.new(prms[:dna_usp_stamp])
    unless dna_usp_stamp.valid?
      errors[:dna_usp_stamp] = dna_usp_stamp.errors.full_messages
      has_error = true
    end

    company_data = CompanyDatum.new(prms[:company_data])
    unless company_data.valid?
      errors[:company_data] = company_data.errors.full_messages
      has_error = true
    end

    about_company = AboutCompany.new(prms[:about_company])
    unless about_company.valid?
      errors[:about_company] = about_company.errors.full_messages
      has_error = true
    end

    investment = Investment.new(prms[:investment])
    unless investment.valid?
      errors[:investment] = investment.errors.full_messages
      has_error = true
    end

    revenue = Revenue.new(prms[:revenue])
    unless revenue.valid?
      errors[:revenue] = revenue.errors.full_messages
      has_error = true
    end

    incubation = Incubation.new(prms[:incubation])
    unless incubation.valid?
      errors[:incubation] = incubation.errors.full_messages
      has_error = true
    end

    staff = Staff.new(prms[:staff])
    unless staff.valid?
      errors[:staff] = staff.errors.full_messages
      has_error = true
    end

    partners = []
    partners_errors = []
    prms[:partners].each_with_index do |raw_partner, index|
      partner = Partner.new(raw_partner.merge(index: index + 1))
      unless partner.valid?
        errors[:partners] = partner.errors.full_messages
        partners_errors << partner.errors.full_messages
        has_error = true
      end

      partners << partner
    end

    if has_error
      render json: { errors: errors }, status: :bad_request
    else
      about_company.save_logo(prms[:about_company][:logo], request.base_url)

      @comp_update = CompanyUpdateRequest.new
      @comp_update.timestamp = Time.zone.now

      @comp_update.dna_usp_stamp = dna_usp_stamp
      @comp_update.company_data = company_data
      @comp_update.about_company = about_company
      @comp_update.investment = investment
      @comp_update.revenue = revenue
      @comp_update.incubation = incubation
      @comp_update.staff = staff
      @comp_update.partners = partners

      if @comp_update.save
        ApplicationMailer.confirmation_company_update(@comp_update).deliver_now
        render json: { company_update: @comp_update }
      else
        render json: { error: @comp_update.errors.full_messages }, status: :bad_request
      end
    end
  end
  # rubocop:enable Metrics/AbcSize
  # rubocop:enable Metrics/MethodLength
  # rubocop:enable Metrics/PerceivedComplexity

  def create_logo
    params = create_logo_params
    logo = params[:logo]
    cnpj = params[:cnpj]

    if logo.present?
      store_tmp_logo(logo, cnpj)
    else
      render status: :bad_request, json: { message: 'Logo is required' }
    end
  end

  private

  def create_logo_params
    params.require(:company).permit(:cnpj, :logo)
  end

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
      company_data: {},
      about_company: {},
      investment: {},
      revenue: {},
      incubation: {},
      staff: {}
    )
  end

  def format_email(email)
    first_part_length = email.index('@')

    case first_part_length

    when 1, 2
      email.gsub(/.{1}@/, '*@')

    when 3, 4
      email.gsub(/.{2}@/, '**@')

    when 5, 6
      email.gsub(/.{3}@/, '***@')

    else
      email.gsub(/.{5}@/, '*****@')
    end
  end

  def store_tmp_logo(logo_file, cnpj)
    cnpj = cnpj.gsub(/[^0-9]/, '')
    filename = cnpj + File.extname(logo_file.original_filename)

    logo_path = Rails.root.join('tmp', 'uploads', 'logos', filename)

    File.open(logo_path, 'wb') do |file|
      file.write(logo_file.read)
    end

    render status: :ok
  end
end
