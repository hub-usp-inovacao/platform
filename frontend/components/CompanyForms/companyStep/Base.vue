<template>
  <v-container>
    <v-form ref="baseForm">
      <div class="mt-5 text-h6 font-weight-regular">
        Dados básicos
        <v-divider />
        <v-container>
          <TextInputFormatted
            class="mb-8"
            :value="name"
            label="Nome fantasia da empresa *"
            hint="A empresa será listada no Hub USPInovação a partir do nome fantasia da mesma."
            capitalization="title"
            :required="true"
            :min-length="3"
            @input="setName"
            ref="nameInput"
          />
          <TextInputFormatted
            class="mb-4"
            :value="corporateName"
            label="Razão social da empresa *"
            capitalization="title"
            :required="true"
            :min-length="3"
            @input="setCorporateName"
            ref="corporateNameInput"
          />
          <NumberInput
            class="mb-4"
            :value="year"
            label="Ano de fundação *"
            hint="Digite o ano de fundação da empresa"
            :required="true"
            :max-length="4"
            :min-value="1900"
            :max-value="2025"
            @input="setYear"
            ref="yearInput"
          />
          <CnpjInput
            :value="cnpj"
            label="CNPJ *"
            :required="true"
            @input="setCnpj"
            :disabled="isUpdating"
            ref="cnpjInput"
          />
          <Dropdown
            :value="registry_status"
            label="Situação cadastral"
            :options="allRegistryStatus"
            @input="setRegistryStatus"
            ref="registryStatusInput"
          />
          <Dropdown
            :value="size"
            :options="sizeList"
            label="Porte da empresa"
            hint="Escolha de acordo com o Comprovante de Inscrição e de Situação Cadastral"
            @input="setSize"
            ref="sizeInput"
          />
        </v-container>
      </div>

      <div v-if="!isUpdating" class="mt-8 text-h6 font-weight-regular">
        Vínculo com a Universidade de São Paulo (USP)
        <v-divider />
        <v-container>
          <legend class="body-2 mb-5">
            Para ser uma empresa DNA USP, a mesma deve se enquadrar nos requisitos estabelecidos na
            <a href="https://leginf.usp.br/?portaria=portaria-gr-no-7679-de-29-de-novembro-de-2021"
            target="_blank"
            >
              Portaria GR Nº 7679</a>. Dessa forma, pedimos que selecione uma das opções abaixo.
          </legend>
          <Dropdown
            :value="category"
            label="Categoria DNA USP"
            :options="allCategories"
            @input="setCategory"
          />
        </v-container>
      </div>

      <div class="mt-8 text-h6 font-weight-regular">
        Código e Descrição da Natureza Jurídica da empresa
        <v-divider />
        <v-container>
          <legend class="body-2 mb-5">
            Para consultar o código e a natureza jurídica da empresa, cole o CNPJ da empresa no
            <a
              href="http://servicos.receita.fazenda.gov.br/Servicos/cnpjreva/Cnpjreva_Solicitacao.asp?cnpj"
              target="_blank"
              >site da receita</a
            >. Depois copie o CÓDIGO e a DESCRIÇÃO DA NATUREZA JURÍDICA e cole
            como resposta nos campos abaixo. Insira apenas o código no formato
            "000-0" e a descrição no formato de texto. A lista de códigos e naturezas jurídicas
            pode ser encontrada na
            <a
              href="https://concla.ibge.gov.br/images/concla/documentacao/CONCLA-TNJ2021-EstruturaDetalhada.pdf"
              target="_blank"
              >tabela de naturezas jurídicas do IBGE</a
            >.
          </legend>
          <PairOfNumberAndText
            :value="company_nature"
            firstLabel="Código"
            secondLabel="Natureza Jurídica"
            firstPlaceholder="000-0"
            separator=" - "
            @input="setCompanyNature"
            @changeFirstField="matchCompanyNature('first', $event)"
            @changeSecondField="matchCompanyNature('second', $event)"
            ref="companyNatureField"
          />
        </v-container>
      </div>

      <div class="mt-8 text-h6 font-weight-regular">
        CNAE (Classificação Nacional de Atividades Econômicas) da empresa
        <v-divider />
        <v-container>
          <legend class="body-2 mb-5">
            Para consultar o CNAE cole o CNPJ da empresa no
            <a
              href="http://servicos.receita.fazenda.gov.br/Servicos/cnpjreva/Cnpjreva_Solicitacao.asp?cnpj"
              target="_blank"
              >site da receita</a
            >. Depois copie o CÓDIGO DA ATIVIDADE ECONÔMICA PRINCIPAL e cole
            como resposta desta pergunta. Insira apenas o código no formato
            00.00-0-00. Essa informação é essencial para a categorização da
            empresa.
          </legend>
          <MaskInput
            :value="cnae"
            label="CNAE *"
            mask="##.##-#-##"
            :rule="/^\d{2}\.\d{2}-\d(-\d{2})?$/"
            @input="setCnae"
            ref="cnaeInput"
          />
        </v-container>
      </div>

      <div class="mt-5 text-h6 font-weight-regular">
        Contato
        <v-divider />
        <v-container>
          <p class="body-2">Telefones comerciais</p>

          <!-- Campos de Telefone -->
          <div v-for="(phone, index) in localPhones" :key="`phone-${index}`" class="d-flex align-center mb-2">
            <PhoneInput
              :value="phone"
              :label="`Telefone comercial ${index + 1}`"
              @input="updatePhone(index, $event)"
              class="flex-grow-1 mr-2"
            />
            <v-btn
              icon
              small
              color="error"
              @click="removePhone(index)"
              :disabled="localPhones.length <= 1"
            >
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </div>

          <v-btn
            small
            color="primary"
            @click="addPhone"
            :disabled="localPhones.length >= 3"
          >
            <v-icon left>mdi-plus</v-icon>
            Adicionar telefone
          </v-btn>

          <p class="body-2 mt-5">Emails</p>

          <!-- Campos de Email -->
          <div v-for="(email, index) in localEmails" :key="`email-${index}`" class="d-flex align-center mb-2">
            <EmailInput
              :value="email"
              :label="`Email ${index + 1}`"
              @input="updateEmail(index, $event)"
              class="flex-grow-1 mr-2"
            />
            <v-btn
              icon
              small
              color="error"
              @click="removeEmail(index)"
              :disabled="localEmails.length <= 1"
            >
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </div>

          <v-btn
            small
            color="primary"
            @click="addEmail"
            :disabled="localEmails.length >= 3"
          >
            <v-icon left>mdi-plus</v-icon>
            Adicionar email
          </v-btn>

          <TextInputFormatted
            class="mt-8"
            :value="venue"
            label="Endereço"
            capitalization="title"
            @input="setVenue"
          />
          <TextInputFormatted
            :value="neighborhood"
            label="Bairro"
            capitalization="title"
            @input="setNeighborhood"
          />
          <TextInputFormatted
            :value="city"
            label="Cidade sede"
            capitalization="title"
            @input="setCity"
          />
          <Dropdown
            class="mt-8"
            :value="state"
            label="Estado"
            :options="allStates"
            @input="setState"
          />
          <MaskInput
            :value="cep"
            label="CEP *"
            mask="#####-###"
            :rule="/\d{5}-\d{3}/"
            @input="setCep"
            ref="cepInput"
          />
        </v-container>
      </div>
    </v-form>
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import MultipleInputs from "@/components/CompanyForms/inputs/MultipleInputs.vue";
import MaskInput from "@/components/CompanyForms/inputs/MaskInput.vue";
import TextInputFormatted from "@/components/CompanyForms/inputs/TextInputFormatted.vue";
import NumberInput from "@/components/CompanyForms/inputs/NumberInput.vue";
import PhoneInput from "@/components/CompanyForms/inputs/PhoneInput.vue";
import EmailInput from "@/components/CompanyForms/inputs/EmailInput.vue";
import CnpjInput from "@/components/CompanyForms/inputs/CnpjInput.vue";
import Dropdown from "@/components/CompanyForms/inputs/Dropdown.vue";
import PairOfNumberAndText from "@/components/CompanyForms/inputs/PairOfNumberAndText.vue";
import BetaVersionModal from "../../layout/BetaVersionModal.vue";

export default {
  components: {
    BetaVersionModal,
    MaskInput,
    MultipleInputs,
    TextInputFormatted,
    NumberInput,
    PhoneInput,
    EmailInput,
    CnpjInput,
    Dropdown,
    PairOfNumberAndText,
  },
  data: () => ({
    allStates: [],
    allRegistryStatus: [
      "Ativa",
      "Ativa Não Regular",
      "Baixada",
      "Inapta",
      "Nula",
      "Suspensa"
    ],
    sizeList: ["MEI", "ME", "EPP", "DEMAIS"],
    isUpdating: false,
    allCategories: [
      "Fundada por aluno ou ex-aluno de graduação da USP",
      "Fundada por aluno ou ex-aluno de pós-graduação da USP",
      "Fundada por aluno ou ex-aluno de pós-doutorado da USP",
      "Fundada por aluno ou ex-aluno do IPEN (Instituto de Pesquisas Energéticas e Nucleares)",
      "Fundada por um docente ou Professor Sênior da USP",
      "Fundada por um servidor técnico e administrativo (ou ex-servidor) da USP",
      "Empresa incubada ou graduada em uma das incubadoras ligadas à USP (Incubadora USP/IPEN, ESALQTEC, HABITS, SUPERA e ParqTec)"
    ],
    localPhones: [''],
    localEmails: [''],
  }),
  computed: {
    ...mapGetters({
      name: "company_forms/name",
      corporateName: "company_forms/corporateName",
      year: "company_forms/year",
      size: "company_forms/size",
      cnpj: "company_forms/cnpj",
      cnae: "company_forms/cnae",
      registry_status: "company_forms/registry_status",
      phones: "company_forms/phones",
      emails: "company_forms/emails",
      address: "company_forms/address",
      venue: "company_forms/venue",
      neighborhood: "company_forms/neighborhood",
      city: "company_forms/city",
      state: "company_forms/state",
      cep: "company_forms/cep",
      company_nature: "company_forms/company_nature",
      category: "company_forms/category",
    }),
  },
  watch: {
    phones: {
      handler(newValue) {
        if (Array.isArray(newValue) && newValue.length > 0) {
          this.localPhones = [...newValue];
        } else {
          this.localPhones = [''];
        }
      },
      immediate: true
    },
    emails: {
      handler(newValue) {
        if (Array.isArray(newValue) && newValue.length > 0) {
          this.localEmails = [...newValue];
        } else {
          this.localEmails = [''];
        }
      },
      immediate: true
    }
  },
  mounted() {
    this.isUpdating = this.cnpj !== "";
  },
  created() {
    this.getStates();
  },
  methods: {
    ...mapActions({
      setName: "company_forms/setName",
      setCorporateName: "company_forms/setCorporateName",
      setYear: "company_forms/setYear",
      setSize: "company_forms/setSize",
      setCnpj: "company_forms/setCnpj",
      setCnae: "company_forms/setCnae",
      setRegistryStatus: "company_forms/setRegistryStatus",
      setPhones: "company_forms/setPhones",
      setEmails: "company_forms/setEmails",
      setVenue: "company_forms/setVenue",
      setNeighborhood: "company_forms/setNeighborhood",
      setCity: "company_forms/setCity",
      setState: "company_forms/setState",
      setCep: "company_forms/setCep",
      setCompanyNature: "company_forms/setCompanyNature",
      setCategory: "company_forms/setCategory",
    }),
    async getStates() {
      const response = await fetch(
        "https://servicodados.ibge.gov.br/api/v1/localidades/estados"
      );
      const ufs = await response.json();
      this.allStates = ufs
        .map(({ nome, sigla }) => `${nome} (${sigla})`)
        .sort();
    },
    findReverse(association, value) {
      const key = Object.keys(association).find(
        (key) => association[key].localeCompare(value) === 0
      );
      return key;
    },
    matchCompanyNature(field, value) {
      const association = this.$company_nature;

      let complement_value = undefined;
      if (field === "first") {
        complement_value = association[value];
      } else {
        complement_value = this.findReverse(association, value);
      }

      const reverse_field = field === "first" ? "second" : "first";
      if (complement_value) {
        this.$refs.companyNatureField.set(reverse_field, complement_value);
      }
    },

    validateStep() {
      const errors = [];

      if (!this.name || this.name.trim().length < 3) {
        errors.push('Nome fantasia é obrigatório (mínimo 3 caracteres)');
      }

      if (!this.corporateName || this.corporateName.trim().length < 3) {
        errors.push('Razão social é obrigatória (mínimo 3 caracteres)');
      }

      if (!this.year || this.year < 1900 || this.year > 2025) {
        errors.push('Ano de fundação é obrigatório e deve estar entre 1900 e 2025');
      }

      if (this.$refs.cnpjInput && !this.$refs.cnpjInput.isValid()) {
        errors.push('CNPJ inválido ou não preenchido');
      }

      if (this.$refs.cnaeInput && !this.$refs.cnaeInput.isValid()) {
        errors.push('CNAE inválido ou não preenchido');
      }

      if (this.$refs.cepInput && !this.$refs.cepInput.isValid()) {
        errors.push('CEP inválido ou não preenchido');
      }

      return {
        isValid: errors.length === 0,
        errors: errors
      };
    },
    addPhone() {
      if (this.localPhones.length < 3) {
        this.localPhones.push('');
      }
    },
    addEmail() {
      if (this.localEmails.length < 3) {
        this.localEmails.push('');
      }
    },
    savePhones() {
      const filteredPhones = this.localPhones
        .filter(phone => phone && phone.toString().trim() !== '')
        .map(phone => phone.toString().trim());
      this.setPhones(filteredPhones);
    },
    saveEmails() {
      const filteredEmails = this.localEmails
        .filter(email => email && email.toString().trim() !== '')
        .map(email => email.toString().trim().toLowerCase());
      this.setEmails(filteredEmails);
    },
    updatePhone(index, value) {
      const sanitizedValue = value ? value.toString().trim() : '';
      this.localPhones[index] = sanitizedValue;
      this.savePhones();
    },
    removePhone(index) {
      if (this.localPhones.length > 1) {
        this.localPhones.splice(index, 1);
        this.savePhones();
      }
    },
    updateEmail(index, value) {
      const sanitizedValue = value ? value.toString().trim().toLowerCase() : '';
      this.localEmails[index] = sanitizedValue;
      this.saveEmails();
    },
    removeEmail(index) {
      if (this.localEmails.length > 1) {
        this.localEmails.splice(index, 1);
        this.saveEmails();
      }
    }
  },
};
</script>
