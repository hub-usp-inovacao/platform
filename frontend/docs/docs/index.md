# Hub USP Inovação – Frontend

Bem-vindo à documentação do frontend do **Hub USP Inovação**.  
Esta plataforma é a interface digital para o ecossistema de inovação e empreendedorismo da USP, gerenciado pela AUSPIN. Ela permite que a comunidade acadêmica, pesquisadores, empresas, parceiros externos e stakeholders interajam e acessem recursos para inovação, centralizando informações e facilitando a colaboração.

---

## 🏛️ Sobre o Projeto

O **Hub USP Inovação** é o ponto central para atividades indexadas pela AUSPIN (Agência USP de Inovação). O frontend fornece diversos recursos para navegação, busca e interação com as entidades de inovação e empreendedorismo da USP. As principais entidades são:

-   **Companies:** Empresas fundadas por alunos ou ex-alunos da USP, destacando o impacto do ecossistema empreendedor.
-   **Disciplines:** Cursos e disciplinas oferecidos na USP que possuem relação com inovação ou empreendedorismo, promovendo a formação de talentos.
-   **Initiatives:** Iniciativas de inovação e empreendedorismo da comunidade USP, como projetos, programas e eventos.
-   **Patents:** Patentes (co-)obtidas pela USP, evidenciando a produção intelectual e tecnológica da universidade.
-   **PDIs:** Projetos de Pesquisa, Desenvolvimento e Inovação, conectando demandas de mercado com a capacidade de pesquisa da USP.
-   **Researchers:** Professores, pós-doutorandos e doutorandos, incluindo suas habilidades e áreas de atuação, facilitando a identificação de expertise.

As principais áreas que o frontend suporta são:

-   **Catalog** – Permite a navegação e busca eficiente por todas as entidades indexadas, com filtros e opções de ordenação para facilitar a descoberta.
-   **Curatorship** – Oferece interfaces intuitivas para a criação, edição e atualização segura de registros de entidades, garantindo a integridade e relevância dos dados.
-   **Discovery** – Guia novos usuários através de chamadas para ação (CTAs) e fluxos de onboarding, incentivando a exploração e o engajamento com a plataforma.
-   **Technology Transference** – Conecta demandas externas (de empresas ou do mercado) com o ecossistema interno da USP, facilitando a colaboração e a aplicação de tecnologias.

---

## 📦 Tecnologias Utilizadas

A escolha das tecnologias visa garantir um desenvolvimento ágil, uma aplicação robusta e escalável, e uma excelente experiência para o desenvolvedor e para o usuário final.

-   **React** – Utilizado para construir interfaces de usuário modernas, reativas e baseadas em componentes, promovendo a reutilização de código e a manutenibilidade.
-   **TypeScript** – Adiciona tipagem estática ao JavaScript, resultando em um código mais seguro, com menos erros em tempo de execução, e melhorando a escalabilidade e a experiência de desenvolvimento.
-   **Vite** – Um bundler e ambiente de desenvolvimento frontend que oferece ferramentas ultra-rápidas, como Hot Module Replacement (HMR) instantâneo, otimizando o ciclo de desenvolvimento.
-   **Tailwind CSS** – Um framework CSS utility-first que permite estilizar rapidamente a interface diretamente no markup, promovendo consistência de design e reduzindo o CSS não utilizado.
-   **React Router** – Gerencia o roteamento e a navegação no frontend de forma declarativa, permitindo a criação de Single Page Applications (SPAs) com URLs amigáveis.
-   **Axios** – Um cliente HTTP baseado em Promises para o navegador e Node.js, facilitando a realização de chamadas de API e o tratamento de requisições e respostas.
-   **ESLint & Prettier** – Ferramentas essenciais para garantir a qualidade do código. **ESLint** para identificar e reportar padrões problemáticos, e **Prettier** para formatar o código de forma consistente, evitando discussões sobre estilo em revisões de código.
-   **Vitest** – Um framework de testes unitários otimizado para Vite, oferecendo uma API compatível com Jest e testes rápidos, essenciais para garantir a robustez do código.

---

## 🚀 Primeiros Passos

### Pré-requisitos

Para configurar e executar o projeto localmente, você precisará ter as seguintes ferramentas instaladas:

-   **Node.js** 18+ (Recomendamos a versão LTS para estabilidade).
-   **Yarn** ou **npm** (Gerenciadores de pacotes JavaScript).

### Configuração do Projeto

Siga os passos abaixo para clonar o repositório e configurar as dependências:

```bash
# Clone o repositório principal do Hub USP Inovação
git clone https://github.com/hub-usp-inovacao/platform.git

# Navegue até o diretório do frontend
cd platform/frontend

# Instale as dependências do projeto usando Yarn (recomendado) ou npm
yarn install
# ou
npm install

# Inicie o servidor de desenvolvimento
yarn dev
# ou
npm run dev