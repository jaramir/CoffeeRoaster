package CoffeeRoaster

sealed trait Action

case class Pull() extends Action

case class Roast() extends Action

case class Stop() extends Action
