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
      Game(bag = List(BeanZero, BeanOne, BeanTwo, BeanThree, BeanFour, BurntBean, Moisture, HardBean)).roastTracker should be(10)
    }
  }

  describe("Scoring") {
    it("gain points for the cup roast level") {
      val targetRoastLevel = Map(
        (2, 5),
        (3, 10)
      )
      Game(cup = List(BeanOne), targetRoastLevel = targetRoastLevel).score should be(0)
      Game(cup = List(BeanTwo), targetRoastLevel = targetRoastLevel).score should be(5)
      Game(cup = List(BeanThree), targetRoastLevel = targetRoastLevel).score should be(10)
      Game(cup = List(BeanFour), targetRoastLevel = targetRoastLevel).score should be(0)
    }

    describe("Skill Points") {
      it("gain points for the largest set of equal value beans") {
        Game(cup = List(BeanOne, BeanOne, BeanOne)).score should be(1)
        Game(cup = List(BeanOne, BeanOne, BeanOne, BeanOne)).score should be(2)
        Game(cup = List(BeanOne, BeanOne, BeanOne, BeanOne, BeanOne)).score should be(3)
        Game(cup = List(BeanOne, BeanOne, BeanOne, BeanOne, BeanOne, BeanOne)).score should be(4)
        Game(cup = List(BeanOne, BeanOne, BeanOne, BeanOne, BeanOne, BeanOne, BeanOne)).score should be(5)
        Game(cup = List(BeanOne, BeanOne, BeanOne, BeanOne, BeanOne, BeanOne, BeanOne, BeanOne)).score should be(5)
      }

      it("only one largest set of equal value beans is scored") {
        Game(cup = List(
          BeanOne, BeanOne, BeanOne, BeanOne, BeanOne,
          BeanTwo, BeanTwo, BeanTwo, BeanTwo, BeanTwo,
          BeanThree, BeanThree, BeanThree, BeanThree,
        )).score should be(3)
      }
    }

    it("lose one for each hard or burnt bean") {
      Game(cup = List(HardBean)).score should be(-1)
      Game(cup = List(BurntBean)).score should be(-1)
    }

    it("ignore moisture") {
      Game(cup = List(Moisture)).score should be(0)
    }
  }

  describe("Concentration") {
    it("use a body to turn two beans into one") {
      val initialState = Game(hand = List(BeanOne, BeanTwo, Body))

      val afterConcentrtion = initialState(Concentration(BeanOne, BeanTwo))

      afterConcentrtion.hand shouldBe empty
      afterConcentrtion.bag should contain only BeanThree
    }

    it("has to have the beans and the body in hand") {
      Game(hand = List(Body, BeanOne))(Concentration(BeanOne, BeanTwo)).bag shouldBe empty
      Game(hand = List(Body, BeanTwo))(Concentration(BeanOne, BeanTwo)).bag shouldBe empty
      Game(hand = List(BeanOne, BeanTwo))(Concentration(BeanOne, BeanTwo)).bag shouldBe empty
    }

    // selects the beans to consume
    // 1, 2 or 3
    // total must me 4 or less
    // goes to the bag
    // consumes the body
    // has to have a body token
  }

  private val twentyFiveBeans: List[Token] = List(
    BeanOne, BeanOne, BeanOne, BeanOne, BeanOne,
    BeanOne, BeanOne, BeanOne, BeanOne, BeanOne,
    BeanOne, BeanOne, BeanOne, BeanOne, BeanOne,
    BeanOne, BeanOne, BeanOne, BeanOne, BeanOne,
    BeanOne, BeanOne, BeanOne, BeanOne, BeanOne
  )
}
