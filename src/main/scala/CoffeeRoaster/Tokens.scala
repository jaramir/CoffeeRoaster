package CoffeeRoaster

sealed trait Token {
  def scoreModifier: Int
  def roastValue: Int
}

case class Bean(value: Int) extends Token {
  override val scoreModifier: Int = 0
  override def roastValue: Int = value
}

case class HardBean() extends Token {
  override val scoreModifier: Int = -1
  override val roastValue: Int = 0
}

case class BurntBean() extends Token {
  override val scoreModifier: Int = -1
  override val roastValue: Int = 0
}

case class Moisture() extends Token {
  override val scoreModifier: Int = 0
  override val roastValue: Int = 0
}