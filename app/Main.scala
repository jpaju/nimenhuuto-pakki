@main
def main(command: String, args: String*): Unit =
  val baseUrl   = requireEnvVar("BASE_URL")
  val sessionId = requireEnvVar("NIMENHUUTO_SESSION_ID")
  val client    = LiveNimenhuutoClient(baseUrl, sessionId)
  val commands  = Commands(client)

  command match
    case "list-events" =>
      commands.listEvents()

    case "show-event" =>
      if args.isEmpty then exitWithError("Usage: show-event <event-id>")
      else commands.showEvent(args.head)

    case "count-attendance" =>
      commands.countAttendance()

    case "event-history" =>
      commands.eventHistory()

    case "help" =>
      printHelp()

    case _ =>
      println(s"Unknown command: '$command'\n")
      printHelp()

def printHelp() =
  println(
    s"""Commands:
       |  - list-events
       |  - show-event <url>
       |  - count-attendance
       |  - event-history""".stripMargin
  )

def requireEnvVar(name: String): String =
  sys.env.get(name) match
    case Some(value) => value
    case None        => exitWithError(s"$name env var not set")

def exitWithError(error: String): Nothing =
  System.err.println(error)
  sys.exit(1)
