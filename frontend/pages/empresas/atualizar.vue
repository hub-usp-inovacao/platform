<template>
  <section>
    <div id="company_update_background" class="background">
      <Panel :title="title" :description="description()" no-search />
      <v-alert dense border="left" type="warning" class="text-center">
        Dados sensíveis não são mostrados neste formulário.
      </v-alert>
    </div>

    <v-form ref="form">
      <v-container>
        <v-row v-if="ok">
          <Stepper @finish="updateCompany" />
          <v-dialog v-model="dialog.show" persistent max-width="500">
            <v-card>
              <v-card-title class="text-h5">
                {{ dialog.title }}
              </v-card-title>
              <v-card-text>
                {{ dialog.message }}
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="green darken-1" text @click="handleOkClick">
                  Ok!
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-row>
        <v-row v-else>
          <v-col cols="10">
            <MaskInput
              :value="cnpj"
              label="CNPJ"
              mask="##.###.###/####-##"
              @input="setCnpj"
            />
          </v-col>
          <v-col cols="2" align="bottom">
            <v-btn
              color="secondary"
              width="100"
              text
              :loading="loading"
              :disabled="!isValid"
              @click="submit"
            >
              Enviar
            </v-btn>
          </v-col>
        </v-row>
      </v-container>
    </v-form>
  </section>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import Panel from "@/components/first_level/Panel.vue";
import MaskInput from "@/components/CompanyForms/inputs/MaskInput.vue";
import Stepper from "@/components/CompanyForms/Stepper.vue";

export default {
  components: {
    Panel,
    MaskInput,
    Stepper,
  },
  data: () => ({
    title: "Atualização de Empresas DNAUSP",

    loading: false,
    ok: false,
    dialog: {
      show: false,
      status: "",
      title: "",
      message: "",
    },
  }),

  computed: {
    ...mapGetters({
      cnpj: "company_forms/cnpj",
      errors: "company_forms/errors",
    }),

    isValid() {
      return this.cnpj.length === 18;
    },
  },

  methods: {
    ...mapActions({
      setCnpj: "company_forms/setCnpj",
      getCompanyData: "company_forms/getCompanyData",
      updateCompanyForm: "company_forms/updateCompanyForm",
    }),

    description() {
      if (this.ok == false) {
        return "Representantes das Empresas podem solicitar, nesta página, a atualização dos dados cadastrados.\n Para prosseguir, insira o CNPJ da empresa abaixo."
      }
      else {
        return ""
      }
    },

    async submit() {
      if (this.isValid) {
        this.loading = true;
        await this.getCompanyData();
        this.loading = false;

        if (this.errors.length === 0) {
          this.ok = true;
        }
      }
    },

    async updateCompany() {
      const updated = await this.updateCompanyForm();

      if (updated) {
        this.dialog = {
          show: true,
          status: "success",
          title: "Solicitação de atualização enviada!",
          message:
            "Agora os dados da empresa serão validados pela equipe Hub USPInovação e em breve estarão disponíveis na plataforma.",
        };
      } else {
        this.dialog = {
          show: true,
          status: "error",
          title: "Erro ao atualizar os dados",
          message: this.errors.join("; "),
        };
      }
    },

    clearDialog() {
      this.dialog = {
        show: false,
        status: "",
        title: "",
        message: "",
      };
    },

    handleOkClick() {
      const { status } = this.dialog;
      this.clearDialog();

      if (status === "success") {
        this.$router.push("/");
      }
    },
  },
};
</script>
