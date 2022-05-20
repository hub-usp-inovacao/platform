class SkillsUpdateController < ApplicationController
    def request_update
        params = request_update_params
    
        email = params[:email]
        @skill = Skill.where({ email: email }).first
        if @skill.nil?
          render json: { error: 'skill not found' }, status: :bad_request
          return
        end
    
        skill = @skill.email
        token = TokenManager.create_token({ email: email })
        ApplicationMailer.skill_update_token("luizcarlosdk@usp.br", token).deliver_now
        render json: { message: 'ok' }, status: :ok
      end
end
