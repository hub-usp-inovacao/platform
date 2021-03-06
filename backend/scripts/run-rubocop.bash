#!/usr/bin/env bash

set -e

cd "${0%/*}/.."

echo "Running rubocop"
docker-compose -f dev-compose.yaml run --rm --no-deps web bundle exec rubocop
