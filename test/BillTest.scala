class BillTest extends munit.FunSuite:
  test("distribute - Total bill assembles the full distribution"):
    val eventRange = EventRange(event("1"), event("2"), totalEvents = 3)
    val attendance = attendanceStats(
      playerStats = List(playerStats("Alice", 3), playerStats("Bob", 2), playerStats("Jane", 1)),
      eventRange = eventRange
    )
    val totalBill = euros("30.00")

    val expectedShares = List(
      PlayerShare(ShortName("Alice"), 3, euros("15.00")),
      PlayerShare(ShortName("Bob"), 2, euros("10.00")),
      PlayerShare(ShortName("Jane"), 1, euros("5.00"))
    )
    val expected = BillDistribution(
      bill = totalBill,
      perEventBill = euros("10.00"),
      unitCost = euros("5.00"),
      collected = euros("30.00"),
      eventRange = eventRange,
      playerShares = expectedShares
    )

    val result = Bill.distribute(Bill.Total(totalBill), attendance)

    assertEquals(result, Some(expected))

  test("distribute - Per-event bill derives the total from the per-event amount"):
    val attendance = attendanceStats(
      playerStats = List(playerStats("Alice", 1)),
      eventRange = EventRange(event("1"), event("2"), totalEvents = 4)
    )

    val result = Bill.distribute(Bill.PerEvent(euros("18.75")), attendance).get

    assertEquals(result.perEventBill, euros("18.75"))
    assertEquals(result.bill, euros("75.00"))

  test("distribute - returns None when there are no attendances"):
    val result = Bill.distribute(Bill.Total(euros("30.00")), attendanceStats(playerStats = Nil))

    assertEquals(result, None)
