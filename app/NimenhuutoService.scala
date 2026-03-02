import java.time.LocalDate
import java.time.LocalDateTime

class NimenhuutoService(client: NimenhuutoClient):
  def listEvents(filter: EventFilter): List[Event] =
    val events = client.fetchEvents()
    applyFilter(filter, events, _.date).toList

  def fetchEventAttendances(filter: EventFilter): List[EventAttendance] =
    val events = client.fetchEventAttendances()
    applyFilter(filter, events, _.event.date).toList

  private def applyFilter[A](filter: EventFilter, events: Iterator[A], getDate: A => LocalDateTime) =
    filter match
      case EventFilter.ByCount(n)              => events.take(n)
      case EventFilter.NewerThan(date)         => events.takeWhile(a => !getDate(a).toLocalDate.isBefore(date))
      case EventFilter.DateRange(since, until) =>
        events
          .dropWhile(a => getDate(a).toLocalDate.isAfter(until))
          .takeWhile(a => !getDate(a).toLocalDate.isBefore(since))

enum EventFilter:
  case ByCount(n: Int)
  case NewerThan(date: LocalDate)
  case DateRange(since: LocalDate, until: LocalDate)
