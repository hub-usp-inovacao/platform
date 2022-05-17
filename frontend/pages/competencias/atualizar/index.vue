<template>
  <section>
      <div class="background" >
          <Panel :title="title" :description="description()" no-search />
      </div>

      <v-form>
          <v-container>
              <v-text-field
              v-model="this.skill.nome"
              label="Nome"
              >
              </v-text-field>
          </v-container>
      </v-form>
  </section>
</template>

<script>

import Panel from "@/components/first_level/Panel.vue";

export default {

    components: {
        Panel,
    },
    data: () => ({
        title: "Atualização de Competências",
        Nome: "",
        skill: {
            nome: "",
        }
    }),

    methods: {
        description() {
            return "Nesta página os pesquisadores da USP cadastrados no HUBUSP INOVAÇÃO, podem pedir alteração de seus dados."
        },
    }

}


</script>