import type { AdminViewServerProps } from 'payload'

import { DefaultTemplate } from '@payloadcms/next/templates'
import { Gutter } from '@payloadcms/ui'
import React from 'react'

import { RefreshButton } from './RefreshButton'

export function RefreshDataView({
  initPageResult,
  params,
  searchParams,
}: AdminViewServerProps) {
  const {
    req: { user },
  } = initPageResult

  if (!user) {
    return (
      <Gutter>
        <h1>Atualizar dados</h1>
        <p>Você precisa estar autenticado para acessar esta página.</p>
      </Gutter>
    )
  }

  return (
    <DefaultTemplate
      i18n={initPageResult.req.i18n}
      locale={initPageResult.locale}
      params={params}
      payload={initPageResult.req.payload}
      permissions={initPageResult.permissions}
      searchParams={searchParams}
      user={initPageResult.req.user || undefined}
      visibleEntities={initPageResult.visibleEntities}
    >
      <Gutter>
        <h1 style={{ marginBottom: '1rem' }}>Atualizar dados</h1>
        <p>
          Recarrega todos os dados (Empresas, PDIs, Disciplinas, Pesquisadores,
          Iniciativas e Patentes) das planilhas do Google para o banco de dados.
        </p>
        <p style={{ color: 'var(--theme-elevation-500)', marginBottom: '1.5rem' }}>
          A atualização roda em segundo plano. Erros de validação são enviados
          automaticamente por e-mail para a equipe de desenvolvimento.
        </p>
        <RefreshButton />
      </Gutter>
    </DefaultTemplate>
  )
}
