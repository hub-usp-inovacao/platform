# frozen_string_literal: true

class CnaeValidator < ActiveModel::EachValidator
  def validate_each(record, attribute, value)
    return if cnae_valid?(value)

    record.errors[attribute] << (options[:message] || 'deve ser um CNAE bem formatado xx.xx-x-xx')
  end

  def cnae_valid?(value)
    !value.nil? && value.match?(/\A\d{2}\.\d{2}-\d-\d{2}\Z/)
  end
end
