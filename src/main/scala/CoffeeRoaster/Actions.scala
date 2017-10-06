package CoffeeRoaster

sealed trait Action

case class Roast() extends Action

case class Stop() extends Action
