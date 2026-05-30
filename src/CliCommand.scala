import com.monovore.decline.*
import com.monovore.decline.time.*
import cats.data.*
import cats.syntax.all.*
import java.time.*

enum CliCommand:
  case ListEvents(filter: EventFilter)
  case CountAttendance(filter: EventFilter)
  case EventHistory(filter: EventFilter)
  case ShowRoster
  case DistributeBill(filter: EventFilter, bill: Bill)

object CliCommand:
  // ====================================== Argument parsers ======================================
  private given Argument[Money] = Argument.from("amount"): str =>
    Validated.fromOption(
      Money.euros(str),
      ifNone = NonEmptyList.of(s"Invalid amount: '$str'. Expected an amount in euros, e.g. 45.50 or 45,50.")
    )

  // ======================================= Shared options =======================================
  private val byCount: Opts[EventFilter] =
    Opts.option[Int]("count", "Number of events").map(EventFilter.ByCount(_))

  private val dateRange: Opts[EventFilter] = (
    Opts.option[LocalDate]("since", "Start of date range"),
    Opts.option[LocalDate]("until", "End of date range")
  ).mapN(EventFilter.DateRange(_, _))

  private val newerThan: Opts[EventFilter] =
    Opts.option[LocalDate]("since", "Events newer than date").map(EventFilter.NewerThan(_))

  private val eventFilterOption: Opts[EventFilter] =
    byCount // dateRange must be before newerThan because both consume --since.
      .orElse(dateRange)
      .orElse(newerThan)

  // ========================================= Subcommands =========================================
  private val listEvents: Opts[CliCommand] =
    Opts.subcommand("list-events", "List latest events"):
      eventFilterOption.map(CliCommand.ListEvents(_))

  private val countAttendance: Opts[CliCommand] =
    Opts.subcommand("count-attendance", "Count attendance statistics for latest events"):
      eventFilterOption.map(CliCommand.CountAttendance(_))

  private val eventHistory: Opts[CliCommand] =
    Opts.subcommand("event-history", "Show event attendance history"):
      eventFilterOption.map(CliCommand.EventHistory(_))

  private val showRoster: Opts[CliCommand] =
    Opts.subcommand("show-roster", "Show team roster with contact info"):
      Opts(CliCommand.ShowRoster)

  private val totalBillOption: Opts[Bill] =
    Opts.option[Money]("total-bill", "Total bill to distribute").map(Bill.Total(_))

  private val perEventBillOption: Opts[Bill] =
    Opts.option[Money]("per-event-bill", "Fixed bill per event; total is derived").map(Bill.PerEvent(_))

  private val billOption: Opts[Bill] =
    totalBillOption.orElse(perEventBillOption)

  private val distributeBill: Opts[CliCommand] =
    Opts.subcommand("distribute-bill", "Distribute a bill across players by attendance"):
      (eventFilterOption, billOption).mapN(CliCommand.DistributeBill(_, _))

// ========================================== Entry point ==========================================
  val main: Command[CliCommand] =
    Command(name = "nh-toolkit", header = "Nimenhuuto toolkit"):
      listEvents
        .orElse(countAttendance)
        .orElse(eventHistory)
        .orElse(showRoster)
        .orElse(distributeBill)
