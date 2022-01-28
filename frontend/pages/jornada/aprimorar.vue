<template>
  <div>
    <Step
      title="Aprimorar o Negócio"
      :description="description"
      color="#338C21"
      :buttons="buttons"
      next="financiamento"
      previous="criar"
      next-color="#214E8C"
      previous-color="#F4C41E"
      :items="filteredItems"
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
      `Se você está em processo de concepção e desenvolvimento da sua tecnologia, pode desenvolver parcerias com os inúmeros INCT e CEPID , a partir das quais será possível aprimorar a sua ideia em conjunto com pesquisadores da USP e desenvolver uma tecnologia que seja protegida por uma patente ou registro de software.`,
      `É possível contar com a ajuda de especialistas e suas competências no momento de desenvolver o seu produto ou serviço. Você pode consultar quais as competências da(o)s pesquisadora(e)s da USP, os serviços e equipamentos disponíveis em seus laboratórios.`,
      `Se você precisa finalizar o desenvolvimento do seu produto ou testá-lo, ou então está em busca por serviços especializados e equipamentos de alta complexidade, verifique as Centrais Multiusuário disponíveis na USP. `,
    ],
    buttons: [
      { label: "CEPID" },
      { label: "INCT" },
      { label: "Centrais Multiusuário" },
    ],
    selected: "",

    items: {
      CEPID: [],
      INCT: [],
      "CENTRAIS MULTIUSUÁRIO": [],
    },

    selectedButtonSecondary: undefined,
  }),

  computed: {
    filteredItems() {
      const secondaryButton = this.selectedButtonSecondary;

      if (secondaryButton != undefined) {
        return this.items[secondaryButton];
      } else {
        return [];
      }
    },
  },

  async beforeMount() {
    const sheetID = "1TZWMGvvn6TUmwo8DdWvtkLcbDVqVuif9HKMRPVcb2eo";
    const sheetName = "PDI";
    const sheetsAPIKey = process.env.sheetsAPIKey;

    let cepid = [];
    let inct = [];
    let uspmulti = [];

    const centraisUrl = "https://uspmulti.prp.usp.br/api/public/centrais";

    const response = await fetch(centraisUrl);
    let centraisData = await response.json();

    centraisData.forEach((element) => {
      const { nome, sigla, site } = element;

      const name = `${sigla} - ${nome}`;

      uspmulti.push({ nome: name, url: site });
    });

    const resp = await fetch(
      `https://sheets.googleapis.com/v4/spreadsheets/${sheetID}/values/'${sheetName}'?key=${sheetsAPIKey}`
    );
    const { values } = await resp.json();

    values.slice(1).forEach((element) => {
      const name = element[0];

      let newUrl = formatURL(element[6]);

      if (name == "CEPID") {
        cepid.push({ nome: element[1], url: newUrl });
      } else if (name == "INCT") {
        inct.push({ nome: element[1], url: newUrl });
      }
    });

    this.items["CEPID"] = cepid;
    this.items["INCT"] = inct;
    this.items["CENTRAIS MULTIUSUÁRIO"] = uspmulti;
  },

  methods: {
    select({ target }) {
      let text = target.innerText;
      this.selected = text;
      text = text.toUpperCase();
      this.selectedButtonSecondary = text;
    },
  },
};
</script>
