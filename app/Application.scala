class Application(service: NimenhuutoService):
  def showEvent(eventId: String): Unit =
    val responses = service.fetchAttendanceResponses(eventId)
    ConsoleRender.attendanceResponses(responses)

  def listEvents(count: Int): Unit =
    val filter = EventFilter.ByCount(count)
    val events = service.listEvents(filter)
    ConsoleRender.events(events)

  def eventHistory(count: Int): Unit =
    val filter      = EventFilter.ByCount(count)
    val attendances = service.fetchEventAttendances(filter)
    ConsoleRender.eventAttendances(attendances)

  def countAttendance(count: Int): Unit =
    val filter      = EventFilter.ByCount(count)
    val attendances = service.fetchEventAttendances(filter)

    Stats.calculateAttendance(attendances) match
      case Some(stats) => ConsoleRender.attendanceStats(stats)
      case None        => println("No events found")
