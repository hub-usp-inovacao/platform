module.exports = {
  title: 'Hub USPInovação',
  description: 'Documento com a arquitetura da plataforma',
  theme: 'default-prefers-color-scheme',
  head: [
    ['link', { rel: "icon", href: "/favicon.ico" }],
  ],
  themeConfig: {
    overrideTheme: 'dark',
    logo: '/hub_logo.svg',
    nav: [
      { text: 'Front End', link: '/frontend/'},
      { text: 'Back End', link: '/backend/'},
      { text: 'Deployment', link: '/deployment/'}
    ]
  }
}