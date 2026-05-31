class MoneyTest extends munit.FunSuite:
  test("euros - renders normalized to two decimals with currency"):
    assertEquals(Money.euros("1.5").map(_.render), Some("1,50 €"))

  test("euros - rounds half up"):
    assertEquals(Money.euros("1.005").map(_.render), Some("1,01 €"))

  test("euros - accepts comma as decimal separator"):
    assertEquals(Money.euros("1,5").map(_.render), Some("1,50 €"))

  test("euros - returns None for invalid input"):
    assertEquals(Money.euros("4x.5"), None)

  test("plus - adds without floating point drift"):
    assertEquals(euros("0.10") + euros("0.20"), euros("0.30"))

  test("minus - subtracts and allows negative results"):
    assertEquals(euros("0.20") - euros("0.30"), euros("-0.10"))

  test("times - multiplies by an integer count"):
    assertEquals(euros("1.50") * 3, euros("4.50"))

  test("dividedBy - rounds up to whole cents"):
    assertEquals(euros("100.00") / 7, euros("14.29"))

  test("dividedBy - exact division stays exact"):
    assertEquals(euros("10.00") / 2, euros("5.00"))

  test("dividedBy - throws on division by zero"):
    intercept[ArithmeticException](euros("10.00") / 0)

  test("comparison - orders amounts"):
    assertEquals(euros("10.01") >= euros("10.00"), true)
    assertEquals(euros("10.00") >= euros("10.00"), true)
    assertEquals(euros("9.99") >= euros("10.00"), false)
    assertEquals(euros("9.99") < euros("10.00"), true)
