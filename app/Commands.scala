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

def countAttendance(baseUrl: String, sessionId: String): Unit =
  val archiveUrl = s"$baseUrl/events/archive"
  val archiveDoc = fetchPage(archiveUrl, sessionId)
  val events     = getEvents(archiveDoc)

  events.foreach: event =>
    val doc     = fetchPage(event.url, sessionId)
    val players = getPlayers(doc)
    println(s"${event.title} - ${event.date}:")
    println(s"  In (${players.in.size}): ${players.in.mkString(", ")}")
    println(s"  Out (${players.out.size}): ${players.out.mkString(", ")}")
    println(s"  Unknown (${players.unknown.size}): ${players.unknown.mkString(", ")}")
