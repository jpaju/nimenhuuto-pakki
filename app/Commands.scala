class Commands(client: NimenhuutoClient):
  def showEvent(eventId: String): Unit =
    val players = client.fetchPlayers(eventId)
    Render.players(players)

  def listEvents(count: Int): Unit =
    val events = client
      .fetchEvents()
      .take(count)
      .toList

    Render.events(events)

  def eventHistory(count: Int): Unit =
    val attendances = client
      .fetchEventAttendances()
      .take(count)
      .toList

    Render.eventAttendances(attendances)

  def countAttendance(count: Int): Unit =
    val attendances = client
      .fetchEventAttendances()
      .take(count)
      .toList

    calculateAttendanceStats(attendances) match
      case Some(stats) => Render.attendanceStats(stats)
      case None        => println("No events found")
