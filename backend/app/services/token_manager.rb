# frozen_string_literal: true

require 'jwt'

class TokenManager
  @@algorithm = 'HS256'
  @@secret = ENV['JWT_SECRET']
  @@issuer = ENV['JWT_ISSUER']

  class << self
    def create_token(payload, audience)
      enriched_payload = payload.merge(
        expiring_time,
        issuer,
        audience(audience)
      )

      JWT.encode enriched_payload, @@secret, @@algorithm
    end

    def decode_token(token, audience)
      JWT.decode token, @@secret, true, {
        algorithm: @@algorithm,
        verify_expiration: true,
        verify_iss: true,
        verify_aud: true,
        iss: @@issuer,
        aud: audience
      }
    rescue JWT::ExpiredSignature
      nil
    rescue JWT::InvalidIssuerError
      nil
    rescue JWT::InvalidAudError
      nil
    end

    private

    def expiring_time
      exp = 24.hours.from_now.to_i
      { exp: exp }
    end

    def issuer
      { iss: @@issuer }
    end

    def audience(audience)
      { aud: audience }
    end
  end
end
