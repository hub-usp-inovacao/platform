{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    nixpkgs-bundler.url = "github:NixOS/nixpkgs/b60793b86201040d9dee019a05089a9150d08b5b";
    nixpkgs-ruby.url = "github:NixOS/nixpkgs/0cb2fd7c59fed0cd82ef858cbcbdb552b9a33465";
  };

  outputs =
    {
      nixpkgs,
      nixpkgs-bundler,
      nixpkgs-ruby,
      systems,
      ...
    }:
    let
      inherit (nixpkgs) lib;
      forAllSystems = lib.genAttrs (import systems);
      pkgsFor = forAllSystems (system: nixpkgs.legacyPackages.${system});
    in
    {
      devShells = forAllSystems (
        system:
        let
          pkgs = pkgsFor.${system};
        in
        {
          default = pkgs.callPackage ./nix/shell.nix {
            bundler = nixpkgs-bundler.legacyPackages.${system}.bundler.override {
              ruby = nixpkgs-ruby.legacyPackages.${system}.ruby_3_3;
            };
          };
        }
      );
    };
}
