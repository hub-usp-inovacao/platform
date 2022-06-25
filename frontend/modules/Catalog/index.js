import * as path from "path"

export default function CatalogModule() {
  this.extendRoutes((routes) => {
    routes.push({
      name: 'educacao',
      path: '/educacao',
      component: path.resolve(__dirname, "pages/educacao.vue")
    })
  })

  this.nuxt.options.plugins.push({
    src: "~/modules/Catalog/plugins/DisciplineAdapter.js",
    mode: "client"
  })
}