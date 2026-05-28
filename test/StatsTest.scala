class StatsTest extends munit.FunSuite:
  test("calculateAttendance - returns None for empty input"):
    assertEquals(Stats.calculateAttendance(Nil), None)

  test("calculateAttendance - totalEvents matches number of events"):
    val attendances = List(
      EventAttendance(event("1", "2025-01-10T20:00"), attendanceResponses(in = List("Alice"))),
      EventAttendance(event("2", "2025-01-09T20:00"), attendanceResponses(in = List("Bob"))),
      EventAttendance(event("3", "2025-01-08T20:00"), attendanceResponses(out = List("Alice")))
    )

    val stats = Stats.calculateAttendance(attendances).get

    assertEquals(stats.totalEvents, 3)
