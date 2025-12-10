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
    val attendances =
      client
        .fetchEventAttendances()
        .take(count)
        .toList

    val inPlayers        = attendances.flatMap(_.players.in)
    val totalAttendances = inPlayers.size
    val maxAttendance    =
      attendances
        .maxByOption(_.players.in.size)
        .map((a) => s"${a.event.date} (${a.players.in.size} players)")
        .getOrElse("-")

    val avgAttendance =
      Option(attendances)
        .filter(_.nonEmpty)
        .map(attendances => f"${totalAttendances.toDouble / attendances.size}%.1f")
        .getOrElse("-")

    val counts =
      inPlayers
        .groupBy(identity)
        .view
        .mapValues(_.size)
        .toList
        .sortBy(-_._2)

    println(s"Total attendances: $totalAttendances")
    println(s"Max attendance: $maxAttendance")
    println(s"Average attendance: $avgAttendance")
    println()
    counts.foreach((name, count) => println(s"$name: $count"))
