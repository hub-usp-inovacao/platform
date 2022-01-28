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
            <ShortTextInput v-model="formData.name" label="Nome completo" />
            <ShortTextInput
              v-model="formData.email"
              label="Email"
              hint="Este dado não será publicado."
            />
            <ShortTextInput
              v-model="formData.phone"
              class="mt-5"
              label="Telefone"
            />
            <NumberInput
              v-model="formData.nusp"
              label="Qual o número USP?"
              hint="Insira o seu Nº USP, caso se recorde do mesmo. Se não possui um Nº USP, deixe o campo em branco."
            />
            <Dropdown
              v-model="formData.bond"
              label="Qual tipo de vínculo já possuiu ou ainda mantém com a USP?"
              :options="bonds"
            />
            <Dropdown
              v-model="formData.unity"
              label="Com qual instituto, escola ou centro é o vínculo atual ou mais recente?"
              :options="unities"
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
import ShortTextInput from "@/components/CompanyForms/inputs/ShortTextInput.vue";
import NumberInput from "@/components/CompanyForms/inputs/NumberInput.vue";
import Dropdown from "@/components/CompanyForms/inputs/Dropdown.vue";

export default {
  components: { ShortTextInput, NumberInput, Dropdown },
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
    },
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
        ? Object.assign({}, { ...this.updatedPartner.partner, phone: "" })
        : {
            name: "",
            email: "",
            phone: "",
            nusp: "",
            bond: "",
            unity: "",
          };
    },
  },
  methods: {
    savePartnerPipeline() {
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
      this.$emit("close");
    },
  },
};
</script>
