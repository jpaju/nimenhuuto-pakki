class AttendanceCountsTest extends munit.FunSuite:
  test("from - no events yields no counts"):
    assertEquals(AttendanceCounts.perPlayer(List.empty), Map.empty[ShortName, Int])

  test("from - each in player from a single event counts once"):
    val attendances = List(
      EventAttendance(event("1"), attendanceResponses(in = List("Alice", "Bob")))
    )

    assertEquals(
      AttendanceCounts.perPlayer(attendances),
      Map(
        ShortName("Alice") -> 1,
        ShortName("Bob")   -> 1
      )
    )

  test("from - sums attendances across events"):
    val attendances = List(
      EventAttendance(event("1"), attendanceResponses(in = List("Alice", "Bob"))),
      EventAttendance(event("2"), attendanceResponses(in = List("Alice"))),
      EventAttendance(event("3"), attendanceResponses(in = List("Alice", "Bob")))
    )

    assertEquals(
      AttendanceCounts.perPlayer(attendances),
      Map(
        ShortName("Alice") -> 3,
        ShortName("Bob")   -> 2
      )
    )

  test("from - out and unknown responses are not counted"):
    val attendances = List(
      EventAttendance(
        event("1"),
        attendanceResponses(
          in = List("Alice"),
          out = List("Bob"),
          unknown = List("Jane")
        )
      )
    )

    assertEquals(
      AttendanceCounts.perPlayer(attendances),
      Map(ShortName("Alice") -> 1)
    )
