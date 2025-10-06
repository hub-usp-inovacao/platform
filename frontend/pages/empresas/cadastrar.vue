<template>
  <div>
    <div class="background">
      <Panel :title="title" :description="description" no-search />
    </div>
    <Stepper :errors="errors" :update="false" @finish="onStepperFinish" />
    <ResultDialog
      :show="dialog.show"
      :title="dialog.title"
      :message="dialog.message"
      @ok-click="handleOkClick"
    />
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
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

  computed: {
    ...mapGetters({
      errors: "company_forms/errors",
    }),
  },

  methods: {
    ...mapActions({
      registerCompanyForm: "company_forms/registerCompanyForm",
    }),

    async onStepperFinish() {
      await this.registerCompanyForm()
        .then((status) => {
          if (status) {
            this.dialog = {
              show: true,
              latestRequestSucceeded: true,
              title: "Solicitação de registro enviada!",
              message:
                "Agora os dados da empresa serão validados pela equipe Hub USPInovação e em breve estarão disponíveis na plataforma.",
            };
          } else {
            this.dialog = {
              show: true,
              latestRequestSucceeded: false,
              title: "Erro ao registrar dados",
              message:
                this.errors.server || this.errors.logo ||
                "Verifique o formulário e tente novamente.",
            };
          }
        })
        .catch(console.log);
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
