<template>
  <v-app>
    <v-container mt-12>
      <v-row justify="center" align-content="space-around" class="mx-0">
        <v-col cols="12" md="5">
          <v-row justify="center">
            <img
              :src="require('@/vectors/home_dna_usp.svg')"
              style="max-width: 100%; max-height: 100%; object-fit: contain"
            />
          </v-row>
        </v-col>

        <v-col cols="12" md="7">
          <p
            :class="$breakpoint.smAndDown ? 'headline' : 'display-1'"
            class="font-weight-medium mb-0"
          >
            Empresas com DNA USP:
          </p>

          <p
            :class="$breakpoint.smAndDown ? 'headline' : 'display-1'"
            class="font-weight-medium mb-8"
          >
            Resultados de uma Universidade Inovadora e Empreendedora
          </p>

          <p
            :class="$breakpoint.smAndDown ? 'body-1' : 'title'"
            class="font-weight-light"
          >
            A marca DNA USP foi criada para identificar empresas constituídas
            por alunos, ex-alunos e pesquisadores que passaram pela Universidade
            de São Paulo ao longo dos anos, bem como empresas resultantes de
            processos de incubação ou de aceleração em alguma das incubadoras
            associadas à USP.
          </p>
        </v-col>
      </v-row>

      <v-row justify="center" class="mx-0">
        <v-col>
          <CompaniesCarousel />
        </v-col>
      </v-row>
    </v-container>
  </v-app>
</template>

<script>
import CompaniesCarousel from "@/components/second_level/CompaniesCarousel.vue";

export default {
  components: {
    CompaniesCarousel,
  },
};
</script>

<style></style>
