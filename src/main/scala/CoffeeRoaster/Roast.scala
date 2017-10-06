package CoffeeRoaster

sealed trait Action

case class Roast() extends Action
