<template>
  <div>
    <v-row v-for="i in counter" :key="i" align="center">
      <v-col cols="10">
        <component
          :is="component"
          :value="value[i - 1]"
          clearable
          :label="displayLabel(i)"
          :mask="mask"
          :rule="rule"
          @input="changeItem($event, i - 1)"
        ></component>
      </v-col>
      <v-col cols="2" align="center">
        <v-btn icon @click="removeItem(i)">
          <v-icon>mdi-delete</v-icon>
        </v-btn>
      </v-col>
    </v-row>
    <v-btn color="primary" rounded @click="newItem"
      >Adicionar {{ inputLabel }}</v-btn
    >
  </div>
</template>

<script>
import ShortTextInput from "@/components/CompanyForms/inputs/ShortTextInput.vue";
import MaskInput from "@/components/CompanyForms/inputs/MaskInput.vue";
import URLInput from "@/components/CompanyForms/inputs/URLInput.vue";

export default {
  components: {
    ShortTextInput,
    MaskInput,
    URLInput,
  },
  props: {
    inputLabel: {
      type: String,
      required: true,
    },
    component: {
      type: String,
      required: false,
      default: () => "ShortTextInput",
    },
    value: {
      type: Array,
      default: () => [],
    },
    rule: {
      type: RegExp,
      required: false,
      default: () => /.*/,
    },
    mask: {
      type: String,
      default: "",
    },
  },
  computed: {
    counter() {
      return this.value.length;
    },
  },
  methods: {
    displayLabel(i) {
      return `${this.inputLabel} ${i}`;
    },
    newItem() {
      const items = [];
      Object.assign(items, this.value);
      items.push("");
      this.$emit("input", items);
    },
    changeItem(val, pos) {
      const items = [];
      Object.assign(items, this.value);
      items[pos] = val;
      this.$emit("input", items);
    },
    removeItem(i) {
      const end = this.value.length;
      const head = this.value.slice(0, i - 1);
      const tail = this.value.slice(i, end);
      const items = head.concat(tail);
      this.$emit("input", items);
    },
  },
};
</script>
