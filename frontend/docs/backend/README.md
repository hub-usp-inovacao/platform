# Back End

[[TOC]]


## Context

The backend is a Web API built with [Ruby on Rails][rails] in API mode. This means that the API follows MVC architecture, except serving JSON instead of HTML.

The data inserted follows a different proccess: it's fetched from Google Spreadsheets. 

## View

The sequence diagram below shows how the data is fetched:

<img src="/views/backend.svg" alt="backend view">

- _User_ is the management agent; for now it's a manually executed role.  

- _Fetch_ is a Rake task stored in `/lib/tasks/fetch_spreadsheets.rake`

- _Get Entities_ represents a service responsible for fetching data from a specific Google Spreadsheets.

- _Entity_ represents a specific Rails model.

[:arrow_left: Go back](/)

[rails]: https://rubyonrails.org/