# Deployment

[[TOC]]

## Deployment View

This system follows a distributed architectural. At the moment we host all applications in a single Virtual Machine.

<img src="/views/deployment.svg" alt="Deployment View">

Each application is represented as a deployment unit:

- The application itself;
- Its corresponding configurations.

The Caddy server acts as a gateway, placed on the edge of the VM. Its role is to redirect requests to the corresponding application.

The backend recovers data from Google Spreadsheets and stores it in the database application as a local cache. It exposes an API to be consumed by the frontend.

### Configuration

This topic shows how to configurate all the application contexts

#### Front End

The `.env` file, in the root of the application, configures all the environments variables.

```
sheetsAPIKey=
GOOGLE_ANALYTICS_ID=
BACKEND_URL=
```

#### Back End

The `.env` file, in the root of the application, configures all the environments variables.

```
MONGO_INITDB_DATABASE=
RAILS_ENV=production
```

#### DataBase

The `.env` file, in the root of the application, configures all the environments variables.

```
MONGO_INITDB_DATABASE=
```

#### Reverse proxy

The `Caddyfile` file makes all the [Caddy](https://caddyserver.com/docs/) configuration.

```
hubuspinovacao.if.usp.br

handle_path /api/* {
    uri strip_prefix /api
    reverse_proxy localhost:3001
}

reverse_proxy /* localhost:3000
```

[:arrow_left: Go back](/)
