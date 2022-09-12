<template>
  <div>
    <Step
      title="Aprenda"
      :description="description"
      color="#C0161B"
      :buttons="buttons"
      next="pratica"
      next-color="#E46926"
      :items="disciplines"
    >
      <template v-slot:PrimaryButtons>
        <v-row justify="center">
          <v-col cols="6">
            <div class="d-flex flex-wrap justify-space-around">
              <v-btn
                v-for="{ label } in buttons.primary"
                :key="label"
                class="white px-6 py-8 ma-1 text-10 flex-grow-1 button"
                :color="primary === label ? 'grey' : 'white'"
                max-width="100%"
                @click="setPrimary(label)"
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
                :color="secondary === label ? 'grey' : 'white'"
                max-width="100%"
                @click="setSecondary(label)"
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
import Step from "../components/Step.vue";

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
      primary: [{ label: "Graduação" }, { label: "Pós-graduação" }],
      secondary: [
        { label: "Quero aprender!" },
        { label: "Tenho uma ideia, e agora?" },
        { label: "Preciso testar minha ideia!" },
        { label: "Tópicos avançados em Empreendedorismo" },
      ],
    },

    disciplines: [],
    primary: undefined,
    secondary: undefined,
  }),

  computed: {
    params() {
      return {
        nature: this.primary,
        level: this.secondary,
      };
    },
  },

  watch: {
    async params() {
      this.disciplines = await this.$JornadaAdapter.updateLearn(this.params);
    },
  },

  async beforeMount() {
    this.disciplines = await this.$JornadaAdapter.requestLearn();
  },

  methods: {
    setPrimary(label) {
      this.primary = this.primary === label ? undefined : label;
    },

    setSecondary(label) {
      this.secondary = this.secondary === label ? undefined : label;
    },
  },
};
</script>
