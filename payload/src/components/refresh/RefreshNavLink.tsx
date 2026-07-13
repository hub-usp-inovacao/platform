import Link from 'next/link'
import React from 'react'

export function RefreshNavLink() {
  return (
    <Link className="nav__link" href="/admin/refresh-data" prefetch={false}>
      <span className="nav__link-label">Atualizar dados</span>
    </Link>
  )
}
