import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Document

// TODO Error handling (missing events, network/authentication/parsing errors) ?
trait NimenhuutoClient:
  def fetchPlayers(eventId: String): Players
  def fetchEvents(): Iterator[Event]
  def fetchEventAttendances(): Iterator[EventAttendance]

class LiveNimenhuutoClient(baseUrl: String, sessionId: String) extends NimenhuutoClient:
  def fetchPlayers(eventId: String): Players =
    val url      = s"$baseUrl/events/$eventId"
    val eventDoc = fetchPage(url)
    parsePlayers(eventDoc)

  def fetchEvents(): Iterator[Event] =
    Iterator
      .unfold(1) { page =>
        val doc    = fetchPage(s"$baseUrl/events/archive?page=$page")
        val events = parseEvents(doc)
        if events.isEmpty then None
        else Some((events, page + 1))
      }
      .flatten

  def fetchEventAttendances(): Iterator[EventAttendance] =
    fetchEvents().map { event =>
      val players = fetchPlayers(event.id)
      EventAttendance(event, players)
    }

  private def fetchPage(url: String): Document =
    val browser = JsoupBrowser.typed() // TODO Share browser instance?
    browser.setCookie("ignored", "_session_id", sessionId)
    browser.get(url)
