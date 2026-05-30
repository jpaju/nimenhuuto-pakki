class StatsTest extends munit.FunSuite:
  test("calculateAttendance - returns None for empty input"):
    assertEquals(Stats.calculateAttendance(Nil), None)

  test("calculateAttendance - totalEvents matches number of events"):
    val attendances = List(
      EventAttendance(event("1"), attendanceResponses(in = List("Alice"))),
      EventAttendance(event("2"), attendanceResponses(in = List("Bob"))),
      EventAttendance(event("3"), attendanceResponses(out = List("Alice")))
    )

    val stats = Stats.calculateAttendance(attendances).get

    assertEquals(stats.totalEvents, 3)
