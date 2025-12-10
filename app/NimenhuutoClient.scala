import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Document

// TODO Error handling (missing events, network/authentication/parsing errors) ?
trait NimenhuutoClient:
  def fetchAttendanceResponses(eventId: String): AttendanceResponses
  def fetchEvents(): Iterator[Event]
  def fetchEventAttendances(): Iterator[EventAttendance]

class LiveNimenhuutoClient(baseUrl: String, sessionId: String) extends NimenhuutoClient:
  def fetchAttendanceResponses(eventId: String): AttendanceResponses =
    val url      = s"$baseUrl/events/$eventId"
    val eventDoc = fetchPage(url)
    parseAttendanceResponses(eventDoc)

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
      val responses = fetchAttendanceResponses(event.id)
      EventAttendance(event, responses)
    }

  private def fetchPage(url: String): Document =
    val browser = JsoupBrowser.typed() // TODO Share browser instance?
    browser.setCookie("ignored", "_session_id", sessionId)
    browser.get(url)
