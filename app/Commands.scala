class Commands(client: NimenhuutoClient):
  def showEvent(eventId: String): Unit =
    val Players(in, out, unknown) = client.fetchPlayers(eventId)
    println(s"In (${in.size}): ${in.mkString(", ")}")
    println(s"Out (${out.size}): ${out.mkString(", ")}")
    println(s"Unknown (${unknown.size}): ${unknown.mkString(", ")}")

  def listEvents(count: Int): Unit =
    client
      .fetchEvents()
      .take(count)
      .foreach(event => println(s"${event.date} - ${event.title} (${event.url})"))

  def eventHistory(count: Int): Unit =
    client
      .fetchEventAttendances()
      .take(count)
      .foreach { attendance =>
        val event   = attendance.event
        val players = attendance.players

        println(s"${event.title} - ${event.date}:")
        println(s"  In (${players.in.size}): ${players.in.mkString(", ")}")
        println(s"  Out (${players.out.size}): ${players.out.mkString(", ")}")
        println(s"  Unknown (${players.unknown.size}): ${players.unknown.mkString(", ")}")
      }

  def countAttendance(count: Int): Unit =
    val attendances = client
      .fetchEventAttendances()
      .take(count)
      .toList

    calculateAttendanceStats(attendances) match
      case None        => println("No events found")
      case Some(stats) =>
        val (maxEvent, maxCount) = stats.maxAttendance
        println(s"Total attendances: ${stats.totalAttendances}")
        println(s"Max attendance: ${maxEvent.date} ($maxCount players)")
        println(f"Average attendance: ${stats.avgAttendance}%.1f")
        println()
        stats.playerCounts.foreach((name, count) => println(s"$name: $count"))
