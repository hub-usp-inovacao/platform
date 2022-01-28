const matchesFilter = (discipline, { primary, terciary }) => {
  const categories = discipline.keywords;
  let primaryMatch = true;
  let terciaryMatch = true;

  if (primary.length > 0) {
    primaryMatch = categories.some((cat) => primary.includes(cat));
  }

  const [campus, unity, level, nature, period] = terciary;

  if (campus != undefined) {
    terciaryMatch = discipline.campus === campus;
  }

  if (unity != undefined) {
    terciaryMatch = discipline.unity === unity;
  }

  if (level != undefined) {
    terciaryMatch = terciaryMatch && discipline.level === level;
  }

  if (nature != undefined) {
    terciaryMatch =
      terciaryMatch && discipline.nature.toLowerCase() === nature.toLowerCase();
  }

  if (period != undefined) {
    terciaryMatch = terciaryMatch && discipline.offeringPeriod === period;
  }

  return primaryMatch && terciaryMatch;
};

export default (_, inject) => {
  inject("disciplineMatchesFilter", matchesFilter);
};
