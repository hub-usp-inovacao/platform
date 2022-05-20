<template>
  <main>
    <div class="background">
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
          <v-text-field
            :value="email"
            label="Email cadastrado"
            v-model="email"
          />
        </v-col>
      </v-row>

      <v-row>
        <v-col cols="12" offset-md="3" md="6" align="center">
          <v-btn color="primary" @click="request">Solicitar</v-btn>
        </v-col>
      </v-row>

      <v-row>
        <v-col cols="12" offset-md="3" md="6" align="center">
          <p>
            Já solicitou sua atualização?
            <nuxt-link to="/empresas/atualizar">Cole seu token aqui</nuxt-link>
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
      "Para garantir a segurança na atualização de dados na Plataforma Hub USPInovação, faça uma solicitação nesta página informando o email cadastrado. Uma palavra-passe (token) segura será automaticamente enviada para o email correspondente em nosso cadastro atual. Este token será utilizado para dar acesso ao formulário de atualização.",
    email: "",
    message: undefined,
  }),

  methods: {
    async request() {
      this.message = undefined;
      const backendURL = process.env.BACKEND_URL;
      const uri = "skill/update_request";
      const headers = {
        "Content-Type": "application/json",
      };
      const body = {
        update_request: {
          email: this.email,
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
          this.message = { type: "error", text: "Email não encontrado" };
        } else {
          this.message = {
            type: "success",
            text: "Um email com o token e as instruções foi enviado",
          };
        }
      } catch (err) {
        console.log(err);
      }
    },
  },
};
</script>

<style></style>
