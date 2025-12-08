import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.model.Document

def getPlayers(doc: Document): Players =
  val inPlayersId      = "zone_1"
  val outPlayersId     = "zone_2"
  val unknownPlayersId = "zone_3"

  val inPlayers      = namesIn(doc, inPlayersId)
  val outPlayers     = namesIn(doc, outPlayersId)
  val unknownPlayers = namesIn(doc, unknownPlayersId)

  Players(inPlayers, outPlayers, unknownPlayers)

def namesIn(doc: Document, zoneId: String): List[String] =
  val query          = s"#$zoneId span.player_label"
  val playerElements = (doc >> elements(query)).toList
  playerElements.map(_.text.trim)

def getEvents(doc: Document): List[Event] =
  val containers = (doc >> elements("div.event-detailed-container")).toList
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
