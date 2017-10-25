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
    Body, Body
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
    StdIn.readLine("[Look|Pull|Roast|Stop|Concentration x y]> ") match {
      case "l" =>
        look("hand", game.hand)
        look("discarded", game.discarded)

      case "lb" =>
        look("bag (cheat!)", game.bag)

      case "p" =>
        val next = game(Pull)
        if (next.discarded != game.discarded) {
          println(s"discarded: ${next.discarded.diff(game.discarded)}")
        }
        game = next
        game.hand.foreach(println)

      case "r" =>
        game = game(Roast)
        println(s"roast tracker is now: ${game.roastTracker}")

      case "s" =>
        game = game(Stop)
        game.cup.foreach(println)
        println(s"cup roast level is: ${game.cupRoastLevel}")
        println(s"your score is: ${game.score}")

      case cmd if cmd.matches("c [123] [123]") =>
        val parts = cmd.split(' ')
        val b1 = toBean(parts(1))
        val b2 = toBean(parts(2))
        game = game(Concentration(b1, b2))

      case cmd =>
        println(s"I don't know how to $cmd")
    }
  }

  private def toBean(str: String) = str match {
    case "1" => BeanOne
    case "2" => BeanTwo
    case "3" => BeanThree
  }

  private def look(name: String, tokens: List[Token]) {
    print(name)
    print(":")
    tokens.foreach(t => {
      print(" ")
      print(t)
    })
    println()
  }
}
