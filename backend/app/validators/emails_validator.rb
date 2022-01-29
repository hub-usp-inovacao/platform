# frozen_string_literal: true

class EmailsValidator < ActiveModel::EachValidator
  def validate_each(record, attribute, value)
    return if list_of_emails? value

    error_message = 'devem ser emails válidos. Ex: example@domain.com'

    record.errors[attribute] << (options[:message] || error_message)
  end

  def list_of_emails?(value)
    value.is_a?(Array) &&
      value.all? do |val|
        email_valid? val
      end
  end

  def email_valid?(raw)
    # rubocop:disable Layout/LineLength
    raw.match?(/\A(|(([A-Za-z0-9]+_+)|([A-Za-z0-9]+-+)|([A-Za-z0-9]+\.+)|([A-Za-z0-9]+\++))*[A-Za-z0-9]+@((\w+-+)|(\w+\.))*\w{1,63}\.[a-zA-Z]{2,6})\Z/i)
    # rubocop:enable Layout/LineLength
  end
end
