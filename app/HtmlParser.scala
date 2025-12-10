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

  def archiveEvents(archiveDoc: Document): List[Event] =
    val containers = (archiveDoc >> elements("div.event-detailed-container")).toList
    containers.flatMap: container =>
      val linkOpt  = (container >?> element("a.event-title-link"))
      val titleOpt = linkOpt.map(_.text.trim)
      val urlOpt   = linkOpt.map(_.attr("href"))
      val idOpt    = urlOpt.flatMap(_.split("/events/").lastOption)
      val dateOpt  = (container >?> element("h4")).map(_.text.trim)

      for
        id    <- idOpt
        url   <- urlOpt
        title <- titleOpt
        date  <- dateOpt
      yield Event(id, url, title, date)

  def parseDate(dateStr: String, defaultYear: Int): Option[LocalDateTime] =
    dateStr match
      case s"$_ $day.$month. klo $hour:$minute" =>
        toLocalDateTime(defaultYear.toString, month, day, hour, minute)

      case s"$_ $day.$month.$year klo $hour:$minute" =>
        toLocalDateTime(year, month, day, hour, minute)

      case _ =>
        None

  private def namesIn(doc: Document, zoneId: String): List[PlayerName] =
    val query          = s"#$zoneId span.player_label"
    val playerElements = (doc >> elements(query)).toList
    playerElements.map(_.text.trim).map(PlayerName(_))

  private def toLocalDateTime(yearStr: String, monthStr: String, dayStr: String, hourStr: String, minuteStr: String) =
    for
      year   <- yearStr.toIntOption
      month  <- monthStr.toIntOption
      day    <- dayStr.toIntOption
      hour   <- hourStr.toIntOption
      minute <- minuteStr.toIntOption
    yield LocalDateTime.of(year, month, day, hour, minute)
