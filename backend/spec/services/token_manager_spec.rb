# frozen_string_literal: true

require 'rails_helper'

RSpec.describe TokenManager, type: :model do
  pl = { foo: 'baz' }

  it 'does not throw when generating a token' do
    expect { described_class.create_token(pl) }.not_to raise_error
  end

  it 'returns nil when decoding an expired token' do
    allow(described_class).to receive(:expiring_time).and_return({ exp: 10.seconds.ago.to_i })
    token = described_class.create_token(pl)

    payload = described_class.decode_token(token)
    expect(payload).to be_nil
  end

  it 'returns a payload when decoding a valid token' do
    given = { cnpj: '123' }
    token = described_class.create_token(given)
    payload = described_class.decode_token(token)
    expect(payload['cnpj']).to eql(given[:cnpj])
  end
end
