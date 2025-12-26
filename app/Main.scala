import com.monovore.decline.*

@main
def main(args: String*): Unit =
  val baseUrl   = requireEnvVar("BASE_URL")
  val sessionId = requireEnvVar("NIMENHUUTO_SESSION_ID")

  val client = LiveNimenhuutoClient(baseUrl, sessionId)
  val app    = Application(client)

  CliCommand.main.parse(args) match
    case Left(help) => showHelp(help)
    case Right(cmd) => runCommand(app)(cmd)

private def showHelp(help: Help) =
  val (output, exitCode) = help.errors match
    case Nil => System.out -> 0
    case _   => System.err -> 1

  output.println(help)
  sys.exit(exitCode)

private def runCommand(app: Application): CliCommand => Unit =
  case CliCommand.ShowEvent(eventId)     => app.showEvent(eventId)
  case CliCommand.ListEvents(count)      => app.listEvents(count)
  case CliCommand.CountAttendance(count) => app.countAttendance(count)
  case CliCommand.EventHistory(count)    => app.eventHistory(count)

private def requireEnvVar(name: String): String =
  sys.env.get(name).getOrElse {
    System.err.println(s"$name env var not set")
    sys.exit(1)
  }
