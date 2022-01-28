function matchesFilter(iniciatives, { primary, terciary }) {
  let primaryMatch = true;
  let terciaryMatch = true;

  if (primary.length > 0)
    primaryMatch = primary.some(
      (p) => p.toLowerCase() === iniciatives.category.toLowerCase()
    );

  const [campus] = terciary;

  if (campus) terciaryMatch = iniciatives.local === campus;

  return primaryMatch && terciaryMatch;
}

export default (_, inject) => {
  inject("iniciativeMatchesFilter", matchesFilter);
};