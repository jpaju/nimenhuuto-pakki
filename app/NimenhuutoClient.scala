import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Document

// TODO Error handling (missing events, network/authentication/parsing errors) ?
trait NimenhuutoClient:
  def getEvents(): List[Event]
  def getPlayers(eventId: String): Players
  def getEventHistory(): EventHistory

class LiveNimenhuutoClient(baseUrl: String, sessionId: String) extends NimenhuutoClient:
  def getEvents(): List[Event] =
    val archiveUrl = s"$baseUrl/events/archive"
    val archiveDoc = fetchPage(archiveUrl)
    parseEvents(archiveDoc)

  def getPlayers(eventId: String): Players =
    val url      = s"$baseUrl/events/$eventId"
    val eventDoc = fetchPage(url)
    parsePlayers(eventDoc)

  def getEventHistory(): EventHistory =
    val events = getEvents()
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
