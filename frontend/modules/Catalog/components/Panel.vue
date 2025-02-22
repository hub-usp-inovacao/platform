<template>
  <v-container fill-height style="position: relative; min-height: 20rem">
    <v-row class="mt-5 px-1" align="center" justify="space-between">
      <v-col>
        <p class="display-2 font-weight-regular">{{ title }}</p>

        <p class="title font-weight-light" style="line-height: normal">
          {{ description }}
        </p>
        <p v-if="counter !== 0" class="title font-weight-light" style="line-height: normal">
          Quantidade atual de {{ title }} cadastradas: <b>{{ counter }}</b>
        </p>

        <p v-for="{ cURL, text } of callsToAction" :key="cURL">
          <v-btn
            rounded
            color="secondary"
            :href="cURL"
            target="_blank"
            :width="callToActionWidth"
            >{{ text }}</v-btn
          >
        </p>
      </v-col>

      <v-col v-if="!noSearch" cols="11" sm="5" align-self="end">
        <v-text-field
          id="search-bar"
          v-model="value"
          :background-color="searchBarColor"
          solo
          flat
          rounded
          clearable
          full-width
          color="tertiary"
          :label="'Buscar - ' + title"
          append-outer-icon="search"
          :style="setSearchBarWidth"
          :loading="loading"
          :hint="hint"
          persistent-hint
          @click:clear="clearSearch"
        ></v-text-field>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { debounce } from "debounce";
import { mapGetters } from "vuex";

export default {
  props: {
    title: {
      type: String,
      required: true,
    },
    description: {
      type: String,
      default: "",
    },
    url: {
      type: String,
      default: "",
    },
    formsCall: {
      type: String,
      default: "",
    },
    secondUrl: {
      type: String,
      default: "",
    },
    secondCall: {
      type: String,
      default: "",
    },
    thirdUrl: {
      type: String,
      default: "",
    },
    thirdCall: {
      type: String,
      default: "",
    },
    fourthUrl: {
      type: String,
      default: "",
    },
    fourthCall: {
      type: String,
      default: "",
    },
    loading: {
      type: Boolean,
      default: false,
    },
    searchBarColor: {
      type: String,
      default: "#88E3FF",
    },
    value: {
      type: String,
      required: false,
      default: "",
    },
    autoscroll: {
      type: Boolean,
      required: false,
      default: true,
    },
    noSearch: {
      type: Boolean,
      required: false,
      default: false,
    },
    counter: {
      type: Number,
      requered: false,
      default: 0
    }
  },
  computed: {
    ...mapGetters({
      strictSearchResults: "global/strictSearchResults",
    }),
    hint() {
      const strict = "Resultados de busca na caixa abaixo";
      const flexible = "Resultados aproximados de busca na caixa abaixo";

      return this.strictSearchResults ? strict : flexible;
    },
    setSearchBarWidth() {
      switch (this.$vuetify.breakpoint.name) {
        case "xs":
          return { width: "100%" };
        case "sm":
          return { width: "80%" };
        default:
          return { width: "100%" };
      }
    },
    hasCallToAction() {
      return this.url !== "" && this.formsCall != "";
    },
    hasSecondCallToAction() {
      return this.secondUrl !== "" && this.secondCall != "";
    },
    hasThirdCallToAction() {
      return this.thirdUrl !== "" && this.thirdCall != "";
    },
    hasFourthCallToAction() {
      return this.thirdUrl !== "" && this.thirdCall != "";
    },
    callToActionWidth() {
      return this.$breakpoint.smAndDown ? "100%" : "60%";
    },
    callsToAction() {
      const base = [];

      if (this.hasCallToAction) {
        base.push({
          cURL: this.url,
          text: this.formsCall,
        });
      }

      if (this.hasSecondCallToAction) {
        base.push({
          cURL: this.secondUrl,
          text: this.secondCall,
        });
      }

      if (this.hasThirdCallToAction) {
        base.push({
          cURL: this.thirdUrl,
          text: this.thirdCall,
        });
      }

      if (this.hasFourthCallToAction) {
        base.push({
          cURL: this.fourthUrl,
          text: this.fourthCall,
        });
      }

      return base;
    },
  },
  watch: {
    value: debounce(function () {
      this.$emit("input", this.value);
    }, 1000),
  },
  methods: {
    scroll(method) {
      if (this.autoscroll) this.$vuetify.goTo("#display-data").then(method);
      else method();
    },
    submitSearch() {
      this.$emit("search", this.value);
    },
    clearSearch() {
      this.$emit("clear");
    },
  },
};
</script>

<style scoped></style>
