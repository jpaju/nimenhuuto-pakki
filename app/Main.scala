//> using scala "3.3.7"
//> using dep "net.ruippeixotog::scala-scraper:3.2.0"

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.model.Document

case class Players(in: List[String], out: List[String], unknown: List[String])

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

def readSessionId(): String =
  val sessionId = sys.env.getOrElse("NIMENHUUTO_SESSION_ID", "")
  if sessionId.isEmpty then
    System.err.println("NIMENHUUTO_SESSION_ID env var not set")
    System.exit(1)

  sessionId

def fetchPage(url: String, sessionId: String): Document =
  val browser = JsoupBrowser.typed()
  browser.setCookie("ignored", "_session_id", "e45c6db30f3c8112500bd691eb3644e3")
  browser.get(url)

@main
def main(url: String): Unit =
  val sessionId = readSessionId()

  val doc                       = fetchPage(url, sessionId)
  val Players(in, out, unknown) = getPlayers(doc)

  println(s"In (${in.size}): ${in.mkString(", ")}")
  println(s"Out (${out.size}): ${out.mkString(", ")}")
  println(s"Unknown (${unknown.size}): ${unknown.mkString(", ")}")
