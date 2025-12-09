import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.model.Document

def parsePlayers(eventDoc: Document): Players =
  val inPlayersId      = "zone_1"
  val outPlayersId     = "zone_2"
  val unknownPlayersId = "zone_3"

  val inPlayers      = namesIn(eventDoc, inPlayersId)
  val outPlayers     = namesIn(eventDoc, outPlayersId)
  val unknownPlayers = namesIn(eventDoc, unknownPlayersId)

  Players(inPlayers, outPlayers, unknownPlayers)

def namesIn(doc: Document, zoneId: String): List[String] =
  val query          = s"#$zoneId span.player_label"
  val playerElements = (doc >> elements(query)).toList
  playerElements.map(_.text.trim)

def parseEvents(archiveDoc: Document): List[Event] =
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
