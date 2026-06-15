import { mongooseAdapter } from '@payloadcms/db-mongodb'
import { lexicalEditor } from '@payloadcms/richtext-lexical'
import path from 'path'
import { buildConfig } from 'payload'
import { fileURLToPath } from 'url'
import sharp from 'sharp'

import { Users } from './collections/Users'
import { Posts } from './collections/Posts'
import { Media } from './collections/Media'

import { HomeSettings } from './globals/HomeSettings'

const filename = fileURLToPath(import.meta.url)
const dirname = path.dirname(filename)

export default buildConfig({
  admin: {
    user: Users.slug,
    importMap: {
      baseDir: path.resolve(dirname),
    },
    components: {
      views: {
        refreshData: {
          Component: '/components/refresh/RefreshDataView#RefreshDataView',
          path: '/refresh-data',
        },
      },
      afterNavLinks: ['/components/refresh/RefreshNavLink#RefreshNavLink'],
    },
  },
  collections: [Users, Posts, Media],
  globals: [HomeSettings],
  editor: lexicalEditor(),
  secret: process.env.PAYLOAD_SECRET || '',
  typescript: {
    outputFile: path.resolve(dirname, 'payload-types.ts'),
  },
  db: mongooseAdapter({
    url: process.env.DATABASE_URL || '',
  }),
  sharp,
  plugins: [],
  cors: '*',
  endpoints: [
    {
      // Proxy autenticado: valida a sessão do Payload e repassa ao Kotlin com o segredo.
      path: '/refresh-catalog',
      method: 'post',
      handler: async (req) => {
        if (!req.user) {
          return Response.json({ message: 'Unauthorized' }, { status: 401 })
        }

        const target = process.env.CATALOG_INTERNAL_URL || 'http://catalogapp:8080'
        const secret = process.env.HUB_REFRESH_SECRET || ''

        try {
          const upstream = await fetch(`${target}/refresh`, {
            method: 'POST',
            headers: { 'X-Refresh-Secret': secret },
          })
          if (!upstream.ok) {
            return Response.json(
              { message: 'Upstream refresh service returned an error' },
              { status: 502 },
            )
          }
          return Response.json({ message: 'Refresh started' }, { status: 202 })
        } catch (err) {
          req.payload.logger.error(`refresh-catalog proxy failed: ${err}`)
          return Response.json(
            { message: 'Could not reach the refresh service' },
            { status: 502 },
          )
        }
      },
    },
  ],
})
