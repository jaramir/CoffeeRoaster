package CoffeeRoaster

import scala.util.Random.shuffle

case class Game(bag: List[Token] = List(), hand: List[Token] = List(), cup: List[Token] = List()) {
  def score: Int = cup.flatMap(token => token match {
    case bean: Bean => Some(bean.value)
    case _: HardBean => Some(-1)
    case _: BurntBean => Some(-1)
  }).sum

  def execute(action: Action): Game = action match {
    case _: Pull =>
      val (pulled, rest) = shuffle(bag).splitAt(tokensToPull)
      this.copy(rest, pulled, empty)

    case _: Roast =>
      this.copy(hand.map(roast) ++ bag, empty)

    case _: Stop =>
      val (picked, rest) = shuffle(bag).splitAt(tokensToScore)
      this.copy(rest, empty, picked)
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

  private val tokensToPull = 5
  private val tokensToScore = 10

  private val empty: List[Token] = List()
}
