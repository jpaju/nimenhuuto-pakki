case class Players(
    in: List[String],
    out: List[String],
    unknown: List[String]
)

case class Event(
    id: String,
    url: String,
    title: String,
    date: String
)

case class EventHistory(events: Map[Event, Players])
