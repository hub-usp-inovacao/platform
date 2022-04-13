# frozen_string_literal: true

require 'rails_helper'

RSpec.describe TokenManager, type: :model do
  pl = { foo: 'baz' }
  aud = 'foo@mail.com'

  it 'does not throw when generating a token' do
    expect { described_class.create_token(pl, aud) }.not_to raise_error
  end

  it 'returns nil when decoding an expired token' do
    allow(TokenManager).to receive(:expiring_time).and_return({ exp: 10.seconds.ago.to_i })
    token = described_class.create_token(pl, aud)

    payload = described_class.decode_token(token, aud)
    expect(payload).to be_nil
  end

  it 'returns nil when decoding with mismatching audience' do
    allow(TokenManager).to receive(:expiring_time).and_return({ exp: 10.seconds.ago.to_i })
    token = described_class.create_token(pl, aud)

    payload = described_class.decode_token(token, aud + 'foo')
    expect(payload).to be_nil
  end
end
