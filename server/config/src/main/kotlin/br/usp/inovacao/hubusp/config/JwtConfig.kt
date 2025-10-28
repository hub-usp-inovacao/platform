package br.usp.inovacao.hubusp.config

data class JwtConfig(
    var secret : String = "",
    var issuer : String = "",
    var domain : String = "",
    var audience : String = "",
    var realm : String = "",
)
