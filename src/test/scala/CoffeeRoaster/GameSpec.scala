package CoffeeRoaster

import org.scalatest.{FunSpec, Matchers}

class GameSpec extends FunSpec with Matchers {
  describe("Roast action") {
    it("roast beans from the hand and put them in the bag") {
      val initialState = Game(bag=List(), hand=List(Bean(0)))

      val afterRoasting = initialState.execute(Roast())

      afterRoasting.hand shouldBe empty
      afterRoasting.bag should contain only Bean(1)
    }

    it("keeps the bag unchanged") {
      val initialState = Game(bag=List(Bean(2)), hand=List())

      val afterRoasting = initialState.execute(Roast())

      afterRoasting.bag should contain only Bean(2)
    }

    it("hard beans are roasted to level zero") {
      val initialState = Game(bag=List(), hand=List(HardBean()))

      val afterRoasting = initialState.execute(Roast())

      afterRoasting.bag should contain only Bean(0)
    }

    it("level four beans become burnt when roasted") {
      val initialState = Game(bag=List(), hand=List(Bean(4)))

      val afterRoasting = initialState.execute(Roast())

      afterRoasting.bag should contain only BurntBean()
    }
  }
}
