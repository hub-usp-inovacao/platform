<template>
  <main>
    <div id="company_update_background" class="background">
      <Panel :title="title" :description="description" no-search />
    </div>

    <v-container>
      <v-row v-if="message !== undefined">
        <v-col cols="12" offset-md="3" md="6">
          <v-alert
            border="bottom"
            :color="
              message.type == 'error' ? 'red lighten-2' : 'green lighten-2'
            "
            dark
          >
            {{ message.text }}
          </v-alert>
        </v-col>
      </v-row>

      <v-row>
        <v-col cols="12" offset-md="3" md="6">
          <MaskInput
            :value="cnpj"
            label="CNPJ"
            mask="##.###.###/####-##"
            @input="setCNPJ"
          />
        </v-col>
      </v-row>

      <v-row>
        <v-col cols="12" offset-md="3" md="6" align="center">
          <v-btn color="primary" :disabled='requestRunning' @click="request">Solicitar</v-btn>
        </v-col>
      </v-row>

      <v-row>
        <v-col cols="12" offset-md="3" md="6" align="center">
          <p>
            Já tem um token de atualização?
            <nuxt-link to="/empresas/atualizar">Cole aqui</nuxt-link>
          </p>
        </v-col>
      </v-row>
    </v-container>
  </main>
</template>

<script>
import Panel from "@/components/first_level/Panel.vue";
import MaskInput from "@/components/CompanyForms/inputs/MaskInput.vue";

export default {
  components: {
    Panel,
    MaskInput,
  },
  data: () => ({
    title: "Solicitação de Atualização cadastral",
    description:
      "Para garantir a segurança na atualização de dados na Plataforma Hub USPInovação, faça uma solicitação nesta página informando o CNPJ da empresa. Uma palavra-passe (token) segura será automaticamente enviada para o email correspondente em nosso cadastro atual. Este token será utilizado para dar acesso ao formulário de atualização.",
    cnpj: "",
    message: undefined,
    requestRunning: false
  }),

  methods: {
    setCNPJ(e) {
      this.cnpj = e;
    },

    async request() {
      this.message = undefined;
      this.requestRunning = true;
      const backendURL = process.env.BACKEND_URL;
      const uri = "companies/update_request";
      const headers = {
        "Content-Type": "application/json",
      };
      const body = {
        update_request: {
          cnpj: this.cnpj,
        },
      };

      try {
        const resp = await fetch(`${backendURL}/${uri}`, {
          method: "POST",
          headers,
          body: JSON.stringify(body),
        });

        const data = await resp.json();
        if (data.error !== undefined) {
          this.message = { type: "error", text: "CNPJ não encontrado" };
        } else {
          this.message = {
            type: "success",
            text: "Um email com o token e as instruções foi enviado",
          };
        }
      } catch (err) {
        console.log(err);
      }

      this.requestRunning = false;
    },
  },
};
</script>

<style></style>
