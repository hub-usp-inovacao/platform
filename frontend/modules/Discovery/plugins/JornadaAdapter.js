function JornadaAdapter(axios) {
  return {
    requestLearn: undefined,
    updateLearn: undefined,
    requestPractice: undefined,
    updatePractice: undefined,
    requestCreate: undefined,
    updateCreate: undefined,
    requestImprove: undefined,
    updateImprove: undefined,
    requestFund: undefined,
    updateFund: undefined,
  };
}

export default (context, inject) => {
  const adapter = JornadaAdapter(context.$axios);
  inject("JornadaAdapter", adapter);
};
