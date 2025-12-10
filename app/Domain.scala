case class Event(
    id: String,
    url: String,
    title: String,
    date: String
)

opaque type PlayerName = String
def PlayerName(value: String): PlayerName = value

case class AttendanceResponses(
    in: List[PlayerName],
    out: List[PlayerName],
    unknown: List[PlayerName]
)

case class EventAttendance(
    event: Event,
    responses: AttendanceResponses
)

case class PlayerStats(name: PlayerName, timesAttended: Int)

case class AttendanceStats(
    totalAttendances: Int,
    mostAttended: (Event, Int),
    averageAttendance: Double,
    playerStats: List[PlayerStats]
)
