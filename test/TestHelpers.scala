import java.time.LocalDateTime

def attendanceResponses(
    in: List[String] = Nil,
    out: List[String] = Nil,
    unknown: List[String] = Nil
): AttendanceResponses =
  AttendanceResponses(in.map(ShortName(_)), out.map(ShortName(_)), unknown.map(ShortName(_)))

def event(id: String, date: String = "2025-01-01T20:00"): Event =
  Event(id, s"/$id", "Harkka", LocalDateTime.parse(date))

def euros(value: String): Money =
  Money
    .euros(value)
    .getOrElse(sys.error(s"invalid money: $value"))
