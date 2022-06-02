# frozen_string_literal: true

class SkillController < ApplicationController
  def index
    @skills = Skill.all
    render json: @skills, status: :ok
  end

  def protected_read_one
    token = token_params[:token]
    payload = TokenManager.decode_token(token)

    if payload.nil?
      render json: { error: 'invalid token' }, status: :bad_request
      return
    end
    @skill = Skill.where({ email: payload["email"] }).first
    render json: @company, status: :ok
  end
end
