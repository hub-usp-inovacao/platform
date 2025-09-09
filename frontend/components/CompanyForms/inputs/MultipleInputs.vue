<template>
  <div>
    <div v-for="(item, index) in internalItems" :key="`item-${index}`" class="d-flex align-center mb-2">
      <component
        :is="inputComponent"
        :value="item"
        :label="`${inputLabel} ${index + 1}`"
        @input="updateItem(index, $event)"
        class="flex-grow-1 mr-2"
      />
      <v-btn
        icon
        small
        color="error"
        @click="removeItem(index)"
        :disabled="internalItems.length <= 1"
      >
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </div>

    <v-btn
      small
      color="primary"
      @click="addItem"
      :disabled="internalItems.length >= maxItems"
    >
      <v-icon left>mdi-plus</v-icon>
      Adicionar {{ inputLabel.toLowerCase() }}
    </v-btn>

    <div v-if="internalItems.length >= maxItems" class="caption text--secondary mt-2">
      Máximo de {{ maxItems }} itens permitidos
    </div>
  </div>
</template>

<script>
import TextInputFormatted from './TextInputFormatted.vue';
import PhoneInput from './PhoneInput.vue';
import EmailInput from './EmailInput.vue';

export default {
  components: {
    TextInputFormatted,
    PhoneInput,
    EmailInput,
  },
  props: {
    value: {
      type: Array,
      default: () => [],
    },
    inputLabel: {
      type: String,
      default: 'Item',
    },
    component: {
      type: String,
      default: 'TextInputFormatted',
    },
    maxItems: {
      type: Number,
      default: 10,
    },
  },
  data() {
    return {
      internalItems: []
    };
  },
  computed: {
    inputComponent() {
      const componentMap = {
        'TextInputFormatted': 'TextInputFormatted',
        'PhoneInput': 'PhoneInput',
        'EmailInput': 'EmailInput',
      };

      return componentMap[this.component] || 'TextInputFormatted';
    },
  },
  watch: {
    value: {
      handler(newValue) {
        if (Array.isArray(newValue) && newValue.length > 0) {
          this.internalItems = [...newValue];
        } else {
          this.internalItems = [''];
        }
      },
      immediate: true
    }
  },
  methods: {
    updateItem(index, value) {
      this.internalItems[index] = value;
      this.emitUpdate();
    },

    addItem() {
      if (this.internalItems.length < this.maxItems) {
        this.internalItems.push('');
        this.emitUpdate();
      }
    },

    removeItem(index) {
      if (this.internalItems.length > 1) {
        this.internalItems.splice(index, 1);
        this.emitUpdate();
      }
    },

    emitUpdate() {
      const nonEmptyItems = this.internalItems.filter(item => item && item.trim() !== '');
      this.$emit('input', nonEmptyItems);
    },

    isValid() {
      return this.internalItems.some(item => item && item.trim() !== '');
    },
  },
};
</script>
