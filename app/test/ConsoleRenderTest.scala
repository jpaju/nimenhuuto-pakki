import java.time.LocalDateTime

class ConsoleRenderTest extends munit.FunSuite:
  test("formatDate - formats as d.M.yyyy"):
    val date = LocalDateTime.parse("2025-01-07T21:00")
    assertEquals(ConsoleRender.formatDate(date), "07.01.2025")

  test("formatDate - double digit day and month"):
    val date = LocalDateTime.parse("2025-12-18T19:30")
    assertEquals(ConsoleRender.formatDate(date), "18.12.2025")
