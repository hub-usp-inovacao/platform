<template>
  <div>
    <!-- Land -->
    <Banner @search="submitSearch" />

    <!-- Um lugar só com... -->
    <InfoHubBulletList />

    <!-- USP: Um lugar para empreender... -->
    <InfoUSP />

    <!-- Hub USPInovação: Encontre o que voce... -->
    <InfoHubGif />

    <!-- DNA USP -->
    <InfoDNAUSP />

    <!-- AUSPIN -->
    <InfoAuspin />
  </div>
</template>

<script>
import Banner from "@/components/first_level/home/Banner.vue";
import InfoHubBulletList from "@/components/first_level/home/InfoHubBulletList.vue";
import InfoUSP from "@/components/first_level/home/InfoUSP.vue";
import InfoHubGif from "@/components/first_level/home/InfoHubGif.vue";
import InfoDNAUSP from "@/components/first_level/home/InfoDNAUSP.vue";
import InfoAuspin from "@/components/first_level/home/InfoAuspin.vue";

export default {
  components: {
    Banner,
    InfoHubBulletList,
    InfoUSP,
    InfoHubGif,
    InfoDNAUSP,
    InfoAuspin,
  },
  methods: {
    newGlobalSearch(searchTerm) {
      if (!searchTerm.trim()) {
        return;
      }
      this.$router.push({
        name: "busca",
        query: { q: searchTerm },
      });
    },
    disabledGlobalSearch() {
      alert("Este recurso está temporariamente indisponível.");
    },
    submitSearch(searchTerm) {
      if (process.env.NEW_CATALOG == "true") this.newGlobalSearch(searchTerm);
      else this.disabledGlobalSearch();
    },
  },
  head: {
    title: "Hub USPInovação",
    meta: [
      {
        name: "google-site-verification",
        content: "6_EEYtY0wjahRpeFmJZ7qcVSuABHdi4mV5wa8Y1FmlE",
      },
    ],
  },
};
</script>

<style>
.dark-blue-layer {
  background: url("~@/vectors/dark_blue_layer.svg");
  position: absolute;
  height: 234px;
  max-height: 100%;
  width: 1180px;
  max-width: 100%;
  bottom: 0;
  left: 0;
}
.light-blue-layer {
  background: url("~@/vectors/light_blue_layer.svg");
  position: absolute;
  height: 388px;
  max-height: 100%;
  width: 1276px;
  max-width: 100%;
  bottom: 0;
  left: 0;
}
</style>
