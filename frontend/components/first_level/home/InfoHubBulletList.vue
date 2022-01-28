<template>
  <v-app style="background-color: #fcb415">
    <v-container fill-height>
      <v-row class="ma-0" align="center">
        <v-col cols="12" sm="7">
          <p
            :class="$breakpoint.smAndDown ? 'headline' : 'display-1'"
            class="white--text font-weight-medium"
          >
            Um lugar só com:
          </p>

          <v-row
            class="fill-height"
            :class="$breakpoint.smAndDown ? 'ml-2' : 'ml-6'"
            align-content="space-around"
          >
            <v-col cols="12">
              <p
                :class="$breakpoint.smAndDown ? 'body-1' : 'title'"
                class="white--text font-weight-light"
              >
                &bull; As diversas iniciativas e estruturas da USP;
              </p>
            </v-col>

            <v-col cols="12">
              <p
                :class="$breakpoint.smAndDown ? 'body-1' : 'title'"
                class="white--text font-weight-light"
              >
                &bull; Os pesquisadores e suas competências;
              </p>
            </v-col>

            <v-col cols="12">
              <p
                :class="$breakpoint.smAndDown ? 'body-1' : 'title'"
                class="white--text font-weight-light"
              >
                &bull; Todas as patentes que a USP detém e disponibiliza para
                parceria com empresas e outras instituições interessadas;
              </p>
            </v-col>

            <v-col cols="12">
              <p
                :class="$breakpoint.smAndDown ? 'body-1' : 'title'"
                class="white--text font-weight-light"
              >
                &bull; As disciplinas de graduação e pós-graduação sobre
                empreendedorismo e inovação;
              </p>
            </v-col>

            <v-col cols="12">
              <p
                :class="$breakpoint.smAndDown ? 'body-1' : 'title'"
                class="white--text font-weight-light"
              >
                &bull; As empresas com DNA USP, criadas a partir de iniciativas
                incubadas na USP ou que seus fundadores e criadores foram alunos
                ou pesquisadores da USP.
              </p>
            </v-col>
          </v-row>
        </v-col>

        <v-col align-self="end">
          <img
            :src="require('@/vectors/home_globo.svg')"
            style="max-height: 80vh; max-width: 100%; content-fit: contain"
          />
        </v-col>
      </v-row>
    </v-container>
  </v-app>
</template>

<script>
export default {};
</script>

<style></style>
