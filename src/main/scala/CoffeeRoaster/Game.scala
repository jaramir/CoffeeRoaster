package CoffeeRoaster

import scala.util.Random.shuffle

case class Game(bag: List[Token] = List(),
                hand: List[Token] = List(),
                cup: List[Token] = List(),
                discarded: List[Token] = List(),
                isFinished: Boolean = false) {
  def score: Int = cup.map {
    case bean: Bean => bean.value
    case _: HardBean => -1
    case _: BurntBean => -1
    case _: Moisture => 0
  }.sum

  def isMoisture(token: Token): Boolean = token.isInstanceOf[Moisture]

  def apply(action: Action): Game = action match {
    case Pull =>
      val (pulled, rest) = shuffle(bag).splitAt(tokensToPull)
      val (moisture, beans) = pulled.partition(token => isMoisture(token))
      this.copy(bag = rest, hand = beans, discarded = this.discarded ++ moisture)

    case Roast =>
      this.copy(hand.map(roast) ++ bag, empty)

    case Stop =>
      val (picked, rest) = shuffle(bag).splitAt(tokensToScore)
      this.copy(rest, empty, picked, isFinished = true)
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
