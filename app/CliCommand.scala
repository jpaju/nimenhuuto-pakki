import com.monovore.decline.*
import com.monovore.decline.time.*
import cats.syntax.all.*
import java.time.*

enum CliCommand:
  case ListEvents(filter: EventFilter)
  case CountAttendance(filter: EventFilter)
  case EventHistory(filter: EventFilter)

object CliCommand:
  private val eventFilterOption: Opts[EventFilter] =
    val count     = Opts.option[Int]("count", "Number of events").map(EventFilter.ByCount(_))
    val since     = Opts.option[LocalDate]("since", "Events newer than date").map(EventFilter.NewerThan(_))
    val dateRange = (
      Opts.option[LocalDate]("since", "Start of date range"),
      Opts.option[LocalDate]("until", "End of date range")
    ).mapN(EventFilter.DateRange(_, _))
    count.orElse(dateRange).orElse(since)

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
      listEvents
        .orElse(countAttendance)
        .orElse(eventHistory)
