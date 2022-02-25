<template>
  <v-container>
    <h2 class="text-h6 mt-6 font-weight-regular">
      A empresa está ou esteve em alguma incubadora ou Parque tecnológico?
    </h2>
    <Dropdown
      :value="incubated"
      :options="options"
      label=""
      @input="setIncubated"
    />

    <div v-if="!disabledIncubatorsSelect">
      <h2 class="text-h6 mt-6 font-weight-regular">
        Se sim, em qual incubadora ou Parque Tecnológico?
      </h2>
      <Dropdown
        :value="defaultIncubators"
        :options="incubadoras"
        :disabled="disabledIncubatorsSelect"
        label=""
        @input="setDefaultIncubators"
      />
      <div class="mt-5 text-h6 font-weight-regular">
        Outros
        <v-divider />
        <v-container>
          <ShortTextInput
            :value="otherIncubator"
            input-label="Incubadora/Parque Tecnológico"
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

export default {
  components: {
    Dropdown,
    ShortTextInput,
  },
  data: () => ({
    options: [
      "Não",
      "Sim. A empresa está incubada",
      "Sim. A empresa já está graduada",
    ],
    incubadoras: [
      "CIETEC - Centro de Inovação, Empreendedorismo e Tecnologia",
      "ESALQTec - Incubadora de Empresas Agrozootécnicas de Piracicaba",
      "HABITs - Habitat de Inovação Tecnológica e Social/Incubadora-Escola",
      "Supera - Incubadora de Empresas de Base Tecnológica de Ribeirão Preto",
      "Supera Parque de Inovação e Tecnologia",
    ],
  }),
  computed: {
    ...mapGetters({
      incubated: "company_forms/incubated",
      ecosystems: "company_forms/ecosystems",
    }),
    disabledIncubatorsSelect() {
      return this.incubated === "Não";
    },
    defaultIncubators() {
      return this.incubadoras.includes(this.ecosystems) ? this.ecosystems : ''
    },
    otherIncubator() {
      return this.incubadoras.includes(this.ecosystems) ? '' : this.ecosystems
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
