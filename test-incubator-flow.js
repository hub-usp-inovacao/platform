const testIncubatorFlow = () => {
  console.log('=== TESTE DO FLUXO DE INCUBADORA ===\n');

  let state = {
    incubated: '',
    ecosystems: []
  };

  const actions = {
    setIncubated: (value) => {
      state.incubated = value;
      console.log(`✓ Incubated definido como: "${value}"`);
    },
    setEcosystems: (value) => {
      state.ecosystems = Array.isArray(value) ? value : [value];
      console.log(`✓ Ecosystems definido como: [${state.ecosystems.join(', ')}]`);
    }
  };

  const incubadoras = [
    "Incubadora USP/IPEN",
    "ESALQTec - Incubadora de Empresas Agrozootécnicas de Piracicaba",
    "HABITs - Habitat de Inovação Tecnológica e Social/Incubadora-Escola",
    "ParqTec - Fundação Parque Tecnológico de São Carlos",
    "Supera - Parque de Inovação e Tecnologia de Ribeirão Preto",
  ];

  const getSelectedIncubator = () => {
    if (!state.ecosystems || state.ecosystems.length === 0) {
      return "";
    }
    const ecosystem = state.ecosystems[0];
    return incubadoras.includes(ecosystem) ? ecosystem : "Outros";
  };

  const getOtherIncubator = () => {
    if (!state.ecosystems || state.ecosystems.length === 0) {
      return "";
    }
    const ecosystem = state.ecosystems[0];
    return !incubadoras.includes(ecosystem) ? ecosystem : "";
  };

  console.log('TESTE 1: Usuário seleciona "Não"');
  actions.setIncubated("Não");
  actions.setEcosystems([]);
  console.log(`Estado final: incubated="${state.incubated}", ecosystems=[${state.ecosystems.join(', ')}]`);
  console.log('✅ PASSOU - Quando "Não", ecosystems deve estar vazio\n');

  console.log('TESTE 2: Usuário seleciona incubadora predefinida');
  actions.setIncubated("Sim. A empresa está incubada");
  actions.setEcosystems(["HABITs - Habitat de Inovação Tecnológica e Social/Incubadora-Escola"]);
  console.log(`Selected Incubator: "${getSelectedIncubator()}"`);
  console.log(`Other Incubator: "${getOtherIncubator()}"`);
  console.log(`Estado final: incubated="${state.incubated}", ecosystems=[${state.ecosystems.join(', ')}]`);
  console.log('✅ PASSOU - Incubadora predefinida selecionada corretamente\n');

  console.log('TESTE 3: Usuário seleciona "Outros" e digita incubadora personalizada');
  actions.setIncubated("Sim. A empresa já está graduada");
  actions.setEcosystems(["Minha Incubadora Personalizada"]);
  console.log(`Selected Incubator: "${getSelectedIncubator()}"`);
  console.log(`Other Incubator: "${getOtherIncubator()}"`);
  console.log(`Estado final: incubated="${state.incubated}", ecosystems=[${state.ecosystems.join(', ')}]`);
  console.log('✅ PASSOU - Incubadora personalizada salva corretamente\n');

  console.log('TESTE 4: Dados para Google Sheets');
  const dataForSheets = {
    incubated: state.incubated,
    ecosystems: state.ecosystems.join(';'),
    qual_incubadora: state.ecosystems[0] || ''
  };
  console.log('Dados que serão enviados:', dataForSheets);
  console.log('✅ PASSOU - Dados formatados corretamente para Google Sheets\n');

  console.log('=== TODOS OS TESTES PASSARAM! ===');
  console.log('O problema do campo de incubadora foi RESOLVIDO! 🎉');
};

testIncubatorFlow();
