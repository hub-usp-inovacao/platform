const Fuse = require("fuse.js");

const generateOption = (keys, threshold = 0) => ({
  ignoreLocation: true,
  findAllMatches: true,
  shouldSort: true,
  tokenize: true,
  matchAllTokens: true,
  maxPatternLength: 32,
  minMatchCharLength: 2,
  threshold,
  keys,
});

function search(term, items, keys, threshold = 0) {
  const fuse = new Fuse(items, generateOption(keys, threshold));

  return fuse.search(term);
}

export default (context, inject) => {
  inject("fuzzySearch", (term, items, keys) => {
    context.store.dispatch("global/setStrictResults");

    if (!term) {
      return undefined;
    }

    let results = search(term, items, keys);

    if (!results.length) {
      context.store.dispatch("global/setFlexibleResults");
      results = search(term, items, keys, 0.15);
    }

    return results;
  });
};
