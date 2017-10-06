package CoffeeRoaster

case class Game(bag: List[Bean], hand: List[Bean]) {
  def execute(action: Action): Game = this
}
