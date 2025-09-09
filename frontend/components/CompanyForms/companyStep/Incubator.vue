<template>
  <v-container>
    <h2 class="text-h6 mt-6 font-weight-regular">
      A empresa está ou esteve em alguma incubadora ou Parque tecnológico? *
    </h2>
    <Dropdown
      :value="incubated"
      :options="options"
      label=""
      @input="setIncubated"
      :required="true"
      ref="incubatedInput"
    />

    <div v-if="!disabledIncubatorsSelect">
      <h2 class="text-h6 mt-6 font-weight-regular">
        Se sim, em qual incubadora ou Parque Tecnológico? *
      </h2>
      <Dropdown
        :value="selectedIncubator"
        :options="incubadorasWithOther"
        :disabled="disabledIncubatorsSelect"
        label=""
        @input="handleIncubatorSelection"
        :required="true"
        ref="incubatorInput"
      />
      <div v-if="selectedIncubator === 'Outros'" class="mt-5 text-h6 font-weight-regular">
        Especifique a incubadora/parque:
        <v-divider />
        <v-container>
          <TextInputFormatted
            :value="otherIncubator"
            label="Incubadora/Parque Tecnológico *"
            capitalization="title"
            :required="true"
            @input="handleOtherIncubatorInput"
            ref="otherIncubatorInput"
          />
        </v-container>
      </div>
    </div>
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import Dropdown from "@/components/CompanyForms/inputs/Dropdown.vue";
import TextInputFormatted from "@/components/CompanyForms/inputs/TextInputFormatted.vue";

export default {
  components: {
    Dropdown,
    TextInputFormatted,
  },
  data: () => ({
    options: [
      "Não",
      "Sim. A empresa está incubada",
      "Sim. A empresa já está graduada",
    ],
    incubadoras: [
      "Incubadora USP/IPEN",
      "ESALQTec - Incubadora de Empresas Agrozootécnicas de Piracicaba",
      "HABITs - Habitat de Inovação Tecnológica e Social/Incubadora-Escola",
      "ParqTec - Fundação Parque Tecnológico de São Carlos",
      "Supera - Parque de Inovação e Tecnologia de Ribeirão Preto",
    ],
  }),
  computed: {
    ...mapGetters({
      incubated: "company_forms/incubated",
      ecosystems: "company_forms/ecosystems",
    }),
    disabledIncubatorsSelect() {
      if (this.incubated === "Não") {
        this.setEcosystems([]);
        return true;
      }
      return false;
    },
    incubadorasWithOther() {
      return [...this.incubadoras, "Outros"];
    },
    selectedIncubator() {
      if (!this.ecosystems || this.ecosystems.length === 0) {
        return "";
      }

      const ecosystem = this.ecosystems[0];
      if (this.incubadoras.includes(ecosystem)) {
        return ecosystem;
      } else {
        return "Outros";
      }
    },
    otherIncubator() {
      if (!this.ecosystems || this.ecosystems.length === 0) {
        return "";
      }

      const ecosystem = this.ecosystems[0];
      if (!this.incubadoras.includes(ecosystem)) {
        return ecosystem;
      }
      return "";
    },
  },
  methods: {
    ...mapActions({
      setIncubated: "company_forms/setIncubated",
      setEcosystems: "company_forms/setEcosystems",
    }),

    handleIncubatorSelection(incubator) {
      if (incubator && incubator !== "Outros") {
        this.setEcosystems([incubator]);
      } else if (incubator === "Outros") {
        const currentOther = this.otherIncubator;
        if (currentOther) {
          this.setEcosystems([currentOther]);
        } else {
          this.setEcosystems([]);
        }
      }
    },

    handleOtherIncubatorInput(value) {
      if (this.selectedIncubator === "Outros") {
        this.setEcosystems(value ? [value] : []);
      }
    },

    validateStep() {
      const errors = [];

      if (!this.incubated) {
        errors.push('É obrigatório informar se a empresa está/esteve em incubadora');
      }

      if (this.incubated && this.incubated !== "Não") {
        if (!this.selectedIncubator) {
          errors.push('É obrigatório especificar qual incubadora/parque tecnológico');
        } else if (this.selectedIncubator === "Outros" && (!this.otherIncubator || this.otherIncubator.trim() === "")) {
          errors.push('É obrigatório especificar o nome da incubadora/parque quando selecionado "Outros"');
        }
      }

      return {
        isValid: errors.length === 0,
        errors: errors
      };
    },
  },
};
</script>
