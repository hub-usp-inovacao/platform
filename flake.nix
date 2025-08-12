{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    backend.url = "path:backend";
    caddy.url = "path:caddy";
    docs.url = "path:docs";
    frontend.url = "path:frontend";
    server.url = "path:server";
  };

  outputs =
    {
      nixpkgs,
      systems,
      ...
    }@inputs:
    let
      inherit (nixpkgs) lib;
      forAllSystems = lib.genAttrs (import systems);
    in
    {
      devShells = forAllSystems (
        system:
        lib.genAttrs [ "backend" "caddy" "docs" "frontend" "server" ] (
          project: inputs.${project}.devShells.${system}.default
        )
      );
    };
}
