enum Bill:
  case Total(amount: Money)
  case PerEvent(amount: Money)

  def total(totalEvents: Int): Money = this match
    case Total(amount)    => amount
    case PerEvent(amount) => amount * totalEvents

  def perEvent(totalEvents: Int): Money = this match
    case Total(amount)    => amount / totalEvents
    case PerEvent(amount) => amount
