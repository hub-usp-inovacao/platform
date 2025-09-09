<template>
  <v-container>
    <v-form>
      <div class="mt-5 text-h6 font-weight-regular">
        Informações Adicionais da Empresa
        <v-divider />
        <v-container>
          <v-alert
            border="top"
            colored-border
            type="info"
            elevation="2"
            color="secondary"
            class="mb-4"
          >
            Estas informações são utilizadas para classificação e análise estatística
          </v-alert>

          <Dropdown
            :value="companyType"
            label="Tipo de empresa"
            :options="companyTypeOptions"
            hint="Selecione o tipo que melhor descreve sua empresa"
            @input="setCompanyType"
          />

          <BooleanInput
            :value="isUnicorn"
            label="A empresa é considerada um unicórnio?"
            hint="Empresas unicórnio são startups avaliadas em mais de US$ 1 bilhão"
            @input="setIsUnicorn"
          />
        </v-container>
      </div>
    </v-form>
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import Dropdown from "@/components/CompanyForms/inputs/Dropdown.vue";
import BooleanInput from "@/components/CompanyForms/inputs/BooleanInput.vue";

export default {
  components: {
    Dropdown,
    BooleanInput,
  },
  data: () => ({
    companyTypeOptions: [
      "Startup",
      "Spin-off acadêmico",
      "Empresa tradicional",
      "Scale-up",
      "Empresa de base tecnológica",
      "Empresa de inovação",
      "Outros"
    ],
  }),
  computed: {
    ...mapGetters({
      companyType: "company_forms/companyType",
      isUnicorn: "company_forms/isUnicorn",
    }),
  },
  methods: {
    ...mapActions({
      setCompanyType: "company_forms/setCompanyType",
      setIsUnicorn: "company_forms/setIsUnicorn",
    }),
    validateStep() {
      const errors = [];

      return {
        isValid: errors.length === 0,
        errors: errors
      };
    },
  },
};
</script>
