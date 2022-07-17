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
      },
      {
        name: "competencias",
        path: "/competencias",
        component: path.resolve(__dirname, "pages/competencias.vue"),
      },
      {
        name: "patentes",
        path: "/patentes",
        component: path.resolve(__dirname, "pages/patentes.vue"),
      },
      {
        name: "iniciativas",
        path: "/iniciativas",
        component: path.resolve(__dirname, "pages/iniciativas.vue"),
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
    },
    {
      src: "~/modules/Catalog/plugins/ResearcherAdapter.js",
      mode: "client",
    },
    {
      src: "~/modules/Catalog/plugins/PatentAdapter.js",
      mode: "client",
    },
    {
      src: "~/modules/Catalog/plugins/InitiativeAdapter.js",
      mode: "client",
    }
  );
}
