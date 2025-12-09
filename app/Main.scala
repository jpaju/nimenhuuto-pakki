@main
def main(command: String, args: String*): Unit =
  val baseUrl   = requireEnvVar("BASE_URL")
  val sessionId = requireEnvVar("NIMENHUUTO_SESSION_ID")

  command match
    case "list-events" =>
      listEvents(baseUrl, sessionId)

    case "show-event" =>
      if args.isEmpty then exitWithError("Usage: show <event-url>")
      else showEvent(args.head, sessionId)

    case "count-attendance" =>
      countAttendance(baseUrl, sessionId)

    case "event-history" =>
      eventHistory(baseUrl, sessionId)

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
