<template>
    <v-autocomplete
      :value="value"
      :clearable="clearable"
      :counter="counter"
      :label="label"
      :hint="hint"
      :items="items"
      hide-no-data
      persistent-hint
      @input="$emit('input', $event)"
      @change="$emit('change', $event)"
      :filter="customFilter"
    />
  </template>
  
  <script>
  export default {
    components: {},
    props: {
      clearable: {
        type: Boolean,
        required: false,
        default: true,
      },
      maxLength: {
        type: Number,
        required: false,
        default: -1,
      },
      value: {
        type: String,
      },
      label: {
        type: String,
        required: true,
      },
      hint: {
        type: String,
        required: false,
        default: () => "",
      },
      items: {
          type: Array,
          required: true,
      },
    },
  
    computed: {
      counter() {
        return this.maxLength <= 0 ? false : this.maxLength;
      },
    },

    methods: {
      customFilter(item, queryText, itemText) {
      // Remove a acentuação tanto do texto da opção quanto do texto de pesquisa
      const normalizedItemText = itemText.normalize("NFD").replace(/[\u0300-\u036f]/g, "");
      const normalizedQueryText = queryText.normalize("NFD").replace(/[\u0300-\u036f]/g, "");

      // Realiza a comparação insensível a acentos entre o texto da opção e o texto de pesquisa (colocando ambos em caixa baixa)
      return normalizedItemText.toLowerCase().includes(normalizedQueryText.toLowerCase());
      },
    },
  };
  </script>
  