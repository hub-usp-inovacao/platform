<template>
  <v-row justify="center">
    <v-dialog
      :value="value"
      persistent
      max-width="700px"
      @input="$emit('input', $event)"
    >
      <v-card>
        <v-card-title>
          <span class="text-h5">{{ modalTitle }}</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-alert
              v-if="validationErrors.length > 0"
              type="error"
              dismissible
              class="mb-4"
              @input="clearValidationErrors"
            >
              <strong>Corrija os seguintes erros:</strong>
              <ul class="mt-2">
                <li v-for="error in validationErrors" :key="error">{{ error }}</li>
              </ul>
            </v-alert>

            <TextInputFormatted
              v-model="formData.name"
              label="Nome completo *"
              capitalization="title"
              :required="true"
              :min-length="3"
              ref="nameInput"
            />
            <EmailInput
              v-model="formData.email"
              label="Email *"
              hint="Este dado não será publicado."
              :required="true"
              ref="emailInput"
            />
            <PhoneInput
              v-model="formData.phone"
              class="mt-5"
              label="Telefone *"
              :required="true"
              ref="phoneInput"
            />
            <NUSPInput
              v-model="formData.nusp"
              label="Qual o número USP?"
              hint="Insira o seu Nº USP, caso se recorde do mesmo. Se não possui um Nº USP, deixe o campo em branco."
              ref="nuspInput"
            />
            <Dropdown
              v-model="formData.bond"
              label="Qual tipo de vínculo já possuiu ou ainda mantém com a USP? *"
              :options="bonds"
              :required="true"
              ref="bondInput"
            />
            <Dropdown
              v-model="formData.unity"
              label="Com qual instituto, escola ou centro é o vínculo atual ou mais recente?"
              :options="unities"
              ref="unityInput"
            />
            <TextInputFormatted
              v-model="formData.position"
              class="mt-5"
              label="Cargo *"
              capitalization="title"
              :required="true"
              ref="roleInput"
            />
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary darken-1" text @click="closeDialog">
            Fechar
          </v-btn>
          <v-btn color="primary darken-1" text @click="savePartnerPipeline">
            Salvar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import TextInputFormatted from "@/components/CompanyForms/inputs/TextInputFormatted.vue";
import EmailInput from "@/components/CompanyForms/inputs/EmailInput.vue";
import PhoneInput from "@/components/CompanyForms/inputs/PhoneInput.vue";
import NUSPInput from "@/components/CompanyForms/inputs/NUSPInput.vue";
import Dropdown from "@/components/CompanyForms/inputs/Dropdown.vue";

export default {
  components: {
    TextInputFormatted,
    EmailInput,
    PhoneInput,
    NUSPInput,
    Dropdown
  },
  props: {
    value: {
      type: Boolean,
      required: true,
      default: () => false,
    },
    updatedPartner: {
      type: Object,
      required: false,
      default: () => undefined,
    },
  },
  data: () => ({
    formData: {
      name: "",
      email: "",
      phone: "",
      nusp: "",
      bond: "",
      unity: "",
      role: "",
    },
    validationErrors: [],
    bonds: [
      "Aluno ou ex-aluno (graduação)",
      "Aluno ou ex-aluno (pós-graduação)",
      "Aluno ou ex-aluno de pós-graduação do IPEN (Instituto de Pesquisas Energéticas e Nucleares)",
      "Docente aposentado / Licenciado",
      "Docente",
      "Pós-doutorando",
      "Pesquisador",
      "Empresa incubada ou graduada em incubadora associada à USP",
      "Nenhum",
    ],
  }),
  computed: {
    unities() {
      return this.$campi
        .reduce((acc, { unities }) => acc.concat(unities), [])
        .sort();
    },
    modalTitle() {
      return this.updatedPartner
        ? "Atualização do sócio"
        : "Dados do novo sócio";
    },
  },
  watch: {
    updatedPartner() {
      this.formData = this.updatedPartner
        ? Object.assign({}, { ...this.updatedPartner.partner })
        : {
            name: "",
            email: "",
            phone: "",
            nusp: "",
            bond: "",
            unity: "",
            position: "",
          };
    },
  },
  methods: {
    validateForm() {
      const errors = [];

      if (this.$refs.nameInput && !this.$refs.nameInput.isValid()) {
        errors.push('Nome é obrigatório (mínimo 3 caracteres)');
      }

      if (this.$refs.emailInput && !this.$refs.emailInput.isValid()) {
        errors.push('Email inválido ou não preenchido');
      }

      if (this.$refs.phoneInput && !this.$refs.phoneInput.isValid()) {
        errors.push('Telefone inválido ou não preenchido');
      }

      if (this.$refs.roleInput && !this.$refs.roleInput.isValid()) {
        errors.push('Cargo é obrigatório');
      }

      if (!this.formData.bond) {
        errors.push('Vínculo com a USP é obrigatório');
      }

      if (this.$refs.nuspInput && this.formData.nusp && !this.$refs.nuspInput.isValid()) {
        errors.push('NUSP inválido');
      }

      this.validationErrors = errors;

      return {
        isValid: errors.length === 0,
        errors: errors
      };
    },

    savePartnerPipeline() {
      const validation = this.validateForm();

      if (!validation.isValid) {
        return;
      }

      if (this.updatedPartner) {
        this.update();
      } else {
        this.save();
      }

      this.clearFormDataInputs();
      this.closeDialog();
    },
    update() {
      this.$emit("update", {
        index: this.updatedPartner.index,
        newPartner: Object.assign({}, this.formData),
      });
    },
    save() {
      this.$emit("save", Object.assign({}, this.formData));
    },
    clearFormDataInputs() {
      Object.keys(this.formData).forEach((key) => (this.formData[key] = ""));
    },
    closeDialog() {
      this.clearValidationErrors();
      this.$emit("close");
    },
    clearValidationErrors() {
      this.validationErrors = [];
    },
  },
};
</script>
