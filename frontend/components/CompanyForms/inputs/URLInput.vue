<template>
  <v-text-field
    :label="label"
    :value="value"
    :rules="[rules.url]"
    :hint="hint"
    persistent-hint
    clearable
    @input="handleInput"
  >
  </v-text-field>
</template>

<script>
function validURL(str) {
  const protocol = "http(s)?://";
  const alphanumeric = "a-zA-Z0-9";
  const label = `[${alphanumeric}]([${alphanumeric}-]*[${alphanumeric}])?`;
  const port = ":[0-9]+";
  const path = "[/?].*";

  const pattern = new RegExp(
    `^(${protocol})?(${label}\\.)+${label}(${port})?(${path})?$`,
  );

  return !str?.includes(" ") && pattern.test(str);
}

export default {
  components: {},
  props: {
    label: {
      type: String,
      required: true,
    },
    value: {
      type: String,
      required: false,
      default: undefined,
    },
    hint: {
      type: String,
      required: false,
      default: () => "",
    },
  },
  data: () => ({
    isValid: false,
    rules: {
      url: (value) => validURL(value) || "URL inválida.",
    },
  }),
  methods: {
    handleInput(url) {
      this.isValid = validURL(url);
      this.$emit("input", url);
    },
  },
};
</script>
