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

  def showRoster(): Unit =
    val players = service.fetchPlayers()
    ConsoleRender.roster(players)

  def distributeBill(filter: EventFilter, totalBill: Money): Unit =
    val attendances = service.fetchEventAttendances(filter)
    val counts      = AttendanceCounts.perPlayer(attendances)

    val rendered = for
      stats        <- Stats.calculateAttendance(attendances)
      distribution <- CostDistribution.perAttendance(totalBill, counts)
    yield ConsoleRender.distribution(totalBill, stats, distribution)

    if rendered.isEmpty then println("No attendances found")
