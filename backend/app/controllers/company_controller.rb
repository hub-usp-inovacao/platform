# frozen_string_literal: true

class CompanyController < ApplicationController
  def index
    if params.key? :cnpj
      find_one
    else
      all
    end
  end

  def protected_read_one
    token = token_params[:token]
    payload = TokenManager.decode_token(token)

    if payload.nil?
      render json: { error: 'invalid token' }, status: :bad_request
      return
    end

    @company = Company.where({ cnpj: payload['cnpj'] }).first
    render json: @company, status: :ok
  end

  private

  def token_params
    params.require(:security).permit(:token)
  end

  def all
    @companies = Company.all
    render json: @companies, status: :ok
  end

  def find_one
    render status: :bad_request
  end
end
