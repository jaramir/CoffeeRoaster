package CoffeeRoaster

import org.scalatest.{FunSpec, Matchers}

class GameSpec extends FunSpec with Matchers {
  describe("Pull action") {
    it("pulls tokens from the bag into the hand") {
      val initialState = Game(bag = twentyFiveBeans)

      val afterPulling = initialState(Pull)

      afterPulling.bag should have length 20
      afterPulling.hand should have length 5
      afterPulling.cup shouldBe empty

      // TODO: assert the selection was actually randomised
    }

    it("discards moisture tokens") {
      val initialState = Game(bag = List(Moisture(), Moisture(), Bean(0), Bean(0), Bean(0)))

      val afterPulling = initialState(Pull)

      afterPulling.hand should contain theSameElementsAs Seq(Bean(0), Bean(0), Bean(0))
      afterPulling.hand should not contain Moisture()
      afterPulling.discarded should contain theSameElementsAs Seq(Moisture(), Moisture())
    }
  }

  describe("Roast action") {
    it("roast beans from the hand and put them in the bag") {
      val initialState = Game(hand = List(Bean(0)))

      val afterRoasting = initialState(Roast)
      afterRoasting.hand shouldBe empty
      afterRoasting.bag should contain only Bean(1)
    }

    it("keeps the bag unchanged") {
      val initialState = Game(bag = List(Bean(2)))

      val afterRoasting = initialState(Roast)
      afterRoasting.bag should contain only Bean(2)
    }

    it("hard beans are roasted to level zero") {
      val initialState = Game(hand = List(HardBean()))

      val afterRoasting = initialState(Roast)
      afterRoasting.bag should contain only Bean(0)
    }

    it("level four beans become burnt when roasted") {
      val initialState = Game(hand = List(Bean(4)))

      val afterRoasting = initialState(Roast)
      afterRoasting.bag should contain only BurntBean()
    }
  }

  describe("Stop action") {
    it("moves ten random beans from the bag to the cup") {
      val initialState = Game(bag = twentyFiveBeans)

      val afterStopping = initialState(Stop)

      afterStopping.bag should have length 15
      afterStopping.cup should have length 10

      // TODO: assert the selection was actually randomised
    }

    it("finishes the game") {
      Game()(Stop).isFinished shouldBe true
    }
  }

  describe("Roast level of the cup") {
    it("sum the value of roasted beans") {
      Game(cup = List(Bean(0), Bean(1), Bean(2), Bean(2), Bean(2), Bean(3), Bean(4))).cupRoastLevel should be (14)
    }

    it("ignore moisture, hard beans, and burnt beans") {
      Game(cup = List(Bean(2), HardBean(), BurntBean(), BurntBean(), Moisture())).cupRoastLevel should be (2)
    }
  }

  describe("Scoring") {
    it("gains point for the cup roast level") {
      val targetRoastLevel = Map(
        (2, 5),
        (3, 10)
      )
      Game(cup = List(Bean(1)), targetRoastLevel = targetRoastLevel).score should be (0)
      Game(cup = List(Bean(2)), targetRoastLevel = targetRoastLevel).score should be (5)
      Game(cup = List(Bean(3)), targetRoastLevel = targetRoastLevel).score should be (10)
      Game(cup = List(Bean(4)), targetRoastLevel = targetRoastLevel).score should be (0)
    }

    it("lose one for each hard or burnt bean") {
      Game(cup = List(HardBean())).score should be (-1)
      Game(cup = List(BurntBean())).score should be (-1)
    }

    it("ignore moisture") {
      Game(cup = List(Moisture())).score should be (0)
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
