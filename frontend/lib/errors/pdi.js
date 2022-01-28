export const findErrors = function (objects) {
  const knownNames = {};
  const errors = [];

  objects.forEach((obj, index) => {
    const lineErrors = [];

    if (knownNames[obj.name]) {
      lineErrors.push("Nome duplicado");
    } else {
      knownNames[obj.name] = obj;
    }

    if (!obj.description.short || obj.description.short.length === 0) {
      lineErrors.push("Sem descrição curta");
    }

    if (!obj.description.long || obj.description.long.length === 0) {
      lineErrors.push("Sem descrição longa");
    }

    if (!obj.campus || obj.campus.length === 0) {
      lineErrors.push("Sem campus");
    }

    if (!obj.unity || obj.unity.length === 0) {
      lineErrors.push("Sem unidade");
    }

    if (!obj.keywords || obj.keywords.length === 0) {
      lineErrors.push("Sem tags");
    }

    if (lineErrors.length > 0) {
      errors.push({
        id: obj.name,
        line: index + 2,
        errors: lineErrors,
      });
    }
  });

  return errors;
};
