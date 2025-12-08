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

@main
def main(path: String): Unit =
  val browser = JsoupBrowser()
  val doc     = browser.parseFile(path)

  val Players(in, out, unknown) = getPlayers(doc)

  println(s"In (${in.size}): ${in.mkString(", ")}")
  println(s"Out (${out.size}): ${out.mkString(", ")}")
  println(s"Unknown (${unknown.size}): ${unknown.mkString(", ")}")
