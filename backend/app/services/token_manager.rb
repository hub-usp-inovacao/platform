# frozen_string_literal: true

require 'jwt'

class TokenManager
  @@algorithm = 'HS256'
  @@secret = ENV['JWT_SECRET']
  @@issuer = ENV['JWT_ISSUER']

  class << self
    def create_token(payload)
      enriched_payload = payload.merge(
        expiring_time,
        issuer
      )

      JWT.encode enriched_payload, @@secret, @@algorithm
    end

    def decode_token(token)
      JWT.decode token, @@secret, true, {
        algorithm: @@algorithm,
        verify_expiration: true,
        verify_iss: true,
        verify_aud: true,
        iss: @@issuer
      }
    rescue JWT::ExpiredSignature
      nil
    rescue JWT::InvalidIssuerError
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
  end
end
