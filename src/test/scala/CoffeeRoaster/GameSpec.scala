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
      val initialState = Game(bag = List(Moisture, Moisture, BeanZero, BeanZero, BeanZero))

      val afterPulling = initialState(Pull)

      afterPulling.hand should contain theSameElementsAs Seq(BeanZero, BeanZero, BeanZero)
      afterPulling.hand should not contain Moisture
      afterPulling.discarded should contain theSameElementsAs Seq(Moisture, Moisture)
    }
  }

  describe("Roast action") {
    it("beans from the hand are roasted to the next level and put in the bag") {
      val initialState = Game(hand = List(BeanZero, BeanOne, BeanTwo, BeanThree))

      val afterRoasting = initialState(Roast)
      afterRoasting.hand shouldBe empty
      afterRoasting.bag should contain allOf(BeanOne, BeanTwo, BeanThree, BeanFour)
    }

    it("keeps the bag unchanged") {
      val initialState = Game(bag = List(BeanTwo))

      val afterRoasting = initialState(Roast)
      afterRoasting.bag should contain only BeanTwo
    }

    it("hard beans are roasted to level zero") {
      val initialState = Game(hand = List(HardBean))

      val afterRoasting = initialState(Roast)
      afterRoasting.bag should contain only BeanZero
    }

    it("level four beans become burnt when roasted") {
      val initialState = Game(hand = List(BeanFour))

      val afterRoasting = initialState(Roast)
      afterRoasting.bag should contain only BurntBean
    }

    it("other tokens are left unchanged") {
      val initialState = Game(hand = List(BurntBean, Moisture))

      val afterRoasting = initialState(Roast)
      afterRoasting.bag should contain allOf(BurntBean, Moisture)
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
      Game(cup = List(BeanZero, BeanOne, BeanTwo, BeanTwo, BeanTwo, BeanThree, BeanFour)).cupRoastLevel should be(14)
    }

    it("ignore moisture, hard beans, and burnt beans") {
      Game(cup = List(BeanTwo, HardBean, BurntBean, BurntBean, Moisture)).cupRoastLevel should be(2)
    }
  }

  describe("Roast tracker") {
    it("sum the rost value of every token in the bag") {
      Game(bag = List(BeanZero, BeanOne, BeanTwo, BeanThree, BeanFour, BurntBean, Moisture, HardBean)).roastTracker should be (10)
    }
  }

  describe("Scoring") {
    it("gains point for the cup roast level") {
      val targetRoastLevel = Map(
        (2, 5),
        (3, 10)
      )
      Game(cup = List(BeanOne), targetRoastLevel = targetRoastLevel).score should be(0)
      Game(cup = List(BeanTwo), targetRoastLevel = targetRoastLevel).score should be(5)
      Game(cup = List(BeanThree), targetRoastLevel = targetRoastLevel).score should be(10)
      Game(cup = List(BeanFour), targetRoastLevel = targetRoastLevel).score should be(0)
    }

    it("lose one for each hard or burnt bean") {
      Game(cup = List(HardBean)).score should be(-1)
      Game(cup = List(BurntBean)).score should be(-1)
    }

    it("ignore moisture") {
      Game(cup = List(Moisture)).score should be(0)
    }
  }

  private val twentyFiveBeans: List[Token] = List(
    BeanOne, BeanOne, BeanOne, BeanOne, BeanOne,
    BeanOne, BeanOne, BeanOne, BeanOne, BeanOne,
    BeanOne, BeanOne, BeanOne, BeanOne, BeanOne,
    BeanOne, BeanOne, BeanOne, BeanOne, BeanOne,
    BeanOne, BeanOne, BeanOne, BeanOne, BeanOne
  )
}
