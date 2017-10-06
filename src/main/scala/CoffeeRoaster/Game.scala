package CoffeeRoaster

import scala.util.Random

case class Game(bag: List[Token], hand: List[Token], cup: List[Token]) {

  def score: Int = cup.flatMap(token => token match {
    case bean: Bean => Some(bean.value)
    case _: HardBean => Some(-1)
    case _: BurntBean => Some(-1)
  }).sum


  def execute(action: Action): Game = action match {
    case _: Roast =>
      this.copy(hand.map(roast) ++ bag, List())

    case _: Stop =>
      val (pickedForCup, leftInBag) = Random.shuffle(bag).splitAt(10)
      this.copy(leftInBag, List(), pickedForCup)
  }

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
