class Commands(client: NimenhuutoClient):
  def listEvents(): Unit =
    val events = client.getEvents()
    events.foreach(e => println(s"${e.date} - ${e.title} (${e.url})"))

  def showEvent(eventId: String): Unit =
    val Players(in, out, unknown) = client.getPlayers(eventId)
    println(s"In (${in.size}): ${in.mkString(", ")}")
    println(s"Out (${out.size}): ${out.mkString(", ")}")
    println(s"Unknown (${unknown.size}): ${unknown.mkString(", ")}")

  def eventHistory(): Unit =
    val history = client.getEventHistory()
    history.events.foreach: (event, players) =>
      println(s"${event.title} - ${event.date}:")
      println(s"  In (${players.in.size}): ${players.in.mkString(", ")}")
      println(s"  Out (${players.out.size}): ${players.out.mkString(", ")}")
      println(s"  Unknown (${players.unknown.size}): ${players.unknown.mkString(", ")}")

  def countAttendance(): Unit =
    val history = client.getEventHistory()
    val counts  = history.events
      .flatMap((_, players) => players.in)
      .groupBy(identity)
      .view
      .mapValues(_.size)
      .toList
      .sortBy(-_._2)

    counts.foreach((name, count) => println(s"$name: $count"))
