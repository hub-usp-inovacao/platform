export const findErrors = function (objects) {
  const knownNames = {};
  const errors = [];

  objects.forEach((object, index) => {
    const lineErrors = [];
    if (knownNames[object.name]) {
      lineErrors.push("Nome duplicado");
    } else {
      knownNames[object.name] = object;
    }

    if (object.name.match(/[()[\]{}]/))
      lineErrors.push(
        "Nome da disciplina contém um dos seguintes caracteres inválidos: (, ), [, ], {, }"
      );

    if (!object.url.match(/^http(s)?:\/\/uspdigital\.usp\.br\//))
      lineErrors.push("URL fora do domínimo uspdigital");

    if (object.description.long.length == 0)
      lineErrors.push("Sem descrição longa");

    if (!Object.values(object.category).some((el) => el === true))
      lineErrors.push("Disciplina não pertence a nenhuma categoria");

    if (object.campus === "Todos")
      lineErrors.push("Nome de campus('Todos') não é válido");

    if (lineErrors.length > 0) {
      errors.push({
        id: object.name,
        line: index + 2,
        errors: lineErrors,
      });
    }
  });

  return errors;
};
