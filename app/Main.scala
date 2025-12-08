//> using scala "3.3.7"
//> using dep "net.ruippeixotog::scala-scraper:3.2.0"

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.model.Document

case class Players(in: List[String], out: List[String], unknown: List[String])
case class Event(id: String, url: String)

def getPlayers(doc: Document): Players =
  val inPlayersId      = "zone_1"
  val outPlayersId     = "zone_2"
  val unknownPlayersId = "zone_3"

  val inPlayers      = namesIn(doc, inPlayersId)
  val outPlayers     = namesIn(doc, outPlayersId)
  val unknownPlayers = namesIn(doc, unknownPlayersId)

  Players(inPlayers, outPlayers, unknownPlayers)

def namesIn(doc: Document, zoneId: String): List[String] =
  val query          = s"#$zoneId span.player_label"
  val playerElements = (doc >> elements(query)).toList

  playerElements.map(_.text.trim)

def getEventUrls(doc: Document): List[Event] =
  val links = (doc >> elements("a.event-title-link")).toList
  links.flatMap: link =>
    link
      .attr("href")
      .split("/events/")
      .lastOption
      .map(id => Event(id, link.attr("href")))

def fetchPage(url: String, sessionId: String): Document =
  val browser = JsoupBrowser.typed()
  browser.setCookie("ignored", "_session_id", "e45c6db30f3c8112500bd691eb3644e3")
  browser.get(url)

def requireEnvVar(name: String): String =
  sys.env.get(name) match
    case Some(value) => value
    case None        => exitWithError(s"$name env var not set")

def exitWithError(error: String): Nothing =
  System.err.println(error)
  sys.exit(1)

@main
def main(command: String, args: String*): Unit =
  val baseUrl   = requireEnvVar("BASE_URL")
  val sessionId = requireEnvVar("NIMENHUUTO_SESSION_ID")

  command match
    case "list-events" =>
      val archiveUrl = s"$baseUrl/events/archive"
      val doc        = fetchPage(archiveUrl, sessionId)
      val events     = getEventUrls(doc)
      events.foreach(e => println(e.url))

    case "show" =>
      if args.isEmpty then exitWithError("Usage: show <event-url>")
      val doc                       = fetchPage(args.head, sessionId)
      val Players(in, out, unknown) = getPlayers(doc)
      println(s"In (${in.size}): ${in.mkString(", ")}")
      println(s"Out (${out.size}): ${out.mkString(", ")}")
      println(s"Unknown (${unknown.size}): ${unknown.mkString(", ")}")

    case "count-attendance" =>
      val archiveUrl = s"$baseUrl/events/archive"
      val archiveDoc = fetchPage(archiveUrl, sessionId)
      val events     = getEventUrls(archiveDoc)

      events.foreach: event =>
        val doc     = fetchPage(event.url, sessionId)
        val players = getPlayers(doc)
        println(s"${event.id}:")
        println(s"  In (${players.in.size}): ${players.in.mkString(", ")}")
        println(s"  Out (${players.out.size}): ${players.out.mkString(", ")}")
        println(s"  Unknown (${players.unknown.size}): ${players.unknown.mkString(", ")}")

    case _ =>
      exitWithError(
        s"""Unknown command: '$command'
           |
           |Commands:
           |  - list-events
           |  - show <url>
           |  - count-attendance""".stripMargin
      )
