package CoffeeRoaster

import org.scalatest.{FunSpec, Matchers}

class GameSpec extends FunSpec with Matchers {
  describe("Roast action") {
    it("roast beans from the hand and put them in the bag") {
      val initialState = Game(bag = List(), hand = List(Bean(0)), List())

      val afterRoasting = initialState.execute(Roast())

      afterRoasting.hand shouldBe empty
      afterRoasting.bag should contain only Bean(1)
    }

    it("keeps the bag unchanged") {
      val initialState = Game(bag = List(Bean(2)), hand = List(), List())

      val afterRoasting = initialState.execute(Roast())

      afterRoasting.bag should contain only Bean(2)
    }

    it("hard beans are roasted to level zero") {
      val initialState = Game(bag = List(), hand = List(HardBean()), List())

      val afterRoasting = initialState.execute(Roast())

      afterRoasting.bag should contain only Bean(0)
    }

    it("level four beans become burnt when roasted") {
      val initialState = Game(bag = List(), hand = List(Bean(4)), List())

      val afterRoasting = initialState.execute(Roast())

      afterRoasting.bag should contain only BurntBean()
    }
  }

  describe("Stop action") {
    it("takes 10 beans from the bag at random") {
      val initialState = Game(bag = List(
        Bean(1), Bean(1), Bean(1), Bean(1), Bean(1),
        Bean(1), Bean(1), Bean(1), Bean(1), Bean(1),
        Bean(1), Bean(1), Bean(1), Bean(1), Bean(1),
        Bean(1), Bean(1), Bean(1), Bean(1), Bean(1),
        Bean(1), Bean(1), Bean(1), Bean(1), Bean(1)
      ), hand = List(), cup = List())

      val afterStopping = initialState.execute(Stop())

      afterStopping.bag should have length 15
      afterStopping.cup should have length 10

      // TODO: assert the selection was actually randomised
    }
  }

  describe("Scoring") {
    it("gain points equal to the value of roasted beans") {
      val game = Game(List(), List(), List(Bean(0), Bean(1), Bean(2), Bean(2), Bean(2), Bean(3), Bean(4)))

      game.score should be (14)
    }

    it("lose a point for each hard or burnt bean") {
      val game = Game(List(), List(), List(Bean(2), HardBean(), BurntBean(), BurntBean()))

      game.score should be (-1)
    }
  }
}
