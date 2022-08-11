# frozen_string_literal: true

class SkillController < ApplicationController
  def index
    @skills = Skill.all
    render json: @skills, status: :ok
  end

  # params: token
  # response: skill
  def protected_read_one
    prm = protected_read_one_params
    payload = TokenManager.decode_token(prm[:token])

    if payload.nil?
      render json: { error: 'invalid token' }, status: :bad_request
      return
    end

    ## caso contrário, token inalterado
    email = payload['email']
    @skill = Skill.find_by({ email: email })
    render json: @skill
  end

  private

  def protected_read_one_params
    # { skill: { token: '...' } }
    # -> { token: '...' }
    params.require(:skill).permit(:token)
  end
end
