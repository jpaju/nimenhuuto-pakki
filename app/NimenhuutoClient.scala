import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Document

// TODO Error handling (missing events, network/authentication/parsing errors) ?
trait NimenhuutoClient:
  def getPlayers(eventId: String): Players
  def getEvents(): Iterator[Event]
  def getEventAttendance(): Iterator[EventAttendance]

class LiveNimenhuutoClient(baseUrl: String, sessionId: String) extends NimenhuutoClient:
  def getPlayers(eventId: String): Players =
    val url      = s"$baseUrl/events/$eventId"
    val eventDoc = fetchPage(url)
    parsePlayers(eventDoc)

  def getEvents(): Iterator[Event] =
    Iterator
      .unfold(1) { page =>
        val doc    = fetchPage(s"$baseUrl/events/archive?page=$page")
        val events = parseEvents(doc)
        if events.isEmpty then None
        else Some((events, page + 1))
      }
      .flatten

  def getEventAttendance(): Iterator[EventAttendance] =
    getEvents().map { event =>
      val players = getPlayers(event.id)
      EventAttendance(event, players)
    }

  private def fetchPage(url: String): Document =
    val browser = JsoupBrowser.typed() // TODO Share browser instance?
    browser.setCookie("ignored", "_session_id", sessionId)
    browser.get(url)
