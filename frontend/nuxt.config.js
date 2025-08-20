import colors from "vuetify/es5/util/colors";

export default {
  env: {
    sheetsAPIKey: process.env.sheetsAPIKey,
    sheetID: process.env.sheetID,
    BACKEND_URL: process.env.BACKEND_URL,
    INICIATIVES_DATA_SOURCE_URL: process.env.INICIATIVES_DATA_SOURCE_URL,
    PDI_DATA_SOURCE_URL: process.env.PDI_DATA_SOURCE_URL,
    SKILLS_DATA_SOURCE_URL: process.env.SKILLS_DATA_SOURCE_URL,
    // below, the Feature toggles
    NEW_CATALOG: process.env.NEW_CATALOG,
    OPEN_CONEXAO_FORMS: process.env.OPEN_CONEXAO_FORMS,
    BETA_VERSION: process.env.BETA_VERSION,
    NEW_JOURNEY: process.env.NEW_JOURNEY,
    GOOGLE_ANALYTICS_ID: process.env.GOOGLE_ANALYTICS_ID
  },
  ssr: true,
  target: "server",
  /*
   ** Headers of the page
   */
  head: {
    titleTemplate: "Hub USPInovação",
    title: "Hub USPInovação",
    meta: [
      { charset: "utf-8" },
      { name: "viewport", content: "width=device-width, initial-scale=1" },
      {
        hid: "description",
        name: "description",
        content: process.env.npm_package_description || "",
      },
      { name: "msapplication-TileColor", content: "#da532c" },
      { name: "theme-color", content: "#ffffff" },
    ],
    link: [
      { rel: "icon", type: "image/x-icon", href: "/favicon/favicon.ico" },
      {
        rel: "stylesheet",
        href:
          "https://fonts.googleapis.com/css?family=Roboto:300,400,500,700|Material+Icons",
      },
      {
        rel: "apple-touch-icon",
        sizes: "180x180",
        href: "/favicon/apple-touch-icon.png",
      },
      {
        rel: "icon",
        type: "image/png",
        sizes: "32x32",
        href: "/favicon/favicon-32x32.png",
      },
      {
        rel: "icon",
        type: "image/png",
        sizes: "16x16",
        href: "/favicon/favicon-16x16.png",
      },
      { rel: "manifest", href: "/favicon/site.webmanifest" },
      {
        rel: "mask-icon",
        href: "/favicon/safari-pinned-tab.svg",
        color: "#5bbad5",
      },
    ],
    script: [
      {
        src: `https://www.googletagmanager.com/gtag/js?id=${process.env.GOOGLE_ANALYTICS_ID}`,
        async: true,
      },
      {
        hid: "gtag-init",
        innerHTML: `
          window.dataLayer = window.dataLayer || [];
          function gtag(){dataLayer.push(arguments);}
          gtag('js', new Date());
          gtag('config', '${process.env.GOOGLE_ANALYTICS_ID}');
        `,
        type: "text/javascript",
        charset: "utf-8",
      },
    ],
    __dangerouslyDisableSanitizersByTagID: {
      "gtag-init": ["innerHTML"],
    },
  },
  /*
   ** Customize the progress-bar color
   */
  loading: { color: "#fff" },
  /*
   ** Global CSS
   */
  css: [],
  /*
   ** Plugins to load before mounting the App
   */
  plugins: [
    { src: "~/plugins/breakpoint.js" },
    { src: "~/plugins/vue-fuse.js", mode: "client" },
    { src: "~/plugins/vue-typer.js", mode: "client" },
    { src: "~/plugins/campi.js" },
    { src: "~/plugins/municipios.js" },
    { src: "~/plugins/knowledge_areas.js" },
    { src: "~/plugins/cnae.js" },
    { src: "~/plugins/company.js" },
    { src: "~/plugins/company_nature.js" },
    { src: "~/plugins/fuzzySearch.js" },
    { src: "~/plugins/indexer.js" },
    { src: "~/plugins/remove_accent.js" },
    { src: "~/plugins/services/fetch_companies.js" },
    { src: "~/plugins/services/fetch_disciplines.js" },
    { src: "~/plugins/services/fetch_iniciatives.js" },
    { src: "~/plugins/services/fetch_patents.js" },
    { src: "~/plugins/services/fetch_pdis.js" },
    { src: "~/plugins/services/fetch_skills.js" },
    { src: "~/plugins/services/get_company_data.js" },
    { src: "~/plugins/api/index.js" },
    { src: "~/plugins/services/update_company.js" },
    { src: "~/plugins/matching/disciplines.js" },
    { src: "~/plugins/matching/company.js" },
    { src: "~/plugins/matching/patent.js" },
    { src: "~/plugins/matching/skill.js" },
    { src: "~/plugins/matching/iniciative.js" },
    { src: "~/plugins/v-mask.js", mode: "client" },
    { src: "~/plugins/services/update_skills.js" },
  ],

  serverMiddleware: [{ path: "/", handler: "~/api/index.js" }],

  /*
   ** Nuxt.js dev-modules
   */
  buildModules: ["@nuxtjs/vuetify", "@nuxtjs/google-analytics"],
  /*
   ** Nuxt.js modules
   */
  modules: [
    "@nuxtjs/axios",
    "~/modules/Catalog/index.js",
    "~/modules/Discovery/index.js",
  ],
  /*
   ** vuetify module configuration
   ** https://github.com/nuxt-community/vuetify-module
   */
  vuetify: {
    customVariables: ["~/assets/variables.scss"],
    treeShake: true,
    theme: {
      themes: {
        light: {
          primary: "#ffb163",
          secondary: "#4AD4FF",
          tertiary: "#108CB3",
          error: colors.blue.darken1,
          background: "#ececec",
        },
        options: {
          customProperties: true,
        },
      },
    },
  },
  publicRuntimeConfig: {
    axios: {
      baseURL: process.env.BACKEND_URL,
    },
  },
  /*
   ** Build configuration
   */
  build: {
    /*
     ** You can extend webpack config here
     */
    // eslint-disable-next-line no-unused-vars
    extend(config, ctx) {},
  },
  googleAnalytics: {
    id: process.env.GOOGLE_ANALYTICS_ID,
  },
};
