package CoffeeRoaster

sealed trait Action

object Pull extends Action

object Roast extends Action

object Stop extends Action

case class Concentration(b1: Token, b2: Token) extends Action

case class Preservation(b1: Token, b2: Token) extends Action
