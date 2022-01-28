<template>
  <div>
    <div class="mb-16 pb-16">
      <v-container fill-height>
        <v-container>
          <v-row class="ma-0 fill-height" align="center">
            <v-col cols="12" md="7">
              <p
                :class="$breakpoint.smAndDown ? 'headline' : 'display-1'"
                class="font-weight-medium mb-8"
              >
                AUSPIN: Agência USP de Inovação
              </p>

              <p
                :class="$breakpoint.smAndDown ? 'body-1' : 'title'"
                class="font-weight-light"
              >
                É responsável pelo registro e licenciamento de propriedade
                intelectual, pela transferência de tecnologias desenvolvidas na
                USP para empresas e organizações, e pelo fomento ao
                empreendedorismo junto a todas as unidades - faculdades,
                institutos, laboratórios - da USP. Com escritórios e
                representações que atendem todos os campi da USP, a AUSPIN está
                à sua disposição para facilitar seu contato com pesquisadores,
                laboratórios, empresas e estruturas relacionadas à inovação e ao
                empreendedorismo na Universidade de São Paulo.
              </p>
            </v-col>

            <v-col cols="12" md="5">
              <v-row justify="center">
                <img
                  :src="require('@/vectors/logo_auspin.svg')"
                  style="max-width: 100%; max-height: 100%; object-fit: contain"
                />
              </v-row>
            </v-col>
          </v-row>
        </v-container>
      </v-container>
    </div>

    <div class="mt-16 pt-16">
      <div class="hidden-md-and-up" style="height: 5vh"></div>
      <div class="hidden-sm-and-down" style="height: 30vh"></div>
      <BottomCurves class="absolute" style="bottom: 0" />
    </div>
  </div>
</template>

<script>
import BottomCurves from "@/components/second_level/BottomCurves.vue";

export default {
  components: {
    BottomCurves,
  },
};
</script>

<style></style>
