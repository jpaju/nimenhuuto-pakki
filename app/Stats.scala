object Stats:
  def calculateAttendance(attendances: List[EventAttendance]): Option[AttendanceStats] =
    attendances
      .maxByOption(_.players.in.size)
      .map { mostAttendedEvent =>
        val inPlayers         = attendances.flatMap(_.players.in)
        val totalAttendances  = inPlayers.size
        val mostAttended      = (mostAttendedEvent.event, mostAttendedEvent.players.in.size)
        val averageAttendance = inPlayers.size.toDouble / attendances.size

        val playerAttendances = countPlayerAttendances(inPlayers)

        AttendanceStats(
          totalAttendances = totalAttendances,
          mostAttended = mostAttended,
          averageAttendance = averageAttendance,
          playerAttendances = playerAttendances
        )
      }

  private def countPlayerAttendances(inPlayers: List[String]): List[PlayerAttendances] =
    inPlayers
      .groupBy(identity)
      .view
      .map((player, foo) => PlayerAttendances(player, foo.size))
      .toList
      .sortBy(_.attendances)(Ordering.Int.reverse)
