<template>
  <div>
    <Step
      title="Criar a Empresa"
      :description="description"
      color="#F4C41E"
      :buttons="buttons"
      next="aprimorar"
      previous="pratica"
      next-color="#338C21"
      previous-color="#E46926"
      :items="filteredIncubators"
    >
      <template v-slot:SecondaryButtons>
        <v-row justify="center">
          <v-col class="pt-0">
            <div class="d-flex flex-wrap justify-space-around">
              <v-btn
                v-for="{ label } in buttons"
                :key="label"
                class="white px-6 py-6 ma-1 flex-grow-1 button text-capitalize"
                :color="selected == label ? 'grey' : 'white'"
                max-width="100%"
                @click="select"
              >
                {{ label }}
              </v-btn>
            </div>
          </v-col>
        </v-row>
      </template>
    </Step>
  </div>
</template>

<script>
import Step from "@/components/journey/Step.vue";
import { formatURL } from "@/lib/format";

export default {
  components: {
    Step,
  },
  data: () => ({
    description: [
      `Encontre uma rede de apoio para se conectar com outras startups e um local para instalar sua equipe em uma das 4 Incubadoras e do Parque Tecnológico da USP.`,
      `Há no estado de São Paulo uma diversificada rede de ambientes de inovação com aceleradoras, incubadoras e parques que também podem abrigar o seu negócio.`,
    ],
    buttons: [
      { label: "Incubadoras E Parques Técnologicos Da USP" },
      { label: "Incubadoras E Parques Técnologicos Externos" },
    ],
    selected: "",
    incubadoras: {
      "INCUBADORAS E PARQUES TÉCNOLOGICOS DA USP": [],
      "INCUBADORAS E PARQUES TÉCNOLOGICOS EXTERNOS": [],
    },

    selectedButtonSecondary: undefined,
  }),

  computed: {
    filteredIncubators() {
      const secondaryButton = this.selectedButtonSecondary;

      if (secondaryButton != undefined) {
        return this.incubadoras[secondaryButton];
      } else {
        return [];
      }
    },
  },
  async beforeMount() {
    const sheetID = "1kS55eqf_xEfOExh1aEGSvjTFDbtCRWHYSEyh4f31nww";
    const sheetName = "Upload";
    const sheetsAPIKey = process.env.sheetsAPIKey;

    let incubadorasUsp = [];
    let incubadorasExternas = [];

    const resp = await fetch(
      `https://sheets.googleapis.com/v4/spreadsheets/${sheetID}/values/'${sheetName}'?key=${sheetsAPIKey}`
    );
    const { values } = await resp.json();

    values.slice(1).forEach((element) => {
      let newUrl = formatURL(element[3]);

      element[1] == "Outro"
        ? incubadorasExternas.push({ nome: element[2], url: newUrl })
        : incubadorasUsp.push({ nome: element[2], url: newUrl });
    });

    this.incubadoras[
      "INCUBADORAS E PARQUES TÉCNOLOGICOS DA USP"
    ] = incubadorasUsp;

    this.incubadoras[
      "INCUBADORAS E PARQUES TÉCNOLOGICOS EXTERNOS"
    ] = incubadorasExternas;
  },

  methods: {
    select({ target }) {
      let text = target.innerText;
      this.selected = text;
      console.log(this.selected);
      text = text.toUpperCase();
      this.selectedButtonSecondary = text;
    },
  },
};
</script>
