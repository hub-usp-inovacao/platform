'use client'

import { Button } from '@payloadcms/ui'
import React, { useState } from 'react'

// Endpoint autenticado do Payload que repassa o refresh ao Kotlin (não chama o Kotlin direto).
const REFRESH_URL = '/cms/api/refresh-catalog'

type Status = { type: 'success' | 'error'; message: string } | null

export function RefreshButton() {
  const [loading, setLoading] = useState(false)
  const [status, setStatus] = useState<Status>(null)

  const refreshAll = async () => {
    setLoading(true)
    setStatus(null)
    try {
      const res = await fetch(REFRESH_URL, { method: 'POST', credentials: 'include' })
      if (!res.ok) throw new Error(`HTTP ${res.status}`)
      setStatus({
        type: 'success',
        message: 'Atualização iniciada! Os dados serão atualizados em segundo plano.',
      })
    } catch (err) {
      setStatus({
        type: 'error',
        message: 'Falha ao iniciar a atualização. Verifique os logs do servidor.',
      })
    } finally {
      setLoading(false)
    }
  }

  return (
    <div>
      <Button onClick={refreshAll} disabled={loading} buttonStyle="primary">
        {loading ? 'Atualizando…' : 'Atualizar todos os dados'}
      </Button>
      {status && (
        <p
          style={{
            marginTop: '1rem',
            color:
              status.type === 'error'
                ? 'var(--theme-error-500)'
                : 'var(--theme-success-500)',
          }}
        >
          {status.message}
        </p>
      )}
    </div>
  )
}
