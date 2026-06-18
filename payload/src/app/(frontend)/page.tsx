import { headers as getHeaders } from 'next/headers.js'
import Image from 'next/image'
import Link from 'next/link'
import { getPayload } from 'payload'
import React from 'react'

import config from '@/payload.config'
import './styles.css'

export default async function HomePage() {
  const headers = await getHeaders()
  const payloadConfig = await config
  const payload = await getPayload({ config: payloadConfig })
  const { user } = await payload.auth({ headers })

  return (
    <div className="home">
      <div className="content">
        <Image
          alt="Hub USP Logo"
          height={150}
          src="/hub_logo.svg"
          width={150}
          priority
        />
        {!user && <h1>Gerenciamento de Conteúdo</h1>}
        {user && <h1>Bem-vindo de volta, {user.email}</h1>}
        <div className="links">
          <Link
            className="admin"
            href={payloadConfig.routes.admin}
          >
            entrar
          </Link>
        </div>
      </div>
    </div>
  )
}
