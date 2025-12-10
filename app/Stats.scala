def calculateAttendanceStats(attendances: List[EventAttendance]): Option[AttendanceStats] =
  attendances
    .maxByOption(_.players.in.size)
    .map { mostAttendedEvent =>
      val inPlayers         = attendances.flatMap(_.players.in)
      val totalAttendances  = inPlayers.size
      val mostAttended      = (mostAttendedEvent.event, mostAttendedEvent.players.in.size)
      val averageAttendance = inPlayers.size.toDouble / attendances.size

      val playerAttendances = inPlayers
        .groupBy(identity)
        .view
        .mapValues(_.size)
        .toList
        .sortBy(-_._2)

      AttendanceStats(
        totalAttendances,
        mostAttended,
        averageAttendance,
        playerAttendances
      )
    }
