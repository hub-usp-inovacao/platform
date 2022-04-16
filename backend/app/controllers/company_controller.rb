# frozen_string_literal: true

class CompanyController < ApplicationController
  def index
    if params.key? :cnpj
      find_one
    else
      all
    end
  end

  private

  def all
    @companies = Company.all
    render json: @companies, status: :ok
  end

  def find_one
    render status: :bad_request
  end
end
