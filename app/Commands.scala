def listEvents(baseUrl: String, sessionId: String): Unit =
  val archiveUrl = s"$baseUrl/events/archive"
  val doc        = fetchPage(archiveUrl, sessionId)
  val events     = getEvents(doc)
  events.foreach(e => println(s"${e.date} - ${e.title} (${e.url})"))

def showEvent(url: String, sessionId: String): Unit =
  val doc                       = fetchPage(url, sessionId)
  val Players(in, out, unknown) = getPlayers(doc)
  println(s"In (${in.size}): ${in.mkString(", ")}")
  println(s"Out (${out.size}): ${out.mkString(", ")}")
  println(s"Unknown (${unknown.size}): ${unknown.mkString(", ")}")

def eventHistory(baseUrl: String, sessionId: String): Unit =
  val history = getEventHistory(baseUrl, sessionId)
  history.events.foreach: (event, players) =>
    println(s"${event.title} - ${event.date}:")
    println(s"  In (${players.in.size}): ${players.in.mkString(", ")}")
    println(s"  Out (${players.out.size}): ${players.out.mkString(", ")}")
    println(s"  Unknown (${players.unknown.size}): ${players.unknown.mkString(", ")}")

def countAttendance(baseUrl: String, sessionId: String): Unit =
  val history = getEventHistory(baseUrl, sessionId)
  val counts = history.events
    .flatMap((_, players) => players.in)
    .groupBy(identity)
    .view.mapValues(_.size)
    .toList
    .sortBy(-_._2)

  counts.foreach((name, count) => println(s"$name: $count"))

// ================================= HELPERS =================================

def getEventHistory(baseUrl: String, sessionId: String): EventHistory =
  val archiveUrl = s"$baseUrl/events/archive"
  val archiveDoc = fetchPage(archiveUrl, sessionId)
  val events     = getEvents(archiveDoc)

  EventHistory(
    events
      .map: event =>
        val doc     = fetchPage(event.url, sessionId)
        val players = getPlayers(doc)
        event -> players
      .toMap
  )
