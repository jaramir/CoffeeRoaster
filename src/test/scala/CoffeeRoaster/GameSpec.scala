package CoffeeRoaster

import org.scalatest.{FlatSpec, Matchers}

class GameSpec extends FlatSpec with Matchers {
  behavior of "a game of Coffee Roaster"

  it should "roast beans from the hand and put them in the bag" in {
    val t0 = Game(bag=List(), hand=List(Bean(0)))

    val t1 = t0.execute(Roast())

    t1.hand shouldBe empty
    t1.bag should contain only Bean(1)
  }
}
