package CoffeeRoaster

import scala.util.Random.shuffle

case class Game(bag: List[Token] = List(),
                hand: List[Token] = List(),
                cup: List[Token] = List(),
                discarded: List[Token] = List(),
                isFinished: Boolean = false,
                targetRoastLevel: Map[Int, Int] = Map()) {
  private val tokensToPull = 5
  private val tokensToScore = 10
  private val empty: List[Token] = List()

  def apply(action: Action): Game = action match {
    case Pull =>
      val (pulled, rest) = shuffle(bag).splitAt(tokensToPull)
      val (moisture, beans) = pulled.partition(_ == Moisture)
      this.copy(bag = rest, hand = beans, discarded = this.discarded ++ moisture)

    case Roast =>
      this.copy(hand.map(_.roast) ++ bag, empty)

    case Stop =>
      val (picked, rest) = shuffle(bag).splitAt(tokensToScore)
      this.copy(rest, empty, picked, isFinished = true)
  }

  def score: Int =
    targetRoastLevel.getOrElse(cupRoastLevel, 0) +
      cup.map(_.scoreModifier).sum

  def cupRoastLevel: Int = cup.map(_.roastValue).sum
}
