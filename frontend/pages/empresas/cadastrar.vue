<template>
  <div>
    <div class="background">
      <Panel :title="title" :description="description" no-search />
    </div>
    <Stepper :update="false" @finish="onStepperFinish" />
    <ResultDialog
      :show="dialog.show"
      :title="dialog.title"
      :message="dialog.message"
      @ok-click="handleOkClick"
    />
  </div>
</template>

<script>
import { mapActions } from "vuex";
import ResultDialog from "@/components/CompanyForms/ResultDialog.vue";
import Stepper from "@/components/CompanyForms/Stepper.vue";
import Panel from "@/components/first_level/Panel.vue";

export default {
  components: {
    ResultDialog,
    Stepper,
    Panel,
  },

  data: () => ({
    title: "Cadastro de Empresas",
    description:
      "Representantes das Empresas podem solicitar, nesta página, o cadastro de seus empresas no Hub.",
    dialog: {
      show: false,
      latestRequestSucceeded: false,
      title: "",
      message: "",
    },
  }),

  methods: {
    ...mapActions({
      registerCompanyForm: "company_forms/registerCompanyForm",
      errors: "company_forms/errors",
    }),

    async onStepperFinish() {
      const requestStatus = await this.registerCompanyForm();

      if (requestStatus) {
        this.dialog = {
          show: true,
          latestRequestSucceeded: requestStatus,
          title: "Solicitação de registro enviada!",
          message:
            "Agora os dados da empresa serão validados pela equipe Hub USPInovação e em breve estarão disponíveis na plataforma.",
        };
      } else {
        // TODO: Show erros from server
        this.dialog = {
          show: true,
          latestRequestSucceeded: requestStatus,
          title: "Erro ao registrar dados",
          message: "Verifique o formulário e tente novamente.",
        };
      }
    },

    clearDialog() {
      this.dialog = {
        show: false,
        latestRequestSucceeded: false,
        title: "",
        message: "",
      };
    },

    handleOkClick() {
      if (this.dialog.latestRequestSucceeded) {
        this.$router.push("/");
      }
      this.clearDialog();
    },
  },
};
</script>
