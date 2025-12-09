class Commands(client: NimenhuutoClient):
  def showEvent(eventId: String): Unit =
    val Players(in, out, unknown) = client.getPlayers(eventId)
    println(s"In (${in.size}): ${in.mkString(", ")}")
    println(s"Out (${out.size}): ${out.mkString(", ")}")
    println(s"Unknown (${unknown.size}): ${unknown.mkString(", ")}")

  def listEvents(count: Int): Unit =
    client
      .getEvents()
      .take(count)
      .foreach(event => println(s"${event.date} - ${event.title} (${event.url})"))

  def eventHistory(count: Int): Unit =
    client
      .getEventAttendance()
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
    val counts = client // TODO Refactor this monster
      .getEventAttendance()
      .take(count)
      .flatMap(_.players.in)
      .toList
      .groupBy(identity)
      .view
      .mapValues(_.size)
      .toList
      .sortBy(-_._2)

    counts.foreach((name, count) => println(s"$name: $count"))
