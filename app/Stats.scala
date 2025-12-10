object Stats:
  def calculateAttendance(attendances: List[EventAttendance]): Option[AttendanceStats] =
    attendances
      .maxByOption(_.responses.in.size)
      .map { mostAttendedEvent =>
        val inPlayers         = attendances.flatMap(_.responses.in)
        val totalAttendances  = inPlayers.size
        val mostAttended      = (mostAttendedEvent.event, mostAttendedEvent.responses.in.size)
        val averageAttendance = inPlayers.size.toDouble / attendances.size

        val playerAttendances = countPlayerStats(inPlayers)

        AttendanceStats(
          totalAttendances = totalAttendances,
          mostAttended = mostAttended,
          averageAttendance = averageAttendance,
          playerStats = playerAttendances
        )
      }

  private def countPlayerStats(inPlayers: List[PlayerName]): List[PlayerStats] =
    inPlayers
      .groupBy(identity)
      .view
      .map((player, foo) => PlayerStats(player, foo.size))
      .toList
      .sortBy(_.timesAttended)(Ordering.Int.reverse)
