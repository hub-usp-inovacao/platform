package br.usp.inovacao.hubusp.curatorship.sheets

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

internal class PDITest {
    @Test
    fun `it throws when creating with invalid category`() {
        assertFailsWith<ValidationException> {
            valid.copy(category = "Wrong")
        }
    }

    @Test
    fun `it throws when creating with empty name`() {
        assertFailsWith<ValidationException> {
            valid.copy(name = "")
        }
    }

    @Test
    fun `it throws when creating with empty unity`() {
        assertFailsWith<ValidationException> {
            valid.copy(unity = "")
        }
    }

    @Test
    fun `it throws when creating with invalid unity`() {
        assertFailsWith<ValidationException> {
            valid.copy(unity = "Wrong")
        }
    }

    @Test
    fun `it throws when creating with empty campus`() {
        assertFailsWith<ValidationException> {
            valid.copy(campus = "")
        }
    }

    @Test
    fun `it throws when creating with invalid campus`() {
        assertFailsWith<ValidationException> {
            valid.copy(campus = "Wrong")
        }
    }

    @Test
    fun `it throws when creating with empty description`() {
        assertFailsWith<ValidationException> {
            valid.copy(description = "")
        }
    }

    @Test
    fun `it does not throw when creating with null email`() {
         // given
        val email = null

        // when
        val copy = valid.copy(email = email)

        // then
        assertIs<PDI>(copy)
    }

    @Test
    fun `it throws when creating with empty email`() {
        assertFailsWith<ValidationException> {
            valid.copy(campus = "")
        }
    }

    @Test
    fun `it does not throw when creating with null phone`() {
        // given
        val phone = null

        // when
        val copy = valid.copy(phone = phone)

        // then
        assertIs<PDI>(copy)
    }

    @Test
    fun `it throws when creating with empty phone`() {
        assertFailsWith<ValidationException> {
            valid.copy(phone = "")
        }
    }

    @Test
    fun `it does not throw when creating with null site`() {
        // given
        val site = null

        // when
        val copy = valid.copy(site = site)

        // then
        assertIs<PDI>(copy)
    }

    @Test
    fun `it throws when creating with empty site`() {
        assertFailsWith<ValidationException> {
            valid.copy(site = "")
        }
    }

    @Test
    fun `it throws when creating with empty set of keywords`() {
        assertFailsWith<ValidationException> {
            valid.copy(keywords = emptySet())
        }
    }

    private val valid = PDI(
        category = "CEPID",
        name = "Flau flau",
        unity = "Escola de Artes, Ciências e Humanidades - EACH",
        campus = "USP Leste",
        coordinator = null,
        phone = null,
        email = null,
        description = "lorem ipsum",
        site = null,
        keywords = setOf("foo", "baz")
    )
}