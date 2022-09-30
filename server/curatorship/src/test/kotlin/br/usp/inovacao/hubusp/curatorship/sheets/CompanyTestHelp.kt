package br.usp.inovacao.hubusp.curatorship.sheets

class CompanyTestHelp {
    companion object {
        val validSample: Company = Company(
            cnpj = "12.123.123/0001-21"
        )

        fun noValidRow(): List<List<String?>> {
            val row = mutableListOf<String?>()

            for (i in 0..100) {
                row.add(index = i, element = "foo")
            }

            return listOf(row)
        }

        fun someValidRows(): List<List<String?>> {
            return listOf(singleValidRow())
        }

        fun singleValidRow(): List<String?> {
            return listOf(
                null,
                "12.123.123/0001-21",
                "Any Name",
                "Noname Mock Company",
                "2022",
                "01.21-1-01",
                null,
                "foo@mail.com;baz@example.com",
                "Rua das Couves, 38",
                "Jardim de Fora",
                "São Paulo",
                "São Paulo",
                "04434-020",
                "Short description of the company, say no more",
                "product 1; product 2",
                null,
                null,
                null,
                "Não",
                null,
                null,
                "138",
                null,
                "Sim",
                "foo@mail.com",
                "Foo de Bar Baz",
                null,
                "Estou de acordo com tal afirmação",
                null,
                "Foo de Bar Baz",
                "3738645",
                "Aluno ou ex-aluno (pós-graduação)",
                "Instituto de Matemática e Estatística - IME",
                "Gerente",
                "foo@mail.com",
                null,
                "1",
                "Sim",
                "Não",
                null,
                null,
                null,
                null,
                "Não",
                null,
                null,
                null,
                null,
                "Não",
                null,
                null,
                null,
                null,
                "Não",
                null,
                null,
                null,
                null,
                "138",
                "0",
                "0",
                "Não",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "R$12.000.000",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "ATIVA",
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
        }
    }
}