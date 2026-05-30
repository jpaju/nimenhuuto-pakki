case class PlayerShare(name: ShortName, attendances: Int, share: Money)

case class BillDistribution(
    bill: Money,
    unitCost: Money,
    collected: Money,
    eventRange: EventRange,
    playerShares: List[PlayerShare]
)

object BillDistribution:
  def from(totalBill: Money, attendance: AttendanceStats, distribution: Distribution[ShortName]): BillDistribution =
    val playerShares = attendance.playerStats.map: playerStats =>
      PlayerShare(playerStats.name, playerStats.timesAttended, distribution.shares(playerStats.name))

    val collected = distribution.shares.values.reduce(_ + _)

    BillDistribution(
      bill = totalBill,
      eventRange = attendance.eventRange,
      unitCost = distribution.unitCost,
      collected = collected,
      playerShares = playerShares
    )
