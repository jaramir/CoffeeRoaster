package CoffeeRoaster

import org.scalatest.{FunSpec, Matchers}

class GameSpec extends FunSpec with Matchers {
  describe("Pull action") {
    it("pulls tokens from the bag into the hand") {
      val initialState = Game(bag = twentyFiveBeans)

      val afterPulling = initialState.execute(Pull())

      afterPulling.bag should have length 20
      afterPulling.hand should have length 5
      afterPulling.cup shouldBe empty

      // TODO: assert the selection was actually randomised
    }
  }

  describe("Roast action") {
    it("roast beans from the hand and put them in the bag") {
      val initialState = Game(hand = List(Bean(0)))

      val afterRoasting = initialState.execute(Roast())
      afterRoasting.hand shouldBe empty
      afterRoasting.bag should contain only Bean(1)
    }

    it("keeps the bag unchanged") {
      val initialState = Game(bag = List(Bean(2)))

      val afterRoasting = initialState.execute(Roast())
      afterRoasting.bag should contain only Bean(2)
    }

    it("hard beans are roasted to level zero") {
      val initialState = Game(hand = List(HardBean()))

      val afterRoasting = initialState.execute(Roast())
      afterRoasting.bag should contain only Bean(0)
    }

    it("level four beans become burnt when roasted") {
      val initialState = Game(hand = List(Bean(4)))

      val afterRoasting = initialState.execute(Roast())
      afterRoasting.bag should contain only BurntBean()
    }
  }

  describe("Stop action") {
    it("moves ten random beans from the bag to the cup") {
      val initialState = Game(bag = twentyFiveBeans)

      val afterStopping = initialState.execute(Stop())

      afterStopping.bag should have length 15
      afterStopping.cup should have length 10

      // TODO: assert the selection was actually randomised
    }

    it("finishes the game") {
      Game().execute(Stop()).isFinished shouldBe true
    }
  }

  describe("Scoring") {
    it("gain points equal to the value of roasted beans in the cup") {
      val game = Game(cup = List(Bean(0), Bean(1), Bean(2), Bean(2), Bean(2), Bean(3), Bean(4)))

      game.score should be(14)
    }

    it("lose a point for each hard or burnt bean in the cup") {
      val game = Game(cup = List(Bean(2), HardBean(), BurntBean(), BurntBean()))

      game.score should be(-1)
    }
  }

  private val twentyFiveBeans: List[Token] = List(
    Bean(1), Bean(1), Bean(1), Bean(1), Bean(1),
    Bean(1), Bean(1), Bean(1), Bean(1), Bean(1),
    Bean(1), Bean(1), Bean(1), Bean(1), Bean(1),
    Bean(1), Bean(1), Bean(1), Bean(1), Bean(1),
    Bean(1), Bean(1), Bean(1), Bean(1), Bean(1)
  )
}
