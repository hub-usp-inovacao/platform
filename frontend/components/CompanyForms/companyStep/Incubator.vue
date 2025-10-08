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
    />
    <div v-if="!disabledIncubatorsSelect">
      <h2 class="text-h6 mt-6 font-weight-regular">
        Se sim, em qual incubadora ou Parque Tecnológico? *
      </h2>
      <Dropdown
        :value="defaultIncubators"
        :options="incubadoras"
        :disabled="disabledIncubatorsSelect"
        label=""
        @input="setDefaultIncubators"
        :multipleOption="true"
      />
      <div class="mt-5 text-h6 font-weight-regular">
        Em caso de outros, adicione abaixo:
        <v-divider />
        <v-container>
          <MultipleInputs
            :value="otherIncubator"
            label="Incubadora/Parque Tecnológico"
            @input="setOtherIncubators"
          />
        </v-container>
      </div>
    </div>
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import Dropdown from "@/components/CompanyForms/inputs/Dropdown.vue";
import ShortTextInput from "@/components/CompanyForms/inputs/ShortTextInput.vue";
import MultipleInputs from "@/components/CompanyForms/inputs/MultipleInputs.vue";

export default {
  components: {
    Dropdown,
    ShortTextInput,
    MultipleInputs,
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
      "Supera - Parque de Inovação e Tecnologia de Ribeirão Preto"
    ],
  }),
  computed: {
    ...mapGetters({
      incubated: "company_forms/incubated",
      ecosystems: "company_forms/ecosystems",
    }),
    disabledIncubatorsSelect() {
      if (this.incubated === "Não") {
        this.setEcosystems("");
        return true;
      }
      return false;
    },
    defaultIncubators() {
      const ecosystems = Array.isArray(this.ecosystems) ? this.ecosystems : [this.ecosystems];
      const matchingIncubators = ecosystems.filter(ecosystem => this.incubadoras.includes(ecosystem));
      return matchingIncubators;
    },
    otherIncubator() {
      const ecosystems = Array.isArray(this.ecosystems) ? this.ecosystems : [this.ecosystems];
      const matchingOtherIncubators = ecosystems.filter(ecosystem => !this.incubadoras.includes(ecosystem));
      return matchingOtherIncubators;
    },
  },
  methods: {
    ...mapActions({
      setIncubated: "company_forms/setIncubated",
      setEcosystems: "company_forms/setEcosystems",
    }),
    setDefaultIncubators(incubator) {
      this.setEcosystems(incubator);
    },
    setOtherIncubators(other) {
      this.setEcosystems(other);
    },
  },
};
</script>
