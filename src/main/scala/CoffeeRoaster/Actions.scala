package CoffeeRoaster

sealed trait Action

object Pull extends Action

object Roast extends Action

object Stop extends Action
