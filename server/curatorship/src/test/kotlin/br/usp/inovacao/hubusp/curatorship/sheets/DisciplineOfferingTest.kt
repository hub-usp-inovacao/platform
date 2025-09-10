package br.usp.inovacao.hubusp.curatorship.sheets

import it.skrape.fetcher.BlockingFetcher
import it.skrape.fetcher.Request
import it.skrape.fetcher.Result
import it.skrape.selects.html5.*
import kotlin.test.Test
import kotlin.test.assertEquals

internal class DisciplineOfferingTest {
    private class MockFetcher(val mockedResponse: String) : BlockingFetcher<Request> {
        override val requestBuilder: Request
            get() = Request()

        override fun fetch(request: Request) =
            Result(
                responseBody = this.mockedResponse,
                responseStatus = Result.Status(200, "OK"),
                contentType = "text/html",
                headers = emptyMap(),
                baseUri = "",
                cookies = emptyList(),
            )
    }

    @Test
    fun `it can parse HTML to multiple offerings`() {
        val htmlWithMultipleOfferings =
            """
<html>
  <body>
    <table border="0" cellpadding="2" cellspacing="2">
      <tbody>
        <tr>
          <td><b><font><span>Código da Turma:</span></font></b></td>
          <td><font><span>2025202&nbsp;</span></font></td>
        </tr>
        <tr>
          <td><b><font><span>Início:</span></font></b></td>
          <td><font><span>04/08/2025</span></font></td>
        </tr>
        <tr>
          <td><b><font><span>Fim:</span></font></b></td>
          <td><font><span>12/12/2025</span></font></td>
        </tr>
        <tr>
          <td><b><font><span>Tipo da Turma:</span></font></b></td>
          <td><font><span>Prática</span></font></td>
        </tr>
      </tbody>
    </table>
    <table border="0" cellpadding="2" cellspacing="2">
      <tbody>
        <tr>
          <td><b><font><span>Código da Turma:</span></font></b></td>
          <td><font><span>1234&nbsp;</span></font></td>
        </tr>
        <tr>
          <td><b><font><span>Início:</span></font></b></td>
          <td><font><span>01/02/2022</span></font></td>
        </tr>
        <tr>
          <td><b><font><span>Fim:</span></font></b></td>
          <td><font><span>02/03/2023</span></font></td>
        </tr>
        <tr>
          <td><b><font><span>Tipo da Turma:</span></font></b></td>
          <td><font><span>Teórica</span></font></td>
        </tr>
      </tbody>
    </table>
  </body>
</html>
            """

        val offerings =
            DisciplineOffering.trySetFromJupiter("", 10000, MockFetcher(htmlWithMultipleOfferings))
        assertEquals(
            offerings,
            setOf(
                DisciplineOffering(
                    classCode = "2025202",
                    startDate = "04/08/2025",
                    endDate = "12/12/2025",
                ),
                DisciplineOffering(
                    classCode = "1234",
                    startDate = "01/02/2022",
                    endDate = "02/03/2023",
                ),
            ),
        )
    }

    @Test
    fun `it can parse HTML to a valid offering`() {
        val htmlWithSingleOffering =
            """
<html>
  <body>
    <table border="0" cellpadding="2" cellspacing="2">
      <tbody>
        <tr>
          <td><b><font><span>Código da Turma:</span></font></b></td>
          <td><font><span>2025202&nbsp;</span></font></td>
        </tr>
        <tr>
          <td><b><font><span>Início:</span></font></b></td>
          <td><font><span>04/08/2025</span></font></td>
        </tr>
        <tr>
          <td><b><font><span>Fim:</span></font></b></td>
          <td><font><span>12/12/2025</span></font></td>
        </tr>
        <tr>
          <td><b><font><span>Tipo da Turma:</span></font></b></td>
          <td><font><span>Prática</span></font></td>
        </tr>
      </tbody>
    </table>
  </body>
</html>
            """

        val offerings =
            DisciplineOffering.trySetFromJupiter("", 10000, MockFetcher(htmlWithSingleOffering))
        assertEquals(
            offerings,
            setOf(
                DisciplineOffering(
                    classCode = "2025202",
                    startDate = "04/08/2025",
                    endDate = "12/12/2025",
                ),
            ),
        )
    }
}
