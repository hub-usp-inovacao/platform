export const genFuzzyOptions = (keys, threshold = 0.15) => ({
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
