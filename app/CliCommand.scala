import com.monovore.decline.*
import com.monovore.decline.time.*
import java.time.LocalDate

enum CliCommand:
  case ShowEvent(eventId: String)
  case ListEvents(filter: EventFilter)
  case CountAttendance(filter: EventFilter)
  case EventHistory(filter: EventFilter)

object CliCommand:
  private val eventFilterOption: Opts[EventFilter] =
    val count = Opts.option[Int]("count", "Number of events").map(EventFilter.ByCount(_))
    val since = Opts.option[LocalDate]("since", "Events newer than date").map(EventFilter.NewerThan(_))
    count.orElse(since)

  private val showEvent: Opts[CliCommand] =
    Opts.subcommand("show-event", "Show attendance for an event"):
      Opts.argument[String]("event-id").map(CliCommand.ShowEvent(_))

  private val listEvents: Opts[CliCommand] =
    Opts.subcommand("list-events", "List latest events"):
      eventFilterOption.map(CliCommand.ListEvents(_))

  private val countAttendance: Opts[CliCommand] =
    Opts.subcommand("count-attendance", "Count attendance statistics for latest events"):
      eventFilterOption.map(CliCommand.CountAttendance(_))

  private val eventHistory: Opts[CliCommand] =
    Opts.subcommand("event-history", "Show event attendance history"):
      eventFilterOption.map(CliCommand.EventHistory(_))

  val main: Command[CliCommand] =
    Command(name = "nh-toolkit", header = "Nimenhuuto toolkit"):
      showEvent
        .orElse(listEvents)
        .orElse(countAttendance)
        .orElse(eventHistory)
