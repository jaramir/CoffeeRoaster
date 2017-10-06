package CoffeeRoaster

case class Game(bag: List[Bean], hand: List[Bean]) {
  def execute(action: Action): Game =
    Game(hand.map(roast) ++ bag, List())

  private def roast(bean: Bean) = Bean(bean.value + 1)
}
