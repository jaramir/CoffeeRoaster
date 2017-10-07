package CoffeeRoaster

import scala.io.StdIn

object Main extends App {
  private val initialBag = List(
    HardBean(), HardBean(),
    Bean(0), Bean(0), Bean(0), Bean(0), Bean(0),
    Bean(0), Bean(0), Bean(0), Bean(0), Bean(0),
    Bean(0), Bean(0), Bean(0), Bean(0), Bean(0),
    Bean(0), Bean(0), Bean(0), Bean(0), Bean(0),
    Bean(0), Bean(0), Bean(0), Bean(0), Bean(0)
  )

  private var game = Game(bag = initialBag)

  while (!game.isFinished) {
    StdIn.readLine("Pull Roast Stop > ") match {
      case "p" =>
        game = game(Pull)
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
