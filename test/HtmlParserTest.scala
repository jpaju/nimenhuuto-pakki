import java.io.File
import java.time.LocalDateTime

import net.ruippeixotog.scalascraper.browser.JsoupBrowser

class HtmlParserTest extends munit.FunSuite:
  private val baseDataPath = "test/data"

  test("attendanceResponses - parses event page correctly"):
    val doc = readDocumentFromFile(s"$baseDataPath/event.html")

    val actual   = HtmlParser.attendanceResponses(doc)
    val expected = AttendanceResponses(
      in = List("Alice A", "Bob B", "Charlie C").map(ShortName(_)),
      out = List("Diana D", "Eve E").map(ShortName(_)),
      unknown = List("Frank F", "Grace G").map(ShortName(_))
    )

    assertEquals(actual, expected)

  test("archiveEvents - parses archive page correctly"):
    val doc = readDocumentFromFile(s"$baseDataPath/archive.html")

    val actual   = HtmlParser.archiveEvents(doc, defaultYear = 2025)
    val expected = List(
      Event(
        "19457290",
        "https://exampleteam.nimenhuuto.com/events/19457290",
        "Harkka",
        LocalDateTime.of(2025, 12, 2, 21, 0)
      ),
      Event(
        "19457289",
        "https://exampleteam.nimenhuuto.com/events/19457289",
        "Harkka",
        LocalDateTime.of(2025, 11, 25, 21, 0)
      )
    )

    assertEquals(actual, expected)

  test("players - parses players page correctly"):
    val doc = readDocumentFromFile(s"$baseDataPath/players.html")

    val actual   = HtmlParser.players(doc)
    val expected = List(
      Player(
        LongName("Alice Anderson"),
        Some(Email("alice@example.com")),
        Some(PhoneNumber("+358 40 111 2233"))
      ),
      Player(
        LongName("Bob Brown"),
        Some(Email("bob@example.com")),
        Some(PhoneNumber("+358 40 123 4567"))
      ),
      Player(
        LongName("Charlie Clark"),
        Some(Email("charlie@example.com")),
        Some(PhoneNumber("+358 50 987 6543"))
      )
    )

    assertEquals(actual, expected)

private def readDocumentFromFile(filePath: String) =
  val browser = JsoupBrowser.typed()
  browser.parseFile(new File(filePath))
