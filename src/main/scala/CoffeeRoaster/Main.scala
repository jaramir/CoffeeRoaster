package CoffeeRoaster

import scala.io.StdIn

object Main extends App {
  private val jamaicaBlueMountainNo1 = List(
    Bean(0), Bean(0), Bean(0), Bean(0), Bean(0),
    Bean(0), Bean(0), Bean(0), Bean(0), Bean(0),
    Bean(0), Bean(0), Bean(0),
    HardBean(), HardBean(), HardBean(),
    Moisture(), Moisture(), Moisture(),
    // 1x Reject
    // 2x Body
    // 2x Acidity
    // 3x Aroma
  )

  private var game = Game(bag = jamaicaBlueMountainNo1)

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
        println(s"your score is: ${game.score}")
    }
  }

}
