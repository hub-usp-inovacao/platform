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
  const pattern = /((http(s)?:\/\/.)?(www\.)?[-a-zA-Z0-9@:%._+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_+.~#?&//=]*)|N\/D)/g;
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
      if (validURL(url)) {
        this.isValid = true;
        this.$emit("input", url);
      } else if (this.isValid) {
        this.isValid = false;
        this.$emit("input", undefined);
      }
    },
  },
};
</script>
