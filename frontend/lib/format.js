export const formatURL = (raw) => {
  const httpPrefix = raw.substr(0, 4);
  return httpPrefix.toLowerCase() == "http" ? raw : `http://${raw}`;
};

const tokenSeparator = " ";
export const capitalizeName = (raw) => {
  if (raw)
    return raw
      .toLocaleLowerCase()
      .trim()
      .split(tokenSeparator)
      .filter((s) => s.trim().length > 0)
      .map((s) => {
        if (s != "de" && s != "do" && s != "da")
          return s[0].toLocaleUpperCase() + s.slice(1);
        return s;
      })
      .join(tokenSeparator);
};

export const fixPersonName = (name) =>
  name.includes(",") ? name.split(",").reverse().join(" ") : name;

const eightDigits = (numbers) => {
  const prefix = numbers.substr(0, 4);
  const sufix = numbers.substr(4, 4);

  return `${prefix}-${sufix}`;
};

const nineDigits = (numbers) => {
  const prefix = numbers.substr(0, 5);
  const sufix = numbers.substr(5, 4);

  return `${prefix}-${sufix}`;
};

const dddEightDigits = (numbers) => {
  const ddd = numbers.substr(0, 2);
  const number = numbers.substr(2, 8);

  return `(${ddd}) ${eightDigits(number)}`;
};

const dddNineDigits = (numbers) => {
  const ddd = numbers.substr(0, 2);
  const number = numbers.substr(2, 9);

  return `(${ddd}) ${nineDigits(number)}`;
};

const format0800phone = (numbers) => {
  const prefix = numbers.substr(0, 4);
  const middle = numbers.substr(4, 3);
  const final = numbers.substr(7, 4);

  return `${prefix} ${middle} ${final}`;
};

export const formatPhone = (raw) => {
  if (!raw) return "";

  const numbers = raw.replace(/\D/g, "");

  switch (numbers.length) {
    case 8:
      return eightDigits(numbers);
    case 9:
      return nineDigits(numbers);
    case 10:
      return dddEightDigits(numbers);
    case 11:
      if (numbers.substr(0, 4) == "0800") {
        return format0800phone(numbers);
      }
      return dddNineDigits(numbers);
    default:
      return numbers;
  }
};

export const removeAccent = (rawStr) => {
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
