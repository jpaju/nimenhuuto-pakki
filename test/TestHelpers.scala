import java.time.LocalDateTime

def attendanceResponses(
    in: List[String] = Nil,
    out: List[String] = Nil,
    unknown: List[String] = Nil
): AttendanceResponses =
  AttendanceResponses(in.map(ShortName(_)), out.map(ShortName(_)), unknown.map(ShortName(_)))

def event(id: String, date: String): Event =
  Event(id, s"/$id", "Harkka", LocalDateTime.parse(date))
