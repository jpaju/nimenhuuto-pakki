case class Players(
    in: List[String],
    out: List[String],
    unknown: List[String]
)

case class Event(
    id: String,
    url: String,
    title: String,
    date: String
)

case class EventAttendance(
    event: Event,
    players: Players
)

case class PlayerAttendances(player: String, attendances: Int)

case class AttendanceStats(
    totalAttendances: Int,
    mostAttended: (Event, Int),
    averageAttendance: Double,
    playerAttendances: List[PlayerAttendances]
)
