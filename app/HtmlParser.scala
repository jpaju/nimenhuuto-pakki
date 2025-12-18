import java.time.LocalDateTime

import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.model.Document

object HtmlParser:
  def attendanceResponses(eventDoc: Document): AttendanceResponses =
    val inPlayersId      = "zone_1"
    val outPlayersId     = "zone_2"
    val unknownPlayersId = "zone_3"

    val inPlayers      = namesIn(eventDoc, inPlayersId)
    val outPlayers     = namesIn(eventDoc, outPlayersId)
    val unknownPlayers = namesIn(eventDoc, unknownPlayersId)

    AttendanceResponses(inPlayers, outPlayers, unknownPlayers)

  def archiveEvents(archiveDoc: Document, defaultYear: Int): List[Event] =
    val eventContainers = (archiveDoc >> elements("div.event-detailed-container")).toList
    eventContainers.flatMap: eventContainer =>
      val linkOpt = (eventContainer >?> element("a.event-title-link"))
      val dateOpt = (eventContainer >?> element("h4"))

      for
        url     <- linkOpt.map(_.attr("href"))
        id      <- url.split("/events/").lastOption
        title   <- linkOpt.map(_.text.trim)
        dateStr <- dateOpt.map(_.text.trim)
        date     = parseDate(dateStr, defaultYear).getOrElse(sys.error(s"Failed to parse date: $dateStr"))
      yield Event(id, url, title, date)

  private def namesIn(doc: Document, zoneId: String): List[PlayerName] =
    val query          = s"#$zoneId span.player_label"
    val playerElements = (doc >> elements(query)).toList

    playerElements
      .map(_.text.trim)
      .map(PlayerName(_))

  def parseDate(dateStr: String, defaultYear: Int): Option[LocalDateTime] =
    dateStr match
      case s"$_ $day.$month. klo $hour:$minute" =>
        toLocalDateTime(defaultYear.toString, month, day, hour, minute)

      case s"$_ $day.$month.$year klo $hour:$minute" =>
        toLocalDateTime(year, month, day, hour, minute)

      case _ =>
        None

  private def toLocalDateTime(yearStr: String, monthStr: String, dayStr: String, hourStr: String, minuteStr: String) =
    for
      year   <- yearStr.toIntOption
      month  <- monthStr.toIntOption
      day    <- dayStr.toIntOption
      hour   <- hourStr.toIntOption
      minute <- minuteStr.toIntOption
    yield LocalDateTime.of(year, month, day, hour, minute)
