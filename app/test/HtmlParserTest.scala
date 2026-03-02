import java.io.File
import java.time.LocalDateTime

import net.ruippeixotog.scalascraper.browser.JsoupBrowser

class HtmlParserTest extends munit.FunSuite:
  test("attendanceResponses - parses event page correctly"):
    val doc      = readDocumentFromFile("data/event.html")

    val actual   = HtmlParser.attendanceResponses(doc)
    val expected = AttendanceResponses(
      in      = List("Alice A", "Bob B", "Charlie C").map(PlayerName(_)),
      out     = List("Diana D", "Eve E").map(PlayerName(_)),
      unknown = List("Frank F", "Grace G").map(PlayerName(_))
    )

    assertEquals(actual, expected)

  test("archiveEvents - parses archive page correctly"):
    val doc      = readDocumentFromFile("data/archive.html")

    val actual   = HtmlParser.archiveEvents(doc, defaultYear = 2025)
    val expected = List(
      Event("19457290", "https://exampleteam.nimenhuuto.com/events/19457290", "Harkka", LocalDateTime.of(2025, 12, 2, 21, 0)),
      Event("19457289", "https://exampleteam.nimenhuuto.com/events/19457289", "Harkka", LocalDateTime.of(2025, 11, 25, 21, 0))
    )

    assertEquals(actual, expected)

private def readDocumentFromFile(filePath: String) =
  val browser = JsoupBrowser.typed()
  browser.parseFile(new File(filePath))

