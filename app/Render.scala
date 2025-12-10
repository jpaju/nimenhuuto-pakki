object Render:
  def players(players: Players): Unit =
    println(s"In (${players.in.size}): ${players.in.mkString(", ")}")
    println(s"Out (${players.out.size}): ${players.out.mkString(", ")}")
    println(s"Unknown (${players.unknown.size}): ${players.unknown.mkString(", ")}")

  def event(e: Event): Unit =
    println(s"${e.date} - ${e.title} (${e.url})")

  def events(events: List[Event]): Unit =
    events.foreach(event)

  def eventAttendance(attendance: EventAttendance): Unit =
    val e = attendance.event
    val p = attendance.players

    println(s"${e.title} - ${e.date}:")
    println(s"  In (${p.in.size}): ${p.in.mkString(", ")}")
    println(s"  Out (${p.out.size}): ${p.out.mkString(", ")}")
    println(s"  Unknown (${p.unknown.size}): ${p.unknown.mkString(", ")}")

  def eventAttendances(attendances: List[EventAttendance]): Unit =
    attendances.foreach(eventAttendance)

  def attendanceStats(stats: AttendanceStats): Unit =
    val (maxEvent, maxCount) = stats.maxAttendance

    println(s"Total attendances: ${stats.totalAttendances}")
    println(s"Max attendance: ${maxEvent.date} ($maxCount players)")
    println(f"Average attendance: ${stats.avgAttendance}%.1f")
    println()
    stats.playerCounts.foreach((name, count) => println(s"$name: $count"))
