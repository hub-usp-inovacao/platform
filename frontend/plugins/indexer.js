export default (ctx, inject) => {
  inject("indexer", (name, allData) => {
    return allData.map((entity) => {
      entity.inspect = {};

      if (name == "companies") {
        entity.inspect.name = ctx.$removeAccent(entity["name"]);
        entity.inspect.description = {};
        entity.inspect.description.long = ctx.$removeAccent(
          entity.description.long
        );
        entity.inspect.services = ctx.$removeAccent(entity.services);
        entity.inspect.technologies = ctx.$removeAccent(entity.technologies);
      } else if (name == "disciplines") {
        entity.inspect.name = ctx.$removeAccent(entity["name"]);
        entity.inspect.description = {};
        entity.inspect.description.long = ctx.$removeAccent(
          entity.description.long
        );
        entity.inspect.description.short = ctx.$removeAccent(
          entity.description.short
        );
      } else if (name == "patents") {
        entity.inspect.name = ctx.$removeAccent(entity["name"]);
        entity.inspect.summary = ctx.$removeAccent(entity.summary);
        entity.inspect.owners = ctx.$removeAccent(entity.owners);
        entity.inspect.inventors = ctx.$removeAccent(entity.inventors);
        entity.inspect.ipcs = ctx.$removeAccent(entity.ipcs);
      }

      return entity;
    });
  });
};
