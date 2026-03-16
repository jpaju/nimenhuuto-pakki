import java.time.LocalDateTime

case class Event(
    id: String,
    url: String,
    title: String,
    date: LocalDateTime
)

opaque type ShortName <: String = String
def ShortName(value: String): ShortName = value

opaque type LongName <: String = String
def LongName(value: String): LongName = value

opaque type Email <: String = String
def Email(value: String): Email = value

opaque type PhoneNumber <: String = String
def PhoneNumber(value: String): PhoneNumber = value

case class Player(name: LongName, email: Option[Email], phone: Option[PhoneNumber])

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
