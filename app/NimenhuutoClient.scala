import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Document

// TODO Error handling (missing events, network/authentication/parsing errors) ?
trait NimenhuutoClient:
  def getEvents(): Iterator[Event]
  def getPlayers(eventId: String): Players
  def getEventHistory(): EventHistory

class LiveNimenhuutoClient(baseUrl: String, sessionId: String) extends NimenhuutoClient:
  def getEvents(): Iterator[Event] =
    Iterator
      .unfold(1): page =>
        val doc    = fetchPage(s"$baseUrl/events/archive?page=$page")
        val events = parseEvents(doc)
        if events.isEmpty then None
        else Some((events, page + 1))
      .flatten

  def getPlayers(eventId: String): Players =
    val url      = s"$baseUrl/events/$eventId"
    val eventDoc = fetchPage(url)
    parsePlayers(eventDoc)

  def getEventHistory(): EventHistory =
    val events = getEvents().take(10).toList // TODO: make lazy
    EventHistory(
      events
        .map: event =>
          val players = getPlayers(event.id)
          event -> players
        .toMap
    )

  private def fetchPage(url: String): Document =
    val browser = JsoupBrowser.typed() // TODO Share browser instance?
    browser.setCookie("ignored", "_session_id", sessionId)
    browser.get(url)
