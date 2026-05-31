import java.time.LocalDateTime

def attendanceResponses(
    in: List[String] = Nil,
    out: List[String] = Nil,
    unknown: List[String] = Nil
): AttendanceResponses =
  AttendanceResponses(in.map(ShortName(_)), out.map(ShortName(_)), unknown.map(ShortName(_)))

def event(id: String, date: String = "2025-01-01T20:00"): Event =
  Event(id, s"/$id", "Harkka", LocalDateTime.parse(date))

def playerStats(name: String, timesAttended: Int): PlayerStats =
  PlayerStats(ShortName(name), timesAttended)

def attendanceStats(
    playerStats: List[PlayerStats] = Nil,
    eventRange: EventRange = EventRange(event("1"), event("2"), totalEvents = 1)
): AttendanceStats =
  AttendanceStats(
    totalAttendances = 0,
    mostAttended = (eventRange.firstEvent, 0),
    averageAttendance = 0.0,
    eventRange = eventRange,
    playerStats = playerStats
  )

def euros(value: String): Money =
  Money
    .euros(value)
    .getOrElse(sys.error(s"invalid money: $value"))
