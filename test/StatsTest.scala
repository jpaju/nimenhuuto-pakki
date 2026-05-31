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

    assertEquals(stats.eventRange.totalEvents, 3)

  test("calculateAttendance - playerStats counts in-responses per player, excluding out and unknown"):
    val attendances = List(
      EventAttendance(event("1"), attendanceResponses(in = List("Alice", "Bob"), out = List("Jane"))),
      EventAttendance(event("2"), attendanceResponses(in = List("Alice"), unknown = List("Bob"))),
      EventAttendance(event("3"), attendanceResponses(out = List("Jane")))
    )

    val stats = Stats.calculateAttendance(attendances).get

    assertEquals(
      stats.playerStats.toSet,
      Set(playerStats("Alice", 2), playerStats("Bob", 1))
    )

  test("calculateAttendance - totalAttendances sums all in-responses across events"):
    val attendances = List(
      EventAttendance(event("1"), attendanceResponses(in = List("Alice", "Bob"), out = List("Jane"))),
      EventAttendance(event("2"), attendanceResponses(in = List("Alice"))),
      EventAttendance(event("3"), attendanceResponses(out = List("Bob")))
    )

    val stats = Stats.calculateAttendance(attendances).get

    assertEquals(stats.totalAttendances, 3)

  test("calculateAttendance - mostAttended is the event with the largest in-list"):
    val busiest     = event("2")
    val attendances = List(
      EventAttendance(event("1"), attendanceResponses(in = List("Alice"))),
      EventAttendance(busiest, attendanceResponses(in = List("Alice", "Bob", "Jane"))),
      EventAttendance(event("3"), attendanceResponses(out = List("Bob")))
    )

    val stats = Stats.calculateAttendance(attendances).get

    assertEquals(stats.mostAttended, (busiest, 3))

  test("calculateAttendance - eventRange picks firstEvent and lastEvent by date regardless of input order"):
    val earliest    = event("1", date = "2025-01-07T20:00")
    val latest      = event("2", date = "2025-12-18T20:00")
    val middle      = event("3", date = "2025-06-15T20:00")
    val attendances = List(
      EventAttendance(middle, attendanceResponses(in = List("Alice"))),
      EventAttendance(latest, attendanceResponses(in = List("Bob"))),
      EventAttendance(earliest, attendanceResponses(in = List("Jane")))
    )

    val stats = Stats.calculateAttendance(attendances).get

    assertEquals(stats.eventRange.firstEvent, earliest)
    assertEquals(stats.eventRange.lastEvent, latest)
