import java.time.LocalDateTime

case class Event(
    id: String,
    url: String,
    title: String,
    date: LocalDateTime
)

opaque type ShortName = String
def ShortName(value: String): ShortName = value

case class AttendanceResponses(
    in: List[ShortName],
    out: List[ShortName],
    unknown: List[ShortName]
)

case class EventAttendance(
    event: Event,
    responses: AttendanceResponses
)

case class PlayerStats(name: ShortName, timesAttended: Int)

case class AttendanceStats(
    totalAttendances: Int,
    mostAttended: (Event, Int),
    averageAttendance: Double,
    firstEvent: Event,
    lastEvent: Event,
    playerStats: List[PlayerStats]
)
