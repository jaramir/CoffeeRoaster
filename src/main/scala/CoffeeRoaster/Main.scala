package CoffeeRoaster

import scala.io.StdIn

object Main extends App {
  private val jamaicaBlueMountainNo1Bag = List(
    BeanZero, BeanZero, BeanZero, BeanZero, BeanZero,
    BeanZero, BeanZero, BeanZero, BeanZero, BeanZero,
    BeanZero, BeanZero, BeanZero,
    HardBean, HardBean, HardBean,
    Moisture, Moisture, Moisture,
    // 1x Reject
    // 2x Body
    // 2x Acidity
    // 3x Aroma
  )

  private val jamaicaBlueMountainNo1Target = Map(
    (11, 4),
    (12, 4),
    (13, 6),
    (14, 10),
    (15, 7),
    (16, 6),
    (17, 4),
    (18, 4)
  )

  private var game = Game(
    bag = jamaicaBlueMountainNo1Bag,
    targetRoastLevel = jamaicaBlueMountainNo1Target)

  while (!game.isFinished) {
    StdIn.readLine("Pull Roast Stop > ") match {
      case "p" =>
        val next = game(Pull)
        if (next.discarded != game.discarded) {
          println(s"discarded: ${next.discarded.diff(game.discarded)}")
        }
        game = next
        game.hand.foreach(println)

      case "r" =>
        game = game(Roast)

      case "s" =>
        game = game(Stop)
        game.cup.foreach(println)
        println(s"cup roast level is: ${game.cupRoastLevel}")
        println(s"your score is: ${game.score}")
    }
  }
}
