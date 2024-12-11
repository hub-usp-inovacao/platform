<template>
  <v-app>
    <header>
      <UniversalAuspinBar />
    </header>
    <div class="nav">
      <v-toolbar
        class="custom-hidden-sm-and-down bar"
        color="transparent"
        width="100%"
        absolute
        flat
      >
        <v-spacer></v-spacer>
        <v-toolbar-items>
          <v-chip-group>
            <v-chip
              v-for="(item, i) in navItems"
              :key="i"
              :to="item.to"
              nuxt
              class="white--text mx-1 py-auto secondary px-4 subtitle-1"
            >
              {{ item.title }}
              <v-icon v-if="item.new" right>mdi-star</v-icon>
            </v-chip>
          </v-chip-group>
        </v-toolbar-items>
      </v-toolbar>
      <HubNavButton
        v-if="path != '/'"
        class="custom-hidden-sm-and-down"
        :margin="path == '/contato' ? false : true"
        :background="path == '/contato' ? false : true"
      />
   </div>

    <v-app-bar class="custom-hidden-md-and-up" color="white" dense flat fixed>
      <v-app-bar-nav-icon @click="drawer = true"></v-app-bar-nav-icon>
    </v-app-bar>

    <v-navigation-drawer v-model="drawer" fixed temporary>
      <v-list nav dense>
        <v-list-item-group v-model="group">
          <v-list-item to="/">
            <v-list-item-title> Home </v-list-item-title>
          </v-list-item>
          <v-list-item v-for="(item, i) in navItems" :key="i" :to="item.to">
            <v-list-item-title>
              {{ item.title }}
              <v-icon v-if="item.new" right>mdi-star</v-icon>
            </v-list-item-title>
          </v-list-item>
        </v-list-item-group>
        <HubNavButton :margin="false" :background="false" />
      </v-list>
    </v-navigation-drawer>

    <v-main :class="$vuetify.breakpoint.smAndDown ? 'mt-12' : ''">
      <BetaVersionModal v-if="isBetaVersion" />
      <nuxt />
    </v-main>
    <Footer />
  </v-app>
</template>

<script>
import HubNavButton from "@/components/first_level/HubNavButton.vue";
import Footer from "@/components/layout/Footer.vue";
import BetaVersionModal from "@/components/layout/BetaVersionModal.vue";
import UniversalAuspinBar from "@/components/UniversalAuspinBar.vue"

export default {
  components: {
    HubNavButton,
    Footer,
    BetaVersionModal,
    UniversalAuspinBar
  },
  data: () => ({
    activeItem: 0,
    snackbar: true,
    drawer: false,
    group: "",
  }),
  computed: {
    isBetaVersion() {
      return process.env.BETA_VERSION === "true";
    },
    path() {
      return this.$route.path;
    },
    FLAG_CONEXAO_RELEASED() {
      return process.env.OPEN_CONEXAO_FORMS === "true";
    },
    navItems() {
      const base = [
        {
          title: "Iniciativas",
          to: "/iniciativas",
        },
        {
          title: "P&D&I",
          to: "/inovacao",
        },
        {
          title: "Competências",
          to: "/competencias",
        },
        {
          title: "Educação",
          to: "/educacao",
        },
        {
          title: "Empresas DNA USP",
          to: "/empresas",
        },
        {
          title: "Patentes",
          to: "/patentes",
        },
        {
          title: "Jornada",
          to: "/jornada",
        },
        {
          title: "Contato",
          to: "/contato",
        },
      ];

      if (this.FLAG_CONEXAO_RELEASED)
        return base.concat({
          title: "Conexão",
          to: "/conexao",
          new: true,
        });

      return base;
    },
  },
  head() {
    return {
      link: [
        {
          rel: "stylesheet",
          href: "https://fonts.googleapis.com/css?family=Monda&display=swap",
        },
      ],
    };
  },
};
</script>

<style scoped>
.nav {
  position: relative;
}
#app .v-bottom-navigation .v-btn {
  height: inherit !important;
}
.zoom {
  transition: transform 0.2s;
}
.zoom:hover {
  transform: scale(1.1);
}

@media (min-width: 1050px) {
  .custom-hidden-md-and-up {
    display: none;
  }
}

@media (max-width: 1051px) {
  .custom-hidden-sm-and-down {
    display: none;
  }
}
</style>
