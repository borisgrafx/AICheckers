package com.bb;

import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

//Класс, содержащий побочные рабочие методы. Меньше мусора в Контроллере.
public class ThingsToWorkWith {
    //Создание рейтинга для ИИ
    public static List<Integer> rateOption(int localid, int localprevid, Checker[] checkers) {
        int rating = 0;
        List<Integer> result = new ArrayList<>();




        //Станет ли шашка дамкой?
        if (checkers[localprevid].getWhChk() == 'w' && localprevid <= 7)
            rating += 4;
            //Проверка, стоит ли сбоку
        else if (checkers[localprevid].getSpecialty2() == DeskController.LeRi.Left || checkers[localprevid].getSpecialty2() == DeskController.LeRi.Right) {
            rating += 2;
            System.out.println("Сбоку, сбоку заходи!!!");
        }
        //Если не дамка, то стимулирует продвигаться вперёд
        if (checkers[localprevid].getWhChk() == 'w') {
            rating += 2;
            System.out.println("Вперёд, малой!");
        }
        //Проверка, идёт ли дамка во вражескую зону
        if (checkers[localprevid].getWhChk() == 'x') {
            boolean enemiesNearby = false;
            int i1;
            int i2;
            if (localid <= 5) {
                i1 = 0;
                i2 = 9;
            } else if (localid >= 27) {
                i1 = 21;
                i2 = 31;
            } else {
                i1 = localid - 5;
                i2 = localid + 5;
            }

            for (int i = i1; i <= i2; i++) {
                if (checkers[i].getWhChk() == 'b' || checkers[i].getWhChk() == 'c') {
                    enemiesNearby = true;
                    break;
                }
            }
            if (enemiesNearby) {
                rating += 1;
                System.out.println("Дамка, враги рядом!");
            } else {
                rating -= 1;
                System.out.println("Дамка, врагов нет!");
            }
        }
        //Будет ли возможность убить врага в следующий раз
        List<Integer> endangered;
        char rememberMe;
        int a = DeskController.id;
        DeskController.id = localid;
        DeskController.canEat = false;
        DeskController.chooser('m');
        if (DeskController.canEat) {
            rating += 4;
            System.out.println("Я прибью этих ублюдков");
        }
        DeskController.canEat = false;
        DeskController.id = a;
        //Должен ли я есть врага  в данный момент?
        DeskController.id = localprevid;
        boolean eat = false;
        DeskController.clearColors();
        DeskController.chooser('m');
        for (int i = 0; i < 32; i++) {
            if (checkers[i].getColor() == 'b') {
                eat = true;
                break;
            }
        }
        //Съест ли меня враг после этого хода?
        rememberMe = checkers[localprevid].getWhChk();
        checkers[localprevid].setWhChk('e');
        checkers[localid].setWhChk('w');
        endangered = whosInDanger('y', localid, checkers);
        if (endangered.size() != 0 && !eat) {
            rating -= 11;
            System.out.println("Не лезь, оно тебя сожрёт!!!");
        }
        checkers[localid].setWhChk('e');
        checkers[localprevid].setWhChk(rememberMe);

        //Буду ли я съеден, если не сдвинусь с места
        endangered = whosInDanger('y', localprevid, checkers);
        int damkoef = 0;
        if (checkers[localprevid].getWhChk() == 'x')
            damkoef = 4;
        if (endangered.size() != 0) {
            rating += 7 + damkoef;
            System.out.println("Вали-ка лучше отсюдова");
        }
        //Проверка, ставлю ли я кого-нибудь под угрозу, если похожу. Или же спасу?
        endangered = whosInDanger('n', localprevid, checkers);
        rememberMe = checkers[localprevid].getWhChk();
        checkers[localprevid].setWhChk('e');
        checkers[localid].setWhChk('w');
        if (endangered.size() > whosInDanger('n', localid, checkers).size()) {
            rating += 7;
            System.out.println("Слава спасителю!");
        } else if (endangered.size() < whosInDanger('n', localid, checkers).size()) {
            rating -= 8;
            System.out.println("Попридержи-ка коней");
        }
        DeskController.id = a;
        checkers[localid].setWhChk('e');
        checkers[localprevid].setWhChk(rememberMe);

        if (rating > DeskController.maxrating)
            DeskController.maxrating = rating;
        result.add(rating);
        result.add(localprevid);
        result.add(localid);
        return result;
    }

    //Кто-нибудь (или я) в опасности?
    private static List<Integer> whosInDanger(char me, int localprevid, Checker[] checkers) {
        List<Integer> danger = new ArrayList<>();
        DeskController.blackMove = true;
        DeskController.canEat = false;
        for (int i = 0; i < 32; i++) {
            DeskController.id = i;
            if (checkers[i].getWhChk() == 'b' || checkers[i].getWhChk() == 'c')
                DeskController.chooser('m');
        }
        for (int i = 0; i < 32; i++) {
            if ((me == 'n' && i != localprevid) || (me == 'y' && i == localprevid))
                if (checkers[i].getColor() == 'b') {
                    danger.add(i);
                    break;
                }
        }
        DeskController.blackMove = false;
        DeskController.canEat = false;
        DeskController.clearColors();
        return danger;
    }


    //Методы для тестов
    //Создание поля с кастомной расстановкой шашек
    public void myWorld(char[] checko) {
        Button button1 = new Button(), button2 = new Button(), button3 = new Button(), button4 = new Button(),
                button5 = new Button(), button6 = new Button(), button7 = new Button(), button8 = new Button(),
                button9 = new Button(), button10 = new Button(), button11 = new Button(), button12 = new Button(),
                button13 = new Button(), button14 = new Button(), button15 = new Button(), button16 = new Button(),
                button17 = new Button(), button18 = new Button(), button19 = new Button(), button20 = new Button(),
                button21 = new Button(), button22 = new Button(), button23 = new Button(), button24 = new Button(),
                button25 = new Button(), button26 = new Button(), button27 = new Button(), button28 = new Button(),
                button29 = new Button(), button30 = new Button(), button31 = new Button(), button32 = new Button();
        DeskController.buttons = new Button[]{button1, button2, button3, button4, button5, button6, button7, button8,
                button9, button10, button11, button12, button13, button14, button15, button16, button17, button18,
                button19, button20, button21, button22, button23, button24, button25, button26, button27, button28,
                button29, button30, button31, button32};
        DeskController.checkers = new Checker[32];
        Checker.makeYourself(DeskController.checkers, DeskController.buttons);
        DeskController.clearColors();
        DeskController.id = 0;
        DeskController.previd = 1;
        DeskController.blackMove = true;
        DeskController.canEat = false;
        for (int i = 0; i < checko.length; i++) {
            DeskController.checkers[i].setWhChk(checko[i]);
            DeskController.checkers[i].setBtn(DeskController.buttons[i]);
            DeskController.checkers[i].imageChanger(checko[i]);

        }
        for (int i = checko.length; i < 32; i++) {
            DeskController.checkers[i].setWhChk('e');
            DeskController.checkers[i].setBtn(DeskController.buttons[i]);
        }
        for (int i = 0; i < checko.length; i++) {
            DeskController.checkers[i].setWhChk(checko[i]);
        }
    }

    //Возвращает информацию о содержании всех зелёных клеток в виде массива
    public char[] getCheckers() {
        char[] whoIsWho = new char[32];
        for (int i = 0; i < 32; i++)
            whoIsWho[i] = DeskController.checkers[i].getWhChk();
        return whoIsWho;
    }

    //Меняет id выбранной клетки, id предыдущей клетки, и черёд хода
    void setId(int setCur, int setPrev, boolean whoseMove) {
        DeskController.id = setCur;
        DeskController.previd = setPrev;
        DeskController.blackMove = whoseMove;
    }
}
