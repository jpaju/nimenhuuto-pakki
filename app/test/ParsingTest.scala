import java.time.LocalDateTime

class ParsingTest extends munit.FunSuite:
  test("parseDate - date without year uses provided default year"):
    val actual   = parseDate("Ti 9.12. klo 21:00", 2025)
    val expected = LocalDateTime.parse("2025-12-09T21:00")
    assertEquals(actual, Some(expected))

  test("parseDate - date with explicit year ignores default year"):
    val actual   = parseDate("Ti 17.12.2024 klo 21:00", 2025)
    val expected = LocalDateTime.parse("2024-12-17T21:00")
    assertEquals(actual, Some(expected))

  test("parseDate - single digit day and month"):
    val actual   = parseDate("Ti 7.1. klo 21:00", 2025)
    val expected = LocalDateTime.parse("2025-01-07T21:00")
    assertEquals(actual, Some(expected))

  test("parseDate - invalid input"):
    val actual = parseDate("foo", 2025)
    assertEquals(actual, None)
