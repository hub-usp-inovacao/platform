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
      :items="records"
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
      `Quer encontrar uma galera que esteja a fim de apoiar e trabalhar nas mesmas causas que você? Busque a entidade estudantil com a qual você mais se identifica e junte-se a essa turma!`,
      `Quer trabalhar com projetos ligados à inovação e empreendedorismo e ao mesmo tempo iniciar a sua carreira? As Empresas Júnior são associações de alunos que prestam diferentes tipos de serviços, permitindo o desenvolvimento de competências e contato com o mundo dos negócios durante a graduação.`,
      `Na hora de colocar a sua ideia em prática, você pode participar de programas que inspiram e promovem o desenvolvimento de iniciativas inovadoras e empreendedoras disponíveis para a Comunidade USP.`,
      `Precisando de um lugar para colocar a sua ideia em prática? Procure os espaços de convivência e coworking, onde você poderá interagir com projetos e organizações ligadas à inovação e ao empreendedorismo`,
    ],

    buttons: [
      { label: "Empresa Jr." },
      { label: "Ideação" },
      { label: "Grupos e Iniciativas Estudantis" },
      { label: "Entidade Estudantil" },
      { label: "Espaço/coworking" },
    ],

    records: [],
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
      this.records = await this.$JornadaAdapter.updatePractice(this.params);
    },
  },

  async beforeMount() {
    this.records = await this.$JornadaAdapter.requestPractice();
  },

  methods: {
    setFilter(label) {
      this.filter = this.filter === label ? undefined : label;
    },
  },
};
</script>

<style></style>
