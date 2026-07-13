# Hub USPInovação

This repository is composed by an frontend (NuxtJs), backend (Kotlin) and legacy backend (Ruby on Rails)

TO build-up this project, it's needed `docker-compose` command. Flags can be found into `docker-compose.yaml` file.

## Migração dos índices de busca

Antes de publicar uma versão que altere os índices de texto, execute a migração uma única vez em uma janela de manutenção:

```bash
docker-compose --profile cli run --rm catalogcli migrate-text-indexes
```

O comando recria somente índices incompatíveis, cria os ausentes e confirma os que já estão atualizados. Execute-o antes de iniciar a nova versão do `catalogapp`; a aplicação não remove índices automaticamente durante a inicialização.
