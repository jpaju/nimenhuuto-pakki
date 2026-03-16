import java.time.LocalDateTime

object DateParser:
  def parseDate(dateStr: String, defaultYear: Int): Option[LocalDateTime] =
    dateStr match
      case s"$_ $day.$month. klo $hour:$minute" =>
        toLocalDateTime(defaultYear.toString, month, day, hour, minute)

      case s"$_ $day.$month.$year klo $hour:$minute" =>
        toLocalDateTime(year, month, day, hour, minute)

      case _ =>
        None

  private def toLocalDateTime(yearStr: String, monthStr: String, dayStr: String, hourStr: String, minuteStr: String) =
    for
      year   <- yearStr.toIntOption
      month  <- monthStr.toIntOption
      day    <- dayStr.toIntOption
      hour   <- hourStr.toIntOption
      minute <- minuteStr.toIntOption
    yield LocalDateTime.of(year, month, day, hour, minute)
