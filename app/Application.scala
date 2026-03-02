class Application(service: NimenhuutoService):
  def listEvents(filter: EventFilter): Unit =
    val events = service.listEvents(filter)
    ConsoleRender.events(events)

  def eventHistory(filter: EventFilter): Unit =
    val attendances = service.fetchEventAttendances(filter)
    ConsoleRender.eventAttendances(attendances)

  def countAttendance(filter: EventFilter): Unit =
    val attendances = service.fetchEventAttendances(filter)

    Stats.calculateAttendance(attendances) match
      case Some(stats) => ConsoleRender.attendanceStats(stats)
      case None        => println("No events found")
