object Stats:
  def calculateAttendance(attendances: List[EventAttendance]): Option[AttendanceStats] =
    attendances
      .maxByOption(_.responses.in.size)
      .map { mostAttendedEvent =>
        val inPlayers         = attendances.flatMap(_.responses.in)
        val totalAttendances  = inPlayers.size
        val mostAttended      = (mostAttendedEvent.event, mostAttendedEvent.responses.in.size)
        val averageAttendance = inPlayers.size.toDouble / attendances.size

        val (firstEvent, lastEvent) = findFirstAndLastEvent(attendances)
        val playerStats             = countPlayerStats(inPlayers)

        AttendanceStats(
          totalAttendances = totalAttendances,
          mostAttended = mostAttended,
          averageAttendance = averageAttendance,
          firstEvent = firstEvent,
          lastEvent = lastEvent,
          playerStats = playerStats
        )
      }

  private def countPlayerStats(inPlayers: List[PlayerName]): List[PlayerStats] =
    inPlayers
      .groupBy(identity)
      .view
      .map((player, countList) => PlayerStats(player, countList.size))
      .toList
      .sortBy(_.timesAttended)(Ordering.Int.reverse)

  private def findFirstAndLastEvent(attendances: List[EventAttendance]): (Event, Event) =
    val firstEvent = attendances.map(_.event).minBy(_.date)
    val lastEvent  = attendances.map(_.event).maxBy(_.date)

    firstEvent -> lastEvent
