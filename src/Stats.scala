object Stats:
  def calculateAttendance(attendances: List[EventAttendance]): Option[AttendanceStats] =
    attendances
      .maxByOption(_.responses.in.size)
      .map { mostAttendedEvent =>
        val inPlayers         = attendances.flatMap(_.responses.in)
        val totalEvents       = attendances.size
        val totalAttendances  = inPlayers.size
        val mostAttended      = (mostAttendedEvent.event, mostAttendedEvent.responses.in.size)
        val averageAttendance = inPlayers.size.toDouble / totalEvents

        val eventRange  = buildEventRange(attendances)
        val playerStats = countPlayerStats(inPlayers)

        AttendanceStats(
          totalAttendances = totalAttendances,
          mostAttended = mostAttended,
          averageAttendance = averageAttendance,
          eventRange = eventRange,
          playerStats = playerStats
        )
      }

  private def countPlayerStats(inPlayers: List[ShortName]): List[PlayerStats] =
    inPlayers
      .groupBy(identity)
      .view
      .map((player, countList) => PlayerStats(player, countList.size))
      .toList
      .sortBy(_.timesAttended)(Ordering.Int.reverse)

  private def buildEventRange(attendances: List[EventAttendance]): EventRange =
    val events = attendances.map(_.event)
    EventRange(
      firstEvent = events.minBy(_.date),
      lastEvent = events.maxBy(_.date),
      totalEvents = events.size
    )
