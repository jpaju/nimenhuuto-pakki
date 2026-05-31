import scala.math.BigDecimal.RoundingMode
import scala.util.Try

/** An exact amount in euros, stored to the cent. */
case class Money private (private val amount: BigDecimal) extends Ordered[Money]:
  def +(other: Money): Money = Money(amount + other.amount)
  def -(other: Money): Money = Money(amount - other.amount)
  def *(count: Int): Money   = Money(amount * count)

  /** Divides into `by` equal parts, rounding up so a split never under-collects. Throws on division by zero. */
  def /(by: Int): Money = Money.roundedUpToCent(amount / by)

  def compare(other: Money): Int = amount.compare(other.amount)

  def render: String = s"${amount.toString.replace('.', ',')} €"

object Money:
  private val scale = 2

  /** Parses a euro amount, accepting `.` or `,` as the decimal separator. Rounds to the nearest cent. */
  def euros(value: String): Option[Money] =
    val withDotSeparator = value.trim.replace(',', '.')
    val parsed           = Try(BigDecimal(withDotSeparator)).toOption
    parsed.map(toNearestCent)

  private def toNearestCent(value: BigDecimal): Money =
    Money(value.setScale(scale, RoundingMode.HALF_UP))

  private def roundedUpToCent(value: BigDecimal): Money =
    Money(value.setScale(scale, RoundingMode.CEILING))
