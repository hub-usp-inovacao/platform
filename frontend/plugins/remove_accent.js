const removeAccent = (rawStr) => {
  let str = `${rawStr}`;
  const variations = {};

  variations["a"] = /[áàãâä]/g;
  variations["e"] = /[éèẽêë]/g;
  variations["i"] = /[íìĩîï]/g;
  variations["o"] = /[óòõôö]/g;
  variations["u"] = /[úùũûü]/g;
  variations["c"] = /[ç]/g;
  variations["n"] = /[ñ]/g;
  variations["A"] = /[ÀÁÂÄÅÃ]/g;
  variations["E"] = /[ÉÈÊË]/g;
  variations["I"] = /[ÍÌÎÏ]/g;
  variations["O"] = /[ÓÒÔÖÕ]/g;
  variations["U"] = /[ÚÙÛÜ]/g;

  Object.keys(variations).forEach((letter) => {
    str = str.replace(variations[letter], letter);
  });

  return str;
};

export default (_, inject) => {
  inject("removeAccent", removeAccent);
};
