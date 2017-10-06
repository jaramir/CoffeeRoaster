package CoffeeRoaster

case class Game(bag: List[Token], hand: List[Token]) {
  def execute(action: Action): Game =
    Game(hand.map(roast) ++ bag, List())

  private def roast(token: Token) = {
     token match {
      case bean: Bean =>
        if (bean.value == 4)
          BurntBean()
        else
          Bean(bean.value + 1)
      case _: HardBean => Bean(0)
      case _ => token
    }
  }
}
