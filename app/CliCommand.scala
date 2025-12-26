import com.monovore.decline.*

enum CliCommand:
  case ShowEvent(eventId: String)
  case ListEvents(count: Int)
  case CountAttendance(count: Int)
  case EventHistory(count: Int)

object CliCommand:
  private val defaultCount = 10
  private val countOption  = Opts.argument[Int]("count").withDefault(defaultCount)

  private val showEvent: Opts[CliCommand] =
    Opts.subcommand("show-event", "Show attendance for an event"):
      Opts.argument[String]("event-id").map(CliCommand.ShowEvent(_))

  private val listEvents: Opts[CliCommand] =
    Opts.subcommand("list-events", "List latest events"):
      countOption.map(CliCommand.ListEvents(_))

  private val countAttendance: Opts[CliCommand] =
    Opts.subcommand("count-attendance", "Count attendance statistics for latest events"):
      countOption.map(CliCommand.CountAttendance(_))

  private val eventHistory: Opts[CliCommand] =
    Opts.subcommand("event-history", "Show event attendance history"):
      countOption.map(CliCommand.EventHistory(_))

  val main: Command[CliCommand] =
    Command(name = "nh-toolkit", header = "Nimenhuuto toolkit"):
      showEvent
        .orElse(listEvents)
        .orElse(countAttendance)
        .orElse(eventHistory)
