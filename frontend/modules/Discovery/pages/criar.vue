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
      :items="incubs"
    >
      <template v-slot:SecondaryButtons>
        <v-row justify="center">
          <v-col class="pt-0">
            <div class="d-flex flex-wrap justify-space-around">
              <v-btn
                v-for="{ label } in buttons"
                :key="label"
                class="white px-6 py-6 ma-1 flex-grow-1 button text-capitalize"
                :color="primary === label ? 'grey' : 'white'"
                max-width="100%"
                @click="selectFilter(label)"
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
      `Encontre uma rede de apoio para se conectar com outras startups e um local para instalar sua equipe em uma das 4 Incubadoras e do Parque Tecnológico da USP.`,
      `Há no estado de São Paulo uma diversificada rede de ambientes de inovação com aceleradoras, incubadoras e parques que também podem abrigar o seu negócio.`,
    ],
    buttons: [
      { label: "Incubadoras E Parques Técnologicos Da USP" },
      { label: "Incubadoras E Parques Técnologicos Externos" },
    ],
    primary: "",
    incubs: [],
  }),

  computed: {
    params() {
      if (this.primary === undefined) return {};

      return { insideUSP: this.primary === this.buttons[0].label };
    },
  },

  watch: {
    async params() {
      this.incubs = await this.$JornadaAdapter.updateCreate(this.params);
    },
  },

  async beforeMount() {
    this.incubs = await this.$JornadaAdapter.requestCreate();
  },

  methods: {
    selectFilter(label) {
      this.primary = this.primary === label ? undefined : label;
    },
  },
};
</script>
