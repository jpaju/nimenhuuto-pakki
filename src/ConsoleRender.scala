import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ConsoleRender:
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

    printSeparator()
    println(s"Total attendances: ${stats.totalAttendances}")
    println(f"Average attendance: ${stats.averageAttendance}%.1f")
    println(s"Max attendance: ${formatDate(maxEvent.date)} ($maxCount players)")
    printEventRange(stats)
    println()
    stats.playerStats.foreach(a => println(s"${a.name}: ${a.timesAttended}"))

  def roster(players: List[Player]): Unit =
    if players.nonEmpty then
      val header = List("Name", "Email", "Phone")
      val rows   = players.map: p =>
        List(
          p.name.toString,
          p.email.getOrElse("-"),
          p.phone.getOrElse("-")
        )

      println(Tabulator.format(header :: rows))

  def distribution(bill: Money, stats: AttendanceStats, distribution: Distribution[ShortName]): Unit =
    val collected = distribution.shares.values.reduce(_ + _)

    printEventRange(stats)
    println(s"Bill: ${bill.render}")
    println(s"Cost per attendance: ${distribution.unitCost.render}")
    println(s"Collected: ${collected.render}")
    printSeparator()

    val header = List("Player", "Attendances", "Share")
    val rows   = stats.playerStats
      .map: playerStats =>
        List(
          playerStats.name.toString,
          playerStats.timesAttended.toString,
          distribution.shares(playerStats.name).render
        )

    println(Tabulator.format(header :: rows))

  private val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")

  def formatDate(date: LocalDateTime): String =
    date.format(dateFormat)

  private def printSeparator() =
    println("-" * 50)

  private def printEventRange(stats: AttendanceStats): Unit =
    printSeparator()
    println(s"First event: ${formatDate(stats.firstEvent.date)}")
    println(s"Last event: ${formatDate(stats.lastEvent.date)}")
    println(s"Total events: ${stats.totalEvents}")
    printSeparator()

// Shamelessly copied from https://stackoverflow.com/questions/7539831/scala-draw-table-to-console
private object Tabulator:
  def format(table: List[List[String]]): String =
    val colSizes = table.transpose.map(_.map(_.length).max + 2)
    val rows     = table.map(row => formatRow(row, colSizes))
    val top      = colSizes.map("─" * _).mkString("╭", "┬", "╮")
    val mid      = colSizes.map("─" * _).mkString("├", "┼", "┤")
    val bot      = colSizes.map("─" * _).mkString("╰", "┴", "╯")

    (top :: rows.head :: mid :: rows.tail ::: List(bot)).mkString("\n")

  private def formatRow(row: List[String], colSizes: List[Int]): String =
    row
      .zip(colSizes)
      .map((item, size) => s" %-${size - 1}s".format(item))
      .mkString("│", "│", "│")
