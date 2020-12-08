package com.bb;

import javafx.scene.control.Button;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class Tester {
    private final DeskController executor = new DeskController();
    private final ThingsToWorkWith testing = new ThingsToWorkWith();
    char[] mustBe = new char[32];
    char[] giver = new char[32];

    @Test
    void movementTest() {
        App.main(new String[]{});
        DeskController.buttons = new Button[]{executor.button1, executor.button2, executor.button3, executor.button4,
                executor.button5, executor.button6, executor.button7, executor.button8, executor.button9,
                executor.button10, executor.button11, executor.button12, executor.button13, executor.button14,
                executor.button15, executor.button16, executor.button17, executor.button18, executor.button19,
                executor.button20, executor.button21, executor.button22, executor.button23, executor.button24,
                executor.button25, executor.button26, executor.button27, executor.button28, executor.button29,
                executor.button30, executor.button31, executor.button32};
        clearList(mustBe);
        clearList(giver);

        //Ход белой шашки
        giver[8] = 'w';
        ThingsToWorkWith.myWorld(giver);
        executor.move(5, 8);
        mustBe[5] = 'w';
        assertArrayEquals(mustBe, testing.getCheckers());
        clearList(mustBe);
        clearList(giver);

        //Ход белой шашкой и её трансформация в дамку
        giver[5] = 'w';
        ThingsToWorkWith.myWorld(giver);
        executor.move(1, 5);
        mustBe[1] = 'x';
        assertArrayEquals(mustBe, testing.getCheckers());
        clearList(mustBe);
        clearList(giver);

        //Ход чёрной шашки
        giver[0] = 'b';
        ThingsToWorkWith.myWorld(giver);
        executor.move(5, 0);
        mustBe[5] = 'b';
        assertArrayEquals(mustBe, testing.getCheckers());
        clearList(mustBe);
        clearList(giver);

        //Ход чёрной шашки и её трансформация в дамку
        giver[24] = 'b';
        ThingsToWorkWith.myWorld(giver);
        executor.move(30, 24);
        mustBe[30] = 'c';
        assertArrayEquals(mustBe, testing.getCheckers());
    }

    @Test
    void eatingTest() {
        clearList(mustBe);
        clearList(giver);

        //Ход белой шашки со взятием чёрной шашки
        giver[13] = 'w';
        giver[9] = 'b';
        ThingsToWorkWith.myWorld(giver);
        testing.setId(6, 13, false);
        DeskController.checkers[9].setColor('b');
        executor.move(6, 13);
        mustBe[6] = 'w';
        assertArrayEquals(mustBe, testing.getCheckers());
        clearList(mustBe);
        clearList(giver);

        //Ход белой шашки со взятием чёрной шашки и трансформацией в дамку
        giver[5] = 'b';
        giver[8] = 'w';
        ThingsToWorkWith.myWorld(giver);
        testing.setId(1, 8, false);
        DeskController.checkers[5].setColor('b');
        executor.move(1, 8);
        mustBe[1] = 'x';
        assertArrayEquals(mustBe, testing.getCheckers());
        clearList(mustBe);
        clearList(giver);

        //Ход чёрной шашки со взятием белой шашки
        giver[6] = 'b';
        giver[9] = 'w';
        ThingsToWorkWith.myWorld(giver);
        testing.setId(13, 6, true);
        DeskController.checkers[9].setColor('b');
        executor.move(13, 6);
        mustBe[13] = 'b';
        assertArrayEquals(mustBe, testing.getCheckers());
        clearList(mustBe);
        clearList(giver);

        //Ход чёрной шашки со взятием белой шашки и трансформацией в дамку
        giver[21] = 'b';
        giver[25] = 'w';
        ThingsToWorkWith.myWorld(giver);
        testing.setId(30, 21, true);
        DeskController.checkers[25].setColor('b');
        executor.move(30, 21);
        mustBe[30] = 'c';
        assertArrayEquals(mustBe, testing.getCheckers());
        clearList(mustBe);
        clearList(giver);
    }

    @Test
    void testAI() {
        //Развилка: ИИ может пойти вправо и съесть одну шашку противника, либо пойти влево и съесть две.
        clearList(mustBe);
        clearList(giver);
        giver[26] = 'w';
        giver[22] = 'b';
        giver[23] = 'b';
        giver[14] = 'b';
        ThingsToWorkWith.myWorld(giver);
        DeskController.blackMove = false;
        executor.myTurn();
        mustBe[10] = 'w';
        mustBe[23] = 'b';
        assertArrayEquals(mustBe, testing.getCheckers());
        clearList(mustBe);
        clearList(giver);

        //ИИ всех съедает и становится дамкой
        giver[24] = 'w';
        giver[21] = 'b';
        giver[14] = 'b';
        giver[7] = 'b';
        ThingsToWorkWith.myWorld(giver);
        DeskController.blackMove = false;
        executor.myTurn();
        mustBe[3] = 'x';
        assertArrayEquals(mustBe, testing.getCheckers());
        clearList(mustBe);
        clearList(giver);

        //Лабиринт: пройти через доску, не попав в зону поражения врага
        giver[29] = 'w';
        giver[12] = 'w';
        giver[2] = 'w';
        giver[22] = 'b';
        giver[17] = 'b';
        giver[4] = 'b';
        giver[0] = 'b';
        ThingsToWorkWith.myWorld(giver);
        for (int i = 0; i < 7; i++) {
            DeskController.blackMove = false;
            DeskController.canEat = false;
            executor.myTurn();
            if (DeskController.checkers[16].getWhChk() == 'w')
                DeskController.checkers[12].setWhChk('b');
        }
        for (int i = 0; i < 32; i++) {
            if (DeskController.checkers[i].getWhChk() == 'x')
                System.out.println(i);
        }
        mustBe = giver;
        mustBe[29] = 'e';
        mustBe[12] = 'b';
        mustBe[1] = 'x';
        assertArrayEquals(mustBe, testing.getCheckers());
        clearList(mustBe);
        clearList(giver);

    }

    void clearList(char[] list) {
        for (int i = 0; i < 32; i++) {
            list[i] = 'e';
        }
    }
}
