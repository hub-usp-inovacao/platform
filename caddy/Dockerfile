FROM caddy:2.5.1-alpine AS base




FROM base AS dev

COPY Caddyfile.dev /etc/caddy/Caddyfile

EXPOSE 5000




FROM base AS prod

COPY Caddyfile /etc/caddy/Caddyfile
