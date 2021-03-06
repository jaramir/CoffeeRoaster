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

    case Concentration(b1, b2) =>
      def isAcceptable(token: Token) = List(BeanOne, BeanTwo, BeanThree).contains(token)

      if (!isAcceptable(b1) || !isAcceptable(b2)) return this
      if (!hand.contains(b1)) return this
      if (!hand.contains(b2)) return this
      if (b1 == b2 && hand.count(_ == b1) < 2) return this
      if (!hand.contains(Body)) return this

      val roastValue = b1.roastValue + b2.roastValue
      List(BeanTwo, BeanThree, BeanFour)
        .find(_.roastValue == roastValue)
        .map(newToken => {
          val newHand = (hand.toBuffer - Body - b1 - b2).toList
          this.copy(hand = newHand, bag = bag :+ newToken)
        })
        .getOrElse(this)

    case Preservation(b1, b2) =>
      def isAcceptable(token: Token) = List(BeanZero, BeanOne, BeanTwo, BeanThree, BeanFour).contains(token)

      if (!isAcceptable(b1) || !isAcceptable(b2)) return this
      if (!hand.contains(b1)) return this
      if (!hand.contains(b2)) return this
      if (b1 == b2 && hand.count(_ == b1) < 2) return this
      if (!hand.contains(Acidity)) return this

      val newHand = (hand.toBuffer - Acidity - b1 - b2).toList
      this.copy(hand = newHand, bag = bag :+ b1 :+ b2)

    case Dispersion(b, b1, b2) =>
      val newHand = (hand.toBuffer - Aroma - b).toList
      this.copy(hand = newHand, bag = bag :+ b1 :+ b2)
  }

  def score: Int = roastPoints + negativePoints + skillPoints

  private def roastPoints = {
    targetRoastLevel.getOrElse(cupRoastLevel, 0)
  }

  def cupRoastLevel: Int = roastValueOf(cup)

  private def roastValueOf(tokens: List[Token]): Int = tokens.map(_.roastValue).sum

  private def negativePoints = {
    cup.map(_.scoreModifier).sum
  }

  private def skillPoints: Int = {
    val setSizes = cup.filter(_.roastValue > 0)
      .groupBy(_.roastValue).values
      .map(_.length)

    if (setSizes.isEmpty) return 0

    Seq(0, 0, 0, 1, 2, 3, 4, 5)(math.min(setSizes.max, 7))
  }

  def roastTracker: Int = roastValueOf(bag)
}
