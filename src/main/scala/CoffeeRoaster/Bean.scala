package CoffeeRoaster

sealed trait Token

case class Bean(value: Int) extends Token

case class HardBean() extends Token
