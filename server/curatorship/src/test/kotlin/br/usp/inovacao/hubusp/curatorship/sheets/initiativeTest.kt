package br.usp.inovacao.hubusp.curatorship.sheets

import kotlin.reflect.jvm.internal.ReflectProperties.Val
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

internal class initiativeTest {
    @Test
    fun `it parses correctly from a row in the spreadsheet`() {
        //given
        val row: List<String?> = InitiativeTestHelp.validRow

        //when
        val result = Initiative.fromRow(row)

        //then
        assertIs<Initiative>(result)
    }

    @Test
    fun `it throws when creating with invalid classification`() {
        assertFailsWith<ValidationException> {
            valid.copy(classification = "Wrong")
        }
    }

    @Test
    fun `it throws when creating with empty name`() {
        assertFailsWith<ValidationException> {
            valid.copy(name = "")
        }
    }

    @Test
    fun `it throws when creating with invalid campus`() {
        assertFailsWith<ValidationException> {
            valid.copy(localization = "Wrong")
        }
    }

    @Test
    fun `it throws when creating with empty set of tags`() {
        assertFailsWith<ValidationException> {
            valid.copy(tags = emptySet())
        }
    }

    @Test
    fun `it does not throw when creating with null url`() {
        // given
        val url = null

        // when
        val copy = valid.copy(url = url)

        // then
        assertIs<Initiative>(copy)
    }

    @Test
    fun `it throws when creating with empty url`() {
        assertFailsWith<ValidationException> {
            valid.copy(url = "")
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

        //then
        assertIs<Initiative>(copy)
    }

    @Test
    fun `it throws when creating with empty email`() {
        assertFailsWith<ValidationException> {
            valid.copy(email = "")
        }
    }

    @Test
    fun `it does not throw when creating with null contact info`() {
        // given
        val info = null
        val person = "Person"

        // when
        val copy = valid.copy(contact = InitiativeContact(info, person))

        // then
        assertIs<Initiative>(copy)
    }

    @Test
    fun `it throws when creating with empty contact info`() {
        assertFailsWith<ValidationException> {
            valid.copy(contact = InitiativeContact("", "Person"))
        }
    }

    @Test
    fun `it does not throw when creating with null contact person`() {
        // given
        val info = "(11) 5151-5151"
        val person = null

        // when
        val copy = valid.copy(contact = InitiativeContact(info, person))

        // then
        assertIs<Initiative>(copy)
    }

    @Test
    fun `it throws when creating with empty contact person`() {
        assertFailsWith<ValidationException> {
            valid.copy(contact = InitiativeContact("(11) 5151-5151", ""))
        }
    }

    private val valid = InitiativeTestHelp.VALID_RECORD.copy()
}