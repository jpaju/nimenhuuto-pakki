enum Bill:
  case Total(amount: Money)
  case PerEvent(amount: Money)

  def total(totalEvents: Int): Money = this match
    case Total(amount)    => amount
    case PerEvent(amount) => amount * totalEvents

  def perEvent(totalEvents: Int): Money = this match
    case Total(amount)    => amount / totalEvents
    case PerEvent(amount) => amount

case class PlayerShare(name: ShortName, attendances: Int, share: Money)
case class BillDistribution(
    bill: Money,
    perEventBill: Money,
    unitCost: Money,
    collected: Money,
    eventRange: EventRange,
    playerShares: List[PlayerShare]
)

object Bill:
  def distribute(bill: Bill, attendance: AttendanceStats): Option[BillDistribution] =
    val totalEvents          = attendance.eventRange.totalEvents
    val totalBill            = bill.total(totalEvents)
    val attendancesPerPlayer = attendanceCounts(attendance)

    for distribution <- CostDistribution.perAttendance(totalBill, attendancesPerPlayer)
    yield BillDistribution(
      bill = totalBill,
      perEventBill = bill.perEvent(totalEvents),
      unitCost = distribution.unitCost,
      collected = collectedTotal(distribution),
      eventRange = attendance.eventRange,
      playerShares = playerShares(attendance, distribution)
    )

  private def attendanceCounts(attendance: AttendanceStats): Map[ShortName, Int] =
    attendance.playerStats.map(ps => ps.name -> ps.timesAttended).toMap

  private def collectedTotal(distribution: CostDistribution[ShortName]): Money =
    distribution.shares.values.reduce(_ + _)

  private def playerShares(
      attendance: AttendanceStats,
      distribution: CostDistribution[ShortName]
  ): List[PlayerShare] =
    attendance.playerStats.map: playerStats =>
      PlayerShare(
        name = playerStats.name,
        attendances = playerStats.timesAttended,
        share = distribution.shares(playerStats.name)
      )
