import * as path from "path";

export default function CatalogModule() {
  const NEW_CATALOG = process.env.NEW_CATALOG === "true";

  this.extendRoutes((routes) => {
    routes.push(
      {
        name: "inovacao",
        path: "/inovacao",
        component: NEW_CATALOG
          ? path.resolve(__dirname, "pages/inovacao.vue")
          : path.resolve(__dirname, "pages/legacy/inovacao.vue"),
      },
      {
        name: "educacao",
        path: "/educacao",
        component: NEW_CATALOG
          ? path.resolve(__dirname, "pages/educacao.vue")
          : path.resolve(__dirname, "pages/legacy/educacao.vue"),
      },
      {
        name: "empresas",
        path: "/empresas",
        component: NEW_CATALOG
          ? path.resolve(__dirname, "pages/empresas.vue")
          : path.resolve(__dirname, "pages/legacy/empresas.vue"),
      },
      {
        name: "competencias",
        path: "/competencias",
        component: NEW_CATALOG
          ? path.resolve(__dirname, "pages/competencias.vue")
          : path.resolve(__dirname, "pages/legacy/competencias.vue"),
      },
      {
        name: "patentes",
        path: "/patentes",
        component: NEW_CATALOG
          ? path.resolve(__dirname, "pages/patentes.vue")
          : path.resolve(__dirname, "pages/legacy/patentes.vue"),
      },
      {
        name: "iniciativas",
        path: "/iniciativas",
        component: NEW_CATALOG
          ? path.resolve(__dirname, "pages/iniciativas.vue")
          : path.resolve(__dirname, "pages/legacy/iniciativas.vue"),
      }
    );
  });

  this.nuxt.options.plugins.push(
    {
      src: "~/modules/Catalog/plugins/PDIAdapter.js",
      mode: "client",
    },
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
