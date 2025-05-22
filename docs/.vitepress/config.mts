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
      { text: "Home", link: "/" },
      { text: "Examples", link: "/markdown-examples" },
    ],

    sidebar: [
      {
        text: "Examples",
        items: [
          { text: "Markdown Examples", link: "/markdown-examples" },
          { text: "Runtime API Examples", link: "/api-examples" },
        ],
      },
    ],

    socialLinks: [
      { icon: "github", link: "https://github.com/vuejs/vitepress" },
    ],
  },
  srcDir: "src",
});
