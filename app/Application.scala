class Application(service: NimenhuutoService):
  def showEvent(eventId: String): Unit =
    val responses = service.fetchAttendanceResponses(eventId)
    ConsoleRender.attendanceResponses(responses)

  def listEvents(count: Int): Unit =
    val events = service.listEvents(count)
    ConsoleRender.events(events)

  def eventHistory(count: Int): Unit =
    val attendances = service.fetchEventAttendances(count)
    ConsoleRender.eventAttendances(attendances)

  def countAttendance(count: Int): Unit =
    val attendances = service.fetchEventAttendances(count)
    Stats.calculateAttendance(attendances) match
      case Some(stats) => ConsoleRender.attendanceStats(stats)
      case None        => println("No events found")
