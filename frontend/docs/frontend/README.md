# Front End

[[TOC]]

::: tip Info
We're currently migrating from first version to the second one.
:::

## Domain

### Overview

It's a readable-only platform that displays information about 6 main entities -- Initiatives, P&D&I<sub>1</sub> , Professors, Disciplines, Companies and Patents. All the data comes from Google SpreadSheet, maintained by AUSPIN.

_<sub>1: P&D&I is an acronym for "Pesquisa & Desenvolvimento & Inovação".</sub>_

## First Version

This version doesn't use either backend or database applications. The page has two main functionalities:

- **Show:** Get the data from the Google SpreadSheet, store it temporarily, and display it on the page;
- **Filter:** Filter the data using filtering buttons and a [fuzzy search](https://en.wikipedia.org/wiki/Approximate_string_matching) mechanism.

The errors page has the only purpose of displaying the errors generated when the 'show' functionality presented above is called.

### Views

For each entity, the runtime view below shows the internal behavior:

<img src="/views/frontend.svg" alt="frontend view">

_Entity Class_ is responsible for the filtering buttons logic.

_Entity FindErrors_ is responsible for generating the errors displayed in the errors page.

## Second Version

::: tip info
Only Disciplines and Companies use this version yet.
:::

This version uses the API exposed by the backend application. The page has two main functionalities:

- **Show:** Get the data from the API, store it temporarily, and display it on the page;
- **Filter:** Filter the data using filtering buttons and a [fuzzy search](https://en.wikipedia.org/wiki/Approximate_string_matching) mechanism.

### Views

For each entity, the runtime view below shows the internal behavior:

<img src="/views/frontend_v2.svg" alt="frontend view_v2">

_Entity MatchesFilter_ is responsible for the filtering buttons logic.

_Indexer_ is responsible for generating the data without any accent.

[:arrow_left: Go back](/)
