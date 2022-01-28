function phoneMatch(phones) {
  const phoneRgx = /^(\(\d{2}\) )?\d{4,5}-\d{4}$/;
  return phones.every((phone) => phone.match(phoneRgx));
}

function keywordSyntaxeError(keywords) {
  const syntaxeErrorRgx = /[/|]/;
  return keywords.some((keyword) => keyword.match(syntaxeErrorRgx));
}

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

    if (!object.category || object.category.length == 0)
      lineErrors.push("Sem classificação");

    if (!object.description.short || object.description.short.length == 0)
      lineErrors.push("Sem descrição curta");

    if (!object.description.long || object.description.long.length == 0)
      lineErrors.push("Sem descrição longa");

    if (!object.keywords || object.keywords.length == 0)
      lineErrors.push("Sem tags ou palavras chave");
    else if (keywordSyntaxeError(object.keywords))
      lineErrors.push("Erro na escrita das tags ou palavras chave");

    if (!phoneMatch(object.contact.info))
      lineErrors.push("Erro de formatação do número de contato");

    if (!object.url.match(/^http(s)?:\/\//))
      lineErrors.push("URL não é um link");

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
