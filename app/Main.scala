//> using scala "3.3.7"
//> using dep "net.ruippeixotog::scala-scraper:3.2.0"

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*

@main
def main(path: String): Unit =
  val browser = JsoupBrowser()
  val doc = browser.parseFile(path)
  val title = doc >> text("title")
  println(title)
