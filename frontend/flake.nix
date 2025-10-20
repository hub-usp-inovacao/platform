{
  inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
  inputs.nixpkgs-nodejs.url = "github:NixOS/nixpkgs/6b5019a48f876f3288efc626fa8b70ad0c64eb46";

  outputs =
    {
      nixpkgs,
      nixpkgs-nodejs,
      systems,
      ...
    }:
    let
      inherit (nixpkgs) lib;
      forAllSystems = lib.genAttrs (import systems);
      pkgsFor = forAllSystems (
        system:
        import nixpkgs {
          inherit system;
          config = {
            # TODO: Remove once node-sass is gone
            permittedInsecurePackages = [
              "python-2.7.18.8"
            ];
          };
        }
      );
      nodePkgsFor = forAllSystems (
        system:
        import nixpkgs-nodejs {
          inherit system;
          config = {
            # TODO: Remove once node-sass is gone
            permittedInsecurePackages = [
              "nodejs-14.21.1"
              "openssl-1.1.1w"
            ];
          };
        }
      );
    in
    {
      devShells = forAllSystems (
        system:
        let
          pkgs = pkgsFor.${system};
        in
        {
          default = pkgs.callPackage ./nix/shell.nix {
            yarn = pkgs.yarn.override {
              nodejs = nodePkgsFor.${system}.nodejs-14_x;
            };
          };
        }
      );
    };
}
