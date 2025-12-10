class Commands(client: NimenhuutoClient):
  def showEvent(eventId: String): Unit =
    val responses = client.fetchAttendanceResponses(eventId)
    ConsoleRender.attendanceResponses(responses)

  def listEvents(count: Int): Unit =
    val events = client
      .fetchEvents()
      .take(count)
      .toList

    ConsoleRender.events(events)

  def eventHistory(count: Int): Unit =
    val attendances = client
      .fetchEventAttendances()
      .take(count)
      .toList

    ConsoleRender.eventAttendances(attendances)

  def countAttendance(count: Int): Unit =
    val attendances = client
      .fetchEventAttendances()
      .take(count)
      .toList

    Stats.calculateAttendance(attendances) match
      case Some(stats) => ConsoleRender.attendanceStats(stats)
      case None        => println("No events found")
