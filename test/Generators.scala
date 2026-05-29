import org.scalacheck.Gen

val moneyGen: Gen[Money] =
  Gen
    .chooseNum(1, 1_000_00)
    .map(cents => euros(f"${cents / 100}.${cents % 100}%02d"))
