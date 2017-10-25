package CoffeeRoaster

sealed trait Token {
  def roastValue: Int

  def scoreModifier: Int

  def roast: Token
}

case object BeanZero extends Token {
  override def roastValue: Int = 0

  override def scoreModifier: Int = 0

  override def roast: Token = BeanOne

  override def toString = "(0)"
}

case object BeanOne extends Token {
  override def roastValue: Int = 1

  override def scoreModifier: Int = 0

  override def roast: Token = BeanTwo

  override def toString = "(1)"
}

case object BeanTwo extends Token {
  override def roastValue: Int = 2

  override def scoreModifier: Int = 0

  override def roast: Token = BeanThree

  override def toString = "(2)"
}

case object BeanThree extends Token {
  override def roastValue: Int = 3

  override def scoreModifier: Int = 0

  override def roast: Token = BeanFour

  override def toString = "(3)"
}

case object BeanFour extends Token {
  override def roastValue: Int = 4

  override def scoreModifier: Int = 0

  override def roast: Token = BurntBean

  override def toString = "(4)"
}

case object HardBean extends Token {
  override def roastValue: Int = 0

  override def scoreModifier: Int = -1

  override def roast: Token = BeanZero

  override def toString = "(H)"
}

case object BurntBean extends Token {
  override def roastValue: Int = 0

  override def scoreModifier: Int = -1

  override def roast: Token = BurntBean

  override def toString = "(B)"
}

case object Moisture extends Token {
  override def roastValue: Int = 0

  override def scoreModifier: Int = 0

  override def roast: Token = Moisture

  override def toString = "(M)"
}

case object Body extends Token {
  override def roastValue: Int = 0

  override def scoreModifier: Int = 0

  override def roast: Token = Body

  override def toString = "(Body)"
}

case object Acidity extends Token {
  override def roastValue: Int = 0

  override def scoreModifier: Int = 0

  override def roast: Token = Acidity

  override def toString = "(Acidity)"
}

case object Aroma extends Token {
  override def roastValue: Int = 0

  override def scoreModifier: Int = 0

  override def roast: Token = Aroma

  override def toString = "(Aroma)"
}