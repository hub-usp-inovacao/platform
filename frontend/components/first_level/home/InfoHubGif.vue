<template>
  <v-app>
    <v-container fill-height fluid style="background-color: #3890e8">
      <v-container fill-height>
        <v-row class="ma-0 fill-height" align="center">
          <v-col cols="12" sm="7">
            <p
              :class="$breakpoint.smAndDown ? 'headline' : 'display-1'"
              class="font-weight-medium white--text mb-8"
            >
              Hub USPInovação: Encontre o que você procura
            </p>

            <p
              :class="$breakpoint.smAndDown ? 'body-1' : 'title'"
              class="white--text font-weight-light"
            >
              A Plataforma Hub USPInovação facilita a conexão entre a sociedade
              e a USP. Simples e intuitivo, basta digitar palavras-chave na
              ferramenta de busca para obter os contatos de instituições,
              estruturas, empresas e pesquisadores especialistas nos assuntos de
              seu interesse.
            </p>
          </v-col>

          <v-col cols="12" sm="5">
            <v-card flat tile width="100%" height="15rem" color="#38E8E2">
              <v-container fill-height>
                <v-row justify="center" class="ma-0">
                  <v-col cols="12">
                    <v-text-field
                      solo
                      flat
                      rounded
                      readonly
                      append-outer-icon="search"
                    >
                      <template v-slot:label>
                        <vue-typer :text="search_bar"></vue-typer>
                      </template>
                    </v-text-field>
                  </v-col>
                </v-row>
              </v-container>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-container>
  </v-app>
</template>

<script>
export default {
  data: () => ({
    search_bar: [
      "Buscar",
      "Biotecnologia",
      "Laboratórios",
      "Inteligência Artificial",
    ],
  }),
};
</script>

<style></style>
