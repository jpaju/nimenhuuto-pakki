import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ConsoleRender:
  def attendanceResponses(responses: AttendanceResponses): Unit =
    println(s"In (${responses.in.size}): ${responses.in.mkString(", ")}")
    println(s"Out (${responses.out.size}): ${responses.out.mkString(", ")}")
    println(s"Unknown (${responses.unknown.size}): ${responses.unknown.mkString(", ")}")

  def event(e: Event): Unit =
    println(s"${formatDate(e.date)} - ${e.title} (${e.url})")

  def events(events: List[Event]): Unit =
    events.foreach(event)

  def eventAttendance(attendance: EventAttendance): Unit =
    val event = attendance.event
    val resp  = attendance.responses

    println(s"${event.title} - ${formatDate(event.date)}:")
    println(s"  In (${resp.in.size}): ${resp.in.mkString(", ")}")
    println(s"  Out (${resp.out.size}): ${resp.out.mkString(", ")}")
    println(s"  Unknown (${resp.unknown.size}): ${resp.unknown.mkString(", ")}")

  def eventAttendances(attendances: List[EventAttendance]): Unit =
    attendances.foreach(eventAttendance)

  def attendanceStats(stats: AttendanceStats): Unit =
    val (maxEvent, maxCount) = stats.mostAttended

    println(s"Total attendances: ${stats.totalAttendances}")
    println(s"Max attendance: ${formatDate(maxEvent.date)} ($maxCount players)")
    println(s"First event: ${formatDate(stats.firstEvent.date)} - ${stats.firstEvent.title}")
    println(s"Last event: ${formatDate(stats.lastEvent.date)} - ${stats.lastEvent.title}")
    println(f"Average attendance: ${stats.averageAttendance}%.1f")
    println()
    stats.playerStats.foreach(a => println(s"${a.name}: ${a.timesAttended}"))

  private val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")

  def formatDate(date: LocalDateTime): String =
    date.format(dateFormat)
