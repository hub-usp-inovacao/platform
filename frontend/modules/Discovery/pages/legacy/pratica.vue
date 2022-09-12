<template>
  <div>
    <Step
      title="A Ideia na Prática"
      :description="description"
      color="#E46926"
      :buttons="buttons"
      next="criar"
      previous="aprenda"
      next-color="#F4C41E"
      previous-color="#C0161B"
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
      `Quer encontrar uma galera que esteja a fim de apoiar e trabalhar nas mesmas causas que você? Busque a entidade estudantil com a qual você mais se identifica e junte-se a essa turma!`,
      `Quer trabalhar com projetos ligados à inovação e empreendedorismo e ao mesmo tempo iniciar a sua carreira? As Empresas Júnior são associações de alunos que prestam diferentes tipos de serviços, permitindo o desenvolvimento de competências e contato com o mundo dos negócios durante a graduação.`,
      `Na hora de colocar a sua ideia em prática, você pode participar de programas que inspiram e promovem o desenvolvimento de iniciativas inovadoras e empreendedoras disponíveis para a Comunidade USP.`,
      `Precisando de um lugar para colocar a sua ideia em prática? Procure os espaços de convivência e coworking, onde você poderá interagir com projetos e organizações ligadas à inovação e ao empreendedorismo`,
    ],
    route: "pratica",
    buttons: [
      { label: "Empresa Jr." },
      { label: "Ideação" },
      { label: "Grupos e Iniciativas Estudantis" },
      { label: "Entidade Estudantil" },
      { label: "Espaço/coworking" },
    ],
    selected: "",

    items: {
      "EMPRESA JR.": [],
      IDEAÇÃO: [],
      "GRUPOS E INICIATIVAS ESTUDANTIS": [],
      "ENTIDADE ESTUDANTIL": [],
      "ESPAÇO/COWORKING": [],
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
    const sheetID = "1MGRBDs-Bb2PGdyUkTN92dM5kqQuw5dtOpFHwAV1FQpA";
    const sheetName = "INICIATIVAS";
    const sheetsAPIKey = process.env.sheetsAPIKey;

    let empresaJr = [];
    let ideacao = [];
    let grupos = [];
    let entidade = [];
    let coworking = [];

    const resp = await fetch(
      `https://sheets.googleapis.com/v4/spreadsheets/${sheetID}/values/'${sheetName}'?key=${sheetsAPIKey}`
    );
    const { values } = await resp.json();

    values.slice(1).forEach((element) => {
      const name = element[0];
      let newUrl = formatURL(element[6]);

      switch (name) {
        case "Empresa Jr.":
          empresaJr.push({ nome: element[1], url: newUrl });
          break;
        case "Ideação":
          ideacao.push({ nome: element[1], url: newUrl });
          break;
        case "Grupos e Iniciativas Estudantis":
          grupos.push({ nome: element[1], url: newUrl });
          break;
        case "Entidade Estudantil":
          entidade.push({ nome: element[1], url: newUrl });
          break;
        case "Espaço/Coworking":
          coworking.push({ nome: element[1], url: newUrl });
          break;
      }
    });

    this.items["EMPRESA JR."] = empresaJr;
    this.items["IDEAÇÃO"] = ideacao;
    this.items["GRUPOS E INICIATIVAS ESTUDANTIS"] = grupos;
    this.items["ENTIDADE ESTUDANTIL"] = entidade;
    this.items["ESPAÇO/COWORKING"] = coworking;
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

<style></style>
