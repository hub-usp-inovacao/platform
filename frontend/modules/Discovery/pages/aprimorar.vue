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
      :items="pdis"
    >
      <template v-slot:SecondaryButtons>
        <v-row justify="center">
          <v-col class="pt-0">
            <div class="d-flex flex-wrap justify-space-around">
              <v-btn
                v-for="{ label } in buttons"
                :key="label"
                class="white px-6 py-6 ma-1 flex-grow-1 button text-capitalize"
                :color="filter === label ? 'grey' : 'white'"
                max-width="100%"
                @click="setFilter(label)"
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
      `Se você está em processo de concepção e desenvolvimento da sua tecnologia, pode desenvolver parcerias com os inúmeros INCT e CEPID , a partir das quais será possível aprimorar a sua ideia em conjunto com pesquisadores da USP e desenvolver uma tecnologia que seja protegida por uma patente ou registro de software.`,
      `É possível contar com a ajuda de especialistas e suas competências no momento de desenvolver o seu produto ou serviço. Você pode consultar quais as competências da(o)s pesquisadora(e)s da USP, os serviços e equipamentos disponíveis em seus laboratórios.`,
      `Se você precisa finalizar o desenvolvimento do seu produto ou testá-lo, ou então está em busca por serviços especializados e equipamentos de alta complexidade, verifique as Centrais Multiusuário disponíveis na USP. `,
    ],
    buttons: [{ label: "CEPID" }, { label: "INCT" }],

    pdis: [],
    filter: undefined,
  }),

  computed: {
    params() {
      return {
        category: this.filter,
      };
    },
  },

  watch: {
    async params() {
      this.pdis = await this.$JornadaAdapter.updateImprove(this.params);
    },
  },

  async beforeMount() {
    this.pdis = await this.$JornadaAdapter.requestImprove();
  },

  methods: {
    setFilter(label) {
      this.filter = this.filter === label ? undefined : label;
    },
  },
};
</script>
