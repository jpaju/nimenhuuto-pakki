import org.scalacheck.Gen
import org.scalacheck.Prop.forAll

class CostDistributionTest extends munit.ScalaCheckSuite:
  test("perAttendance - returns None when there are no attendances"):
    val total         = euros("100.00")
    val noAttendances = Map.empty[String, Int]

    assertEquals(CostDistribution.perAttendance(total, noAttendances), None)

  test("perAttendance - splits evenly when the total divides exactly"):
    val total       = euros("30.00")
    val attendances = Map(
      "Alice" -> 1,
      "Bob"   -> 1,
      "Jane"  -> 1
    )

    val result = CostDistribution.perAttendance(total, attendances).get

    assertEquals(result.unitCost, euros("10.00"))
    assertEquals(
      result.shares,
      Map(
        "Alice" -> euros("10.00"),
        "Bob"   -> euros("10.00"),
        "Jane"  -> euros("10.00")
      )
    )

  test("perAttendance - each player pays the unit cost per attendance"):
    val total       = euros("100.00")
    val attendances = Map(
      "Alice" -> 3,
      "Bob"   -> 2,
      "Jane"  -> 2
    )

    val result = CostDistribution.perAttendance(total, attendances).get

    assertEquals(result.unitCost, euros("14.29"))
    assertEquals(
      result.shares,
      Map(
        "Alice" -> euros("42.87"),
        "Bob"   -> euros("28.58"),
        "Jane"  -> euros("28.58")
      )
    )

  property("perAttendance - collected total never under-collects"):
    forAll(moneyGen, attendancesGen): (total, attendances) =>
      val result    = CostDistribution.perAttendance(total, attendances).get
      val collected = result.shares.values.reduce(_ + _)
      collected >= total

  property("perAttendance - over-collection stays under one cent per attendance"):
    forAll(moneyGen, attendancesGen): (total, attendances) =>
      val result            = CostDistribution.perAttendance(total, attendances).get
      val collected         = result.shares.values.reduce(_ + _)
      val maxOvercollection = euros("0.01") * attendances.values.sum
      collected < total + maxOvercollection

  property("perAttendance - every attendee gets a share"):
    forAll(moneyGen, attendancesGen): (total, attendances) =>
      val result = CostDistribution.perAttendance(total, attendances).get
      result.shares.keySet == attendances.keySet

  property("perAttendance - each share is the unit cost times attendances"):
    forAll(moneyGen, attendancesGen): (total, attendances) =>
      val result = CostDistribution.perAttendance(total, attendances).get
      attendances.forall: (player, count) =>
        result.shares(player) == result.unitCost * count

  private val attendancesGen: Gen[Map[String, Int]] =
    Gen.nonEmptyMap(
      for
        name  <- Gen.identifier
        count <- Gen.chooseNum(1, 50)
      yield name -> count
    )
