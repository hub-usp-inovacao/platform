<template>
  <v-app>
    <HubNavButton
      v-if="path != '/'"
      class="custom-hidden-sm-and-down"
      :margin="path == '/contato' ? false : true"
      :background="path == '/contato' ? false : true"
    />
    <v-toolbar
      class="custom-hidden-sm-and-down d-flex justify-end"
      color="transparent"
      width="75%"
      style="margin-left: 25%"
      absolute
      flat
    >
      <v-toolbar-items>
        <v-chip-group>
          <v-chip
            v-for="(item, i) in items"
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

    <v-app-bar class="custom-hidden-md-and-up" color="white" dense flat fixed>
      <v-app-bar-nav-icon @click="drawer = true"></v-app-bar-nav-icon>
    </v-app-bar>

    <v-navigation-drawer v-model="drawer" fixed temporary>
      <v-list nav dense>
        <v-list-item-group v-model="group">
          <v-list-item to="/">
            <v-list-item-title> Home </v-list-item-title>
          </v-list-item>
          <v-list-item v-for="(item, i) in items" :key="i" :to="item.to">
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
      <v-snackbar
        :multi-line="true"
        :value="snackbar"
        :timeout="-1"
        :vertical="true"
      >
        Como está sendo sua experiência com o Hub USPInovação?
        <div align="center" class="mt-3">
          <v-btn
            class="mr-8"
            href="https://forms.gle/TiEe6zYq9GsYgDww5"
            target="_blank"
            >Relate aqui</v-btn
          >
          <v-btn @click="snackbar = false"> Fechar </v-btn>
        </div>
      </v-snackbar>
      <nuxt />
    </v-main>
    <Footer />
  </v-app>
</template>

<script>
import HubNavButton from "@/components/first_level/HubNavButton.vue";
import Footer from "@/components/layout/Footer.vue";

export default {
  components: {
    HubNavButton,
    Footer,
  },
  data: () => ({
    activeItem: 0,
    snackbar: true,
    items: [
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
        title: "Empresas",
        to: "/empresas",
      },
      {
        title: "Patentes",
        to: "/patentes",
      },
      {
        title: "Jornada",
        to: "/jornada",
        new: true,
      },
      {
        title: "Contato",
        to: "/contato",
      },
    ],
    drawer: false,
    group: "",
  }),
  computed: {
    path() {
      return this.$route.path;
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
