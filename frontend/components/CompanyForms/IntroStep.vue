<template>
  <div>
    <section v-if="isUpdate === false">
      <h1>Importante</h1>

      <p>
        Para ser considerada elegível como empresa DNA USP, a mesma necessita
        atender aos requisitos do Artigo 1º da
        <a
          href="http://leginf.usp.br/?portaria=portaria-gr-no-7679-de-29-de-novembro-de-2021"
        >
          Portaria GR 7679/2021
        </a>
      </p>

      <p>
        Para preencher este formulário, a empresa necessita ter sido criada por
        alunos(as) que cursaram ou que ainda estejam cursando a graduação ou
        pós-graduação na USP, ou então por pesquisadores e docentes da USP.
        Também podem se cadastrar as empresas que sejam resultantes de processos
        de incubação ou de aceleração em alguma das incubadoras associadas à
        Universidade de São Paulo (Incubadora USP/IPEN, ESALQTec, HABITs e Supera).
      </p>

      <p>
        Somente empresas formalmente constituídas (com CNPJ) podem preencher
        este formulário.
      </p>

      <p>
        Em caso de empresas estabelecidas no exterior, favor entrar em contato
        pelo e-mail
        <a href="mailto:hubusp.inovacao@usp.br">hubusp.inovacao@usp.br</a>
      </p>

      <p>
        As informações fornecidas serão verificadas quanto a adequação aos
        requisitos citados acima.
      </p>
    </section>

    <section v-else>
      <h1>Importante</h1>
      <p>
        Para ser considerada elegível como empresa DNA USP, a mesma necessita
        atender aos requisitos do Artigo 1º da
        <a
          href="http://leginf.usp.br/?portaria=portaria-gr-no-7679-de-29-de-novembro-de-2021"
        >
          Portaria GR 7679/2021
        </a>
      </p>
      <p>
        Para ser considerada uma Empresa DNA USP, a mesma necessita ter sido
        criada por alunos(as) que cursaram ou que ainda estejam cursando a
        graduação ou pós-graduação na USP, ou então por pesquisadores e docentes
        da USP. Também são consideradas aquelas empresas que sejam resultantes
        de processos de incubação ou de aceleração em alguma das incubadoras
        associadas à Universidade de São Paulo (Incubadora USP/IPEN, ESALQTec, HABITs e
        Supera).
      </p>

      <p>
        As informações fornecidas serão verificadas quanto a adequação aos
        requisitos citados e consistência antes do carregamento na plataforma
        Hub USPInovação.
      </p>
    </section>
  </div>
</template>

<script>
export default {
  props: {
    isUpdate: {
      type: Boolean,
      value: Boolean,
      default: true,
    },
  },
};
</script>
