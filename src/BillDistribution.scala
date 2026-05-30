case class PlayerShare(name: ShortName, attendances: Int, share: Money)

case class BillDistribution(
    bill: Money,
    perEventBill: Money,
    unitCost: Money,
    collected: Money,
    eventRange: EventRange,
    playerShares: List[PlayerShare]
)

object BillDistribution:
  def from(bill: Bill, attendance: AttendanceStats, distribution: Distribution[ShortName]): BillDistribution =
    val totalEvents  = attendance.eventRange.totalEvents
    val playerShares = attendance.playerStats.map: playerStats =>
      PlayerShare(playerStats.name, playerStats.timesAttended, distribution.shares(playerStats.name))

    val collected = distribution.shares.values.reduce(_ + _)

    BillDistribution(
      bill = bill.total(totalEvents),
      perEventBill = bill.perEvent(totalEvents),
      eventRange = attendance.eventRange,
      unitCost = distribution.unitCost,
      collected = collected,
      playerShares = playerShares
    )
