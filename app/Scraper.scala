import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Document

def fetchPage(url: String, sessionId: String): Document =
  val browser = JsoupBrowser.typed() // TODO Extract browser and configuration to a class
  browser.setCookie("ignored", "_session_id", sessionId)
  browser.get(url)
