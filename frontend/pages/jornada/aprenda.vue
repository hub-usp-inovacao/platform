<template>
  <div>
    <Step
      title="Aprenda"
      :description="description"
      color="#C0161B"
      :buttons="buttons"
      next="pratica"
      next-color="#E46926"
      :items="filteredDisciplines"
    >
      <template v-slot:PrimaryButtons>
        <v-row justify="center">
          <v-col cols="6">
            <div class="d-flex flex-wrap justify-space-around">
              <v-btn
                v-for="{ label } in buttons.primary"
                :key="label"
                class="white px-6 py-8 ma-1 text-10 flex-grow-1 button text-capitalize"
                :color="buttons.PrimarySelected == label ? 'grey' : 'white'"
                max-width="100%"
                @click="select"
              >
                {{ label }}
              </v-btn>
            </div>
          </v-col>
        </v-row>
      </template>
      <template v-slot:SecondaryButtons>
        <v-row justify="center">
          <v-col class="pt-0">
            <div class="d-flex flex-wrap justify-space-around">
              <v-btn
                v-for="{ label } in buttons.secondary"
                :key="label"
                class="white px-6 py-6 ma-1 flex-grow-1 button text-capitalize"
                :color="buttons.SecondarySelected == label ? 'grey' : 'white'"
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
      `Para aprender um pouco mais sobre inovação e empreendedorismo, curse as disciplinas com potencial de geração de ideias, projetos, produtos e tecnologia relacionadas a novos negócios, oferecidas por diferentes institutos. Hoje a USP tem em sua grade cerca de 110 disciplinas de graduação e 90 disciplinas de pós-graduação voltadas para os temas de inovação e empreendedorismo.`,
      `Utilize os filtros acima para navegar pelas disciplinas de acordo com o nível que você está buscando.`,
    ],
    buttons: {
      primary: [{ label: "Graduação" }, { label: "Pós-Graduação" }],
      secondary: [
        { label: "Quero Aprender!" },
        { label: "Tenho Uma Ideia, E Agora?" },
        { label: "Preciso Testar Minha Ideia!" },
        { label: "Tópicos Avançados Em Empreendedorismo" },
      ],
      PrimarySelected: "",
      SecondarySelected: "",
    },
    graduação: {
      "QUERO APRENDER!": [],
      "TENHO UMA IDEIA, E AGORA?": [],
      "PRECISO TESTAR MINHA IDEIA!": [],
      "TÓPICOS AVANÇADOS EM EMPREENDEDORISMO": [],
    },

    PósGraduação: {
      "QUERO APRENDER!": [],
      "TENHO UMA IDEIA, E AGORA?": [],
      "PRECISO TESTAR MINHA IDEIA!": [],
      "TÓPICOS AVANÇADOS EM EMPREENDEDORISMO": [],
    },

    selectedButtonPrimary: undefined,
    selectedButtonSecondary: undefined,
  }),

  computed: {
    filteredDisciplines() {
      const primaryButton = this.selectedButtonPrimary;
      const secondaryButton = this.selectedButtonSecondary;

      if (primaryButton != undefined) {
        if (secondaryButton == undefined) {
          let disciplines = [];

          if (primaryButton == "GRADUAÇÃO") {
            let keys = Object.keys(this.graduação);

            for (let key of keys) {
              disciplines = disciplines.concat(this.graduação[key]);
            }
          } else {
            let keys = Object.keys(this.PósGraduação);

            for (let key of keys) {
              disciplines = disciplines.concat(this.PósGraduação[key]);
            }
          }

          return disciplines.reduce((acc, item) => {
            return acc.includes(item) ? acc : acc.concat(item);
          }, []);
        } else {
          return primaryButton == "GRADUAÇÃO"
            ? this.graduação[secondaryButton]
            : this.PósGraduação[secondaryButton];
        }
      }

      return [];
    },
  },

  async beforeMount() {
    const sheetID = "1AsmtnS5kY1mhXhNJH5QsCyg_WDnkGtARYB4nMdhyFLs";
    const sheetName = "DISCIPLINAS";
    const sheetsAPIKey = process.env.sheetsAPIKey;

    let gradAprender = [];
    let gradIdeia = [];
    let gradTestar = [];
    let gradTopicos = [];

    let posAprender = [];
    let posIdeia = [];
    let posTestar = [];
    let posTopicos = [];

    const resp = await fetch(
      `https://sheets.googleapis.com/v4/spreadsheets/${sheetID}/values/'${sheetName}'?key=${sheetsAPIKey}`
    );
    const { values } = await resp.json();

    values.slice(1).forEach((element) => {
      let newUrl = formatURL(element[4]);

      if (element[0] == "Graduação") {
        switch (element[5]) {
          case "Quero aprender!":
            gradAprender.push({ nome: element[1], url: newUrl });
            break;
          case "Tenho uma ideia, e agora?":
            gradIdeia.push({ nome: element[1], url: newUrl });
            break;
          case "Preciso testar minha ideia!":
            gradTestar.push({ nome: element[1], url: newUrl });
            break;
          case "Tópicos avançados em Empreendedorismo":
            gradTopicos.push({ nome: element[1], url: newUrl });
            break;
        }
      } else if (element[0] == "Pós-graduação") {
        switch (element[5]) {
          case "Quero aprender!":
            posAprender.push({ nome: element[1], url: newUrl });
            break;
          case "Tenho uma ideia, e agora?":
            posIdeia.push({ nome: element[1], url: newUrl });
            break;
          case "Preciso testar minha ideia!":
            posTestar.push({ nome: element[1], url: newUrl });
            break;
          case "Tópicos avançados em Empreendedorismo":
            posTopicos.push({ nome: element[1], url: newUrl });
            break;
        }
      }
    });

    this.graduação["QUERO APRENDER!"] = gradAprender;
    this.graduação["TENHO UMA IDEIA, E AGORA?"] = gradIdeia;
    this.graduação["PRECISO TESTAR MINHA IDEIA!"] = gradTestar;
    this.graduação["TÓPICOS AVANÇADOS EM EMPREENDEDORISMO"] = gradTopicos;

    this.PósGraduação["QUERO APRENDER!"] = posAprender;
    this.PósGraduação["TENHO UMA IDEIA, E AGORA?"] = posIdeia;
    this.PósGraduação["PRECISO TESTAR MINHA IDEIA!"] = posTestar;
    this.PósGraduação["TÓPICOS AVANÇADOS EM EMPREENDEDORISMO"] = posTopicos;
  },

  methods: {
    select({ target }) {
      let text = target.innerText;

      if (text == "Graduação" || text == "Pós-Graduação") {
        this.buttons.PrimarySelected = text;
        this.selectedButtonPrimary = text;
      } else {
        this.buttons.SecondarySelected = text;
        text = text.toUpperCase();
        this.selectedButtonSecondary = text;
      }
    },
  },
};
</script>
