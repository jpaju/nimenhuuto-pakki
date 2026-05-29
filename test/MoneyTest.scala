class MoneyTest extends munit.FunSuite:
  test("euros - renders normalized to two decimals with currency"):
    assertEquals(Money.euros("1.5").map(_.render), Some("1.50 €"))

  test("euros - rounds half up"):
    assertEquals(Money.euros("1.005").map(_.render), Some("1.01 €"))

  test("euros - accepts comma as decimal separator"):
    assertEquals(Money.euros("1,5").map(_.render), Some("1.50 €"))

  test("euros - returns None for invalid input"):
    assertEquals(Money.euros("4x.5"), None)

  test("plus - adds without floating point drift"):
    val sum = for
      a <- Money.euros("0.10")
      b <- Money.euros("0.20")
    yield a + b
    assertEquals(sum, Money.euros("0.30"))

  test("minus - subtracts and allows negative results"):
    val diff = for
      a <- Money.euros("0.20")
      b <- Money.euros("0.30")
    yield a - b
    assertEquals(diff, Money.euros("-0.10"))

  test("times - multiplies by an integer count"):
    assertEquals(Money.euros("1.50").map(_ * 3), Money.euros("4.50"))

  test("dividedBy - rounds up to whole cents"):
    assertEquals(Money.euros("100.00").map(_ / 7), Money.euros("14.29"))

  test("dividedBy - exact division stays exact"):
    assertEquals(Money.euros("10.00").map(_ / 2), Money.euros("5.00"))

  test("dividedBy - throws on division by zero"):
    val money = Money.euros("10.00").get
    intercept[ArithmeticException](money / 0)
