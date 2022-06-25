<template>
  <v-container>
    <v-row class="fill-height ma-0" justify="center" align="center">
      <p v-if="size > 0" class="title font-weight-light text-center mb-0">
        Selecione um item na lista ao lado
      </p>
      <p v-else class="title font-weight-light text-center mb-0">
        Não encontramos nada relacionado à sua pesquisa
      </p>
    </v-row>
  </v-container>
</template>

<script>
export default {
  props: {
    size: {
      type: Number,
      required: true,
    },
  },
};
</script>
