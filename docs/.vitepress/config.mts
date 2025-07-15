import { defineConfig } from "vitepress";

const base = "/platform/";

// https://vitepress.dev/reference/site-config
export default defineConfig({
  title: "Hub USPInovação",
  description:
    "Documentação da plataforma Hub USP Inovação desenvolvida pelo USPCodeLab",
  base,
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: [
      { text: "Inicio", link: "/" },
    ],

    sidebar: [
      {
        text: "Inicio",
      },
    ],

    socialLinks: [
      { icon: "github", link: "https://github.com/hub-usp-inovacao/platform" },
    ],
  },
  srcDir: "src",
});
