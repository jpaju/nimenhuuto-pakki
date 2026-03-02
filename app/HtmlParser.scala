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
        date     = DateParser.parseDate(dateStr, defaultYear).getOrElse(sys.error(s"Failed to parse date: $dateStr"))
      yield Event(id, url, title, date)

  def players(playersDoc: Document): List[Player] =
    val playerCards = (playersDoc >> elements("div.playercard")).toList
    playerCards.flatMap: card =>
      val h3Opt     = card >?> element("h3")
      val smallText = h3Opt.flatMap(_ >?> element("small")).map(_.text).getOrElse("")
      val nameOpt   = h3Opt.map(_.text.replace(smallText, "").trim)
      val emailOpt  = (card >?> element("span.emai")).map(_.text.trim)
      val phoneOpt  = (card >?> element("a[href^=tel://]")).map(_.text.trim)

      nameOpt
        .filter(_.nonEmpty)
        .map: name =>
          Player(
            name = LongName(name),
            email = emailOpt.map(Email(_)),
            phone = phoneOpt.map(PhoneNumber(_))
          )

  private def namesIn(doc: Document, zoneId: String): List[ShortName] =
    val query          = s"#$zoneId span.player_label"
    val playerElements = (doc >> elements(query)).toList

    playerElements
      .map(_.text.trim)
      .map(ShortName(_))
