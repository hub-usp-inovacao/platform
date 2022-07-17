import * as path from "path";

export default function CatalogModule() {
  this.extendRoutes((routes) => {
    routes.push(
      {
        name: "educacao",
        path: "/educacao",
        component: path.resolve(__dirname, "pages/educacao.vue"),
      },
      {
        name: "empresas",
        path: "/empresas",
        component: path.resolve(__dirname, "pages/empresas.vue"),
      }
    );
  });

  this.nuxt.options.plugins.push(
    {
      src: "~/modules/Catalog/plugins/DisciplineAdapter.js",
      mode: "client",
    },
    {
      src: "~/modules/Catalog/plugins/CompanyAdapter.js",
      mode: "client",
    }
  );
}
