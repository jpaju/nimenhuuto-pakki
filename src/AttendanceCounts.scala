object AttendanceCounts:
  def perPlayer(attendances: List[EventAttendance]): Map[ShortName, Int] =
    attendances
      .flatMap(_.responses.in)
      .groupMapReduce(identity)(_ => 1)(_ + _)
