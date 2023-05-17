  def create
    @company_datum = CompanyDatum.new(company_datum_params)

    if params[:logo].present?
      store_logo(params[:logo])
    end

    if @company_datum.save
      render json: @company_datum, status: :created
    else
      render json: @company_datum.errors, status: :unprocessable_entity
    end
  end

  private

  def company_datum_params
    params.permit(:cnpj, :public_name, :corporate_name, :year, :cnae, :registry_status, :phones, :emails, :street, :neighborhood, :city, :state, :zipcode, :company_nature, :size)
  end

  def store_logo(logo_file)
    filename = SecureRandom.uuid + File.extname(logo_file.original_filename)

    logo_path = Rails.root.join('public', 'uploads', 'logos', filename)

    File.open(logo_path, 'wb') do |file|
      file.write(logo_file.read)
    end

    @company_datum.logo = "/uploads/logos/#{filename}"
  end
end
