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
        game = game.execute(Pull())
        game.hand.foreach(println)

      case "r" =>
        game = game.execute(Roast())

      case "s" =>
        game = game.execute(Stop())
        game.cup.foreach(println)
        println(s"your score is: ${game.score}")
    }
  }

}
