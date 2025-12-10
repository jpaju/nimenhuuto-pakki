object ConsoleRender:
  def attendanceResponses(responses: AttendanceResponses): Unit =
    println(s"In (${responses.in.size}): ${responses.in.mkString(", ")}")
    println(s"Out (${responses.out.size}): ${responses.out.mkString(", ")}")
    println(s"Unknown (${responses.unknown.size}): ${responses.unknown.mkString(", ")}")

  def event(e: Event): Unit =
    println(s"${e.date} - ${e.title} (${e.url})")

  def events(events: List[Event]): Unit =
    events.foreach(event)

  def eventAttendance(attendance: EventAttendance): Unit =
    val event = attendance.event
    val resp  = attendance.responses

    println(s"${event.title} - ${event.date}:")
    println(s"  In (${resp.in.size}): ${resp.in.mkString(", ")}")
    println(s"  Out (${resp.out.size}): ${resp.out.mkString(", ")}")
    println(s"  Unknown (${resp.unknown.size}): ${resp.unknown.mkString(", ")}")

  def eventAttendances(attendances: List[EventAttendance]): Unit =
    attendances.foreach(eventAttendance)

  def attendanceStats(stats: AttendanceStats): Unit =
    val (maxEvent, maxCount) = stats.mostAttended

    println(s"Total attendances: ${stats.totalAttendances}")
    println(s"Max attendance: ${maxEvent.date} ($maxCount players)")
    println(f"Average attendance: ${stats.averageAttendance}%.1f")
    println()
    stats.playerStats.foreach(a => println(s"${a.name}: ${a.timesAttended}"))
