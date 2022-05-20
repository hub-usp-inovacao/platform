class SkillUpdatesRequestController < ApplicationController
    def request_update
        params = request_update_params
    
        email = params[:email]
        @skill = Skill.where({ email: email }).first
        if @skill.nil?
          render json: { error: 'skill not found' }, status: :bad_request
          return
        end
    
        email = @skill.email
        token = TokenManager.create_token({ email: email })
        ApplicationMailer.skill_update_token("luizcarlosdk@usp.br", token).deliver_now
        render json: { message: 'ok' }, status: :ok
      end
    
      def create
        prms = create_params
        @skill_update = SkillUpdateRequest.new
        @skill_update.email = Email.new(prms[:email])
        
        if @skill_update.valid?
          @skill_update.save
          render json: { skill_update: @skill_update }
        else
          render json: { errors: @skill_update.errors.full_messages }, status: :bad_request
        end
      end
    
      private
    
      def request_update_params
        params.require(:update_request).permit(:email)
      end
    
      def create_params
        params.require(:skill).permit(
          email: {}
        )
      end

end