@main
def main(command: String, args: String*): Unit =
  val baseUrl   = requireEnvVar("BASE_URL")
  val sessionId = requireEnvVar("NIMENHUUTO_SESSION_ID")

  val client   = LiveNimenhuutoClient(baseUrl, sessionId)
  val commands = Commands(client)

  val defaultCount = 10

  command match
    case "show-event" =>
      if args.isEmpty then exitWithError("Usage: show-event <event-id>")
      else commands.showEvent(args.head)

    case "list-events" =>
      val count = args.headOption.map(_.toInt).getOrElse(defaultCount)
      commands.listEvents(count)

    case "count-attendance" =>
      val count = args.headOption.map(_.toInt).getOrElse(defaultCount)
      commands.countAttendance(count)

    case "event-history" =>
      val count = args.headOption.map(_.toInt).getOrElse(defaultCount)
      commands.eventHistory(count)

    case "help" =>
      printHelp()

    case _ =>
      println(s"Unknown command: '$command'\n")
      printHelp()

def printHelp() =
  println(
    s"""Commands:
       |  - show-event <url>
       |  - list-events [count]
       |  - count-attendance [count]
       |  - event-history [count]""".stripMargin
  )

def requireEnvVar(name: String): String =
  sys.env.get(name) match
    case Some(value) => value
    case None        => exitWithError(s"$name env var not set")

def exitWithError(error: String): Nothing =
  System.err.println(error)
  sys.exit(1)
