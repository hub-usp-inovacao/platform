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
            v-model="email"
            label="E-mail"
            placeholder="Seu e-mail"
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
            <nuxt-link to="/competencias/atualizar">Cole aqui</nuxt-link>
          </p>
        </v-col>
      </v-row>
    </v-container>
  </main>
</template>

<script>
import Panel from "@/components/first_level/Panel.vue";

export default {
  components: {
    Panel,
  },
  data: () => ({
    title: "Solicitação de Atualização no Cadastro de Competências",
    description:
      "Para garantir a segurança na atualização de dados na Plataforma Hub USPInovação, faça uma solicitação nesta página informando o email utilizado no cadastro. Uma palavra-passe (token) segura será automaticamente enviada para o email correspondente em nosso cadastro atual. Este token será utilizado para dar acesso ao formulário de atualização de competência.",
    email: "",
    message: undefined,
    requestRunning: false
  }),

  methods: {
    async request() {
      this.message = undefined;
      this.requestRunning = true;
      const backendURL = process.env.BACKEND_URL;
      const uri = "skills/update_request";
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
      
      this.requestRunning = false;
    },
  },
};
</script>
