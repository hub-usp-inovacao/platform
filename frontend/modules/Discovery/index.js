import * as path from "path";

export default function DiscoveryModule() {
  const NEW_JOURNEY = process.env.NEW_JOURNEY === "true";

  const pathPrefix = "/jornada";

  this.extendRoutes((routes) => {
    routes.push(
      {
        name: "jornada",
        path: pathPrefix,
        component: NEW_JOURNEY
          ? path.resolve(__dirname, "pages/index.vue")
          : path.resolve(__dirname, "pages/legacy/index.vue"),
      },
      {
        name: "aprenda",
        path: pathPrefix + "/aprenda",
        component: NEW_JOURNEY
          ? path.resolve(__dirname, "pages/aprenda.vue")
          : path.resolve(__dirname, "pages/legacy/aprenda.vue"),
      },
      {
        name: "pratica",
        path: pathPrefix + "/pratica",
        component: NEW_JOURNEY
          ? path.resolve(__dirname, "pages/pratica.vue")
          : path.resolve(__dirname, "pages/legacy/pratica.vue"),
      },
      {
        name: "criar",
        path: pathPrefix + "/criar",
        component: NEW_JOURNEY
          ? path.resolve(__dirname, "pages/criar.vue")
          : path.resolve(__dirname, "pages/legacy/criar.vue"),
      },
      {
        name: "aprimorar",
        path: pathPrefix + "/aprimorar",
        component: NEW_JOURNEY
          ? path.resolve(__dirname, "pages/aprimorar.vue")
          : path.resolve(__dirname, "pages/legacy/aprimorar.vue"),
      },
      {
        name: "financiamento",
        path: pathPrefix + "/financiamento",
        component: NEW_JOURNEY
          ? path.resolve(__dirname, "pages/financiamento.vue")
          : path.resolve(__dirname, "pages/legacy/financiamento.vue"),
      }
    );
  });

  this.nuxt.options.plugins.push({
    src: "~/modules/Discovery/plugins/JornadaAdapter.js",
    mode: "client",
  });
}
