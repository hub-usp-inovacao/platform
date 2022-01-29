# frozen_string_literal: true

class CnpjValidator < ActiveModel::EachValidator
  def validate_each(record, attribute, value)
    return if value.nil? || cnpj_valid?(value)

    err = 'deve ser um CNPJ bem formatado xx.xxx.xxx/xxxx-xx'
    record.errors[attribute] << (options[:message] || err)
  end

  def cnpj_valid?(value)
    value.match?(%r{\A\d{2}\.\d{3}.\d{3}/\d{4}-\d{2}\Z})
  end
end
