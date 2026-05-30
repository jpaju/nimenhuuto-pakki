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
    printEventRange(stats.eventRange)
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

  def distribution(billDistribution: BillDistribution): Unit =
    printEventRange(billDistribution.eventRange)
    println(s"Bill: ${billDistribution.bill.render}")
    println(s"Cost per attendance: ${billDistribution.unitCost.render}")
    println(s"Collected: ${billDistribution.collected.render}")
    printSeparator()

    val header = List("Player", "Attendances", "Share")
    val rows   = billDistribution.playerShares.map: ps =>
      List(ps.name.toString, ps.attendances.toString, ps.share.render)

    println(Tabulator.format(header :: rows))

  private val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")

  def formatDate(date: LocalDateTime): String =
    date.format(dateFormat)

  private def printSeparator() =
    println("-" * 50)

  private def printEventRange(eventRange: EventRange): Unit =
    printSeparator()
    println(s"First event: ${formatDate(eventRange.firstEvent.date)}")
    println(s"Last event: ${formatDate(eventRange.lastEvent.date)}")
    println(s"Total events: ${eventRange.totalEvents}")
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
