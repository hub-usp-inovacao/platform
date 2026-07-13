/**
 * blogSsrCache
 *
 * Cache de páginas SSR para as rotas do blog (/blog e /blog/*), funcionando
 * como um ISR pobre: a primeira requisição renderiza no servidor e o HTML fica
 * em cache por um TTL; as próximas são servidas do cache (sem bater no CMS nem
 * re-renderizar). Após o TTL, a página é renderizada de novo automaticamente —
 * sem necessidade de rebuild quando um post muda no Payload.
 *
 * Por que aqui e não em `nuxt generate`: o Nuxt 2 não tem renderização por rota,
 * e tornar o app inteiro estático quebraria o serverMiddleware (/centrais,
 * /servicos) e congelaria as páginas dinâmicas. Este módulo mantém o app SSR e
 * só intercepta as rotas do blog.
 *
 * Como funciona: no hook `render:done` o renderer já existe. `Server.renderRoute`
 * delega para `this.renderer.renderRoute` em tempo de chamada, então envolver
 * `renderer.renderRoute` intercepta todas as renderizações de página. Cacheamos
 * o objeto de resultado (HTML) antes da compressão/headers, que continuam sendo
 * aplicados normalmente pelo pipeline a cada resposta.
 *
 * Config por env (em frontend/.env):
 *   BLOG_CACHE_ENABLED  'true' força ligar, 'false' força desligar.
 *                       Default: ligado só quando NODE_ENV === 'production'.
 *   BLOG_CACHE_TTL      TTL em segundos (default 600 = 10 min).
 *   BLOG_CACHE_MAX      máximo de entradas no cache (default 500).
 *   BLOG_CACHE_DEBUG    'true' loga HIT/MISS/SKIP no stdout do container.
 */
module.exports = function blogSsrCache() {
  const flag = process.env.BLOG_CACHE_ENABLED
  const enabled =
    flag === 'true' || (flag !== 'false' && process.env.NODE_ENV === 'production')

  if (!enabled) return

  const ttlMs = (Number(process.env.BLOG_CACHE_TTL) || 600) * 1000
  const maxEntries = Number(process.env.BLOG_CACHE_MAX) || 500
  const debug = process.env.BLOG_CACHE_DEBUG === 'true'

  // key (url) -> { result, expires }
  const store = new Map()

  const shouldCache = (url) => {
    const path = String(url).split('?')[0].replace(/\/+$/, '') || '/'
    return path === '/blog' || path.startsWith('/blog/')
  }

  this.nuxt.hook('render:done', (server) => {
    const renderer = server && server.renderer
    if (!renderer || renderer.__blogCacheWrapped) return
    renderer.__blogCacheWrapped = true

    const original = renderer.renderRoute.bind(renderer)

    renderer.renderRoute = async function (url, context = {}) {
      if (!shouldCache(url)) return original(url, context)

      const cached = store.get(url)
      if (cached && Date.now() < cached.expires) {
        if (debug) console.log(`[blog-cache] HIT  ${url}`)
        // shallow clone para o pipeline não mutar o objeto cacheado
        return { ...cached.result }
      }

      const result = await original(url, context)

      // só cacheia render bem-sucedido (sem erro/redirect)
      if (result && !result.error && !result.redirected) {
        if (store.size >= maxEntries) {
          store.delete(store.keys().next().value)
        }
        store.set(url, { result, expires: Date.now() + ttlMs })
        if (debug) console.log(`[blog-cache] MISS ${url} (cached ${ttlMs / 1000}s)`)
      } else if (debug) {
        console.log(`[blog-cache] SKIP ${url} (error/redirect)`)
      }

      return result
    }

    console.log(
      `[blog-cache] habilitado — rotas=/blog* ttl=${ttlMs / 1000}s max=${maxEntries}`
    )
  })
}
