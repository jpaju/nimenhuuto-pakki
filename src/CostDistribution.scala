/** The outcome of distributing a total cost across participants. */
case class Distribution[A](unitCost: Money, shares: Map[A, Money])

object CostDistribution:
  /** Distributes `total` across participants by attendance count: each pays for as many attendances as they have.
    * Returns `None` when there are no attendances.
    */
  def perAttendance[A](total: Money, attendances: Map[A, Int]): Option[Distribution[A]] =
    val totalAttendances = attendances.values.sum
    Option.when(0 < totalAttendances):
      val unitCost = total / totalAttendances
      val shares   = attendances.view
        .mapValues(attendances => unitCost * attendances)
        .toMap

      Distribution(unitCost, shares)
