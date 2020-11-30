package com.bb;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class DeskController implements Initializable {
    public Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button10, button11,
            button12, button13, button14, button15, button16, button17, button18, button19, button20, button21, button22,
            button23, button24, button25, button26, button27, button28, button29, button30, button31, button32;

    private int id, previd;
    public Button[] buttons;
    public Checker[] checkers;
    private boolean transBig = false, killStreak = false, canEat = false, blackMove = true;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons = new Button[]{button1, button2, button3, button4, button5, button6, button7, button8,
                button9, button10, button11, button12, button13, button14, button15, button16, button17, button18,
                button19, button20, button21, button22, button23, button24, button25, button26, button27, button28,
                button29, button30, button31, button32};
        checkers = new Checker[32];
        newGame();
    }

    //Расстановка шашек по клеткам для новой игры
    public void newGame() {
        for (int i = 0; i < 12; i++) {
            checkers[i] = new Checker('b', 'g', buttons[i]);
        }
        for (int i = 12; i < 20; i++) {
            checkers[i] = new Checker('e', 'g', buttons[i]);
        }
        for (int i = 20; i < 32; i++) {
            checkers[i] = new Checker('w', 'g', buttons[i]);
        }
        clearColors();
        id = 0;
        previd = 1;
        for (int i = 0; i < 12; i++)
            checkers[i].imageChanger('b');
        for (int i = 12; i < 20; i++)
            checkers[i].btn.setGraphic(new ImageView());
        for (int i = 20; i < 32; i++)
            checkers[i].imageChanger('w');
        blackMove = true;
        canEat = false;
    }

    //При нажатии на кнопку: позволяет выбрать шашку и посмотреть доступные для её перемещения клетки, вызвав метод chooser
    @FXML
    private void buttonAction(ActionEvent event) {

        String source = event.getSource().toString();
        id = 0;
        for (int i = 0; i < source.length(); i++) {
            if (Character.isDigit(source.charAt(i)))
                id = id * 10 + Character.getNumericValue(source.charAt(i));
        }
        id--;
        if (killStreak && checkers[id].getColor() != 'y')
            id = previd;
        if (checkers[id].getColor() == 'y') {
            move(id, previd);
            clearColors();
            killStreak = false;
            if (Math.abs(id - previd) >= 7 && !transBig) {
                if ((blackMove && (checkers[id].getWhChk() == 'b' || checkers[id].getWhChk() == 'c'))
                        || (!blackMove && (checkers[id].getWhChk() == 'w' || checkers[id].getWhChk() == 'x'))) {
                    checkers[id].recoloriser('r');
                    chooser('m');
                }
                for (int i = 0; i < 32; i++) {
                    if (checkers[i].getColor() == 'b') {
                        killStreak = true;
                        break;
                    }
                }
            }
            if (!killStreak) {
                checkers[id].recoloriser('g');
                id = 0;
                previd = -1;
                blackMove = !blackMove;
                canEat = false;
                int a = id;
                for (int i = 0; i < 32; i++) {
                    if ((blackMove && (checkers[i].getWhChk() == 'b' || checkers[i].getWhChk() == 'c')) || (!blackMove && (checkers[i].getWhChk() == 'w' || checkers[i].getWhChk() == 'x')) ) {
                        id = i;
                        chooser('m');
                    }
                }
                id = a;
            }

            if (!killStreak)
                clearColors();
        } else if (!killStreak) {
            if (previd != id)
                clearColors();
            if ((blackMove && (checkers[id].getWhChk() == 'b' || checkers[id].getWhChk() == 'c'))
                    || (!blackMove && (checkers[id].getWhChk() == 'w' || checkers[id].getWhChk() == 'x'))) {
                checkers[id].recoloriser('r');
                chooser('s');
            }
        }
        previd = id;
        /*for (int i = 0; i < 31; i++) {
            if(checkers[i].getWhChk() != 'e') {
                id = i;
                chooser('m');
            }
        }*/
    }

    //Закрашивает жёлтым клетки, куда может походить шашка.
    //Если на пути есть вражеская шашка, которую можно съесть, закрашивает её клетку синим
    //shouldEat: 'n' = no; 's' = should; 'm' = must
    //'n' - только ходит; 'm' - только ест; 's' - ходит и, если может, ест
    public void chooser(char shouldEat) {
        boolean idsorter;
        if (shouldEat != 'n') {
            char c1;
            char c2;
            if (blackMove) {
                c1 = 'w';
                c2 = 'x';
            } else {
                c1 = 'b';
                c2 = 'c';
            }
            int pluser;
            if (blackMove || checkers[id].getWhChk() == 'x' || checkers[id].getWhChk() == 'c') {
                idsorter = (id >= 0 && id <= 3) || (id >= 8 && id <= 11) || (id >= 16 && id <= 19);
                if (idsorter)
                    pluser = 5;
                else pluser = 4;
                if (id != 3 && id != 7 && id != 11 && id != 15 && id != 19 && id < 23
                        && checkers[id + 9].getWhChk() == 'e' && (checkers[id + pluser].getWhChk() == c1
                        || checkers[id + pluser].getWhChk() == c2)) {
                    checkers[id + pluser].recoloriser('b');
                    checkers[id + 9].recoloriser('y');
                    canEat = true;
                }
                if (idsorter)
                    pluser = 4;
                else pluser = 3;
                if (id != 0 && id != 4 && id != 8 && id != 12 && id != 16 && id != 20 && id < 24
                        && checkers[id + 7].getWhChk() == 'e' && (checkers[id + pluser].getWhChk() == c1
                        || checkers[id + pluser].getWhChk() == c2)) {
                    checkers[id + pluser].recoloriser('b');
                    checkers[id + 7].recoloriser('y');
                    canEat = true;
                }
            }
            if (!blackMove || checkers[id].getWhChk() == 'x' || checkers[id].getWhChk() == 'c') {
                idsorter = (id >= 24 && id <= 27) || (id >= 8 && id <= 11) || (id >= 16 && id <= 19);
                if (idsorter)
                    pluser = 4;
                else pluser = 5;
                if (id > 8 && id != 12 && id != 16 && id != 20 && id != 24 && id != 28
                        && checkers[id - 9].getWhChk() == 'e' && (checkers[id - pluser].getWhChk() == c1
                        || checkers[id - pluser].getWhChk() == c2)) {
                    checkers[id - pluser].recoloriser('b');
                    checkers[id - 9].recoloriser('y');
                    canEat = true;
                }
                if (idsorter)
                    pluser = 3;
                else pluser = 4;
                if (id > 7 && id != 11 && id != 15 && id != 19 && id != 23 && id != 27 && id != 31
                        && checkers[id - 7].getWhChk() == 'e' && (checkers[id - pluser].getWhChk() == c1
                        || checkers[id - pluser].getWhChk() == c2)) {
                    checkers[id - pluser].recoloriser('b');
                    checkers[id - 7].recoloriser('y');
                    canEat = true;
                }
            }
        }
        if (shouldEat != 'm' && !canEat) {
            if (blackMove || checkers[id].getWhChk() == 'x' || checkers[id].getWhChk() == 'c') {
                if (id == 4 || id == 12 || id == 20 || id == 3 || id == 11 || id == 19 || id == 27) {
                    if (checkers[id + 4].getWhChk() == 'e')
                        checkers[id + 4].recoloriser('y');
                } else if ((id >= 0 && id <= 2) || (id >= 8 && id <= 10) || (id >= 16 && id <= 18)
                        || (id >= 24 && id <= 26)) {
                    if (checkers[id + 5].getWhChk() == 'e')
                        checkers[id + 5].recoloriser('y');
                    if (checkers[id + 4].getWhChk() == 'e')
                        checkers[id + 4].recoloriser('y');
                } else if ((id >= 5 && id <= 7) || (id >= 13 && id <= 15) || (id >= 21 && id <= 23)) {
                    if (checkers[id + 3].getWhChk() == 'e')
                        checkers[id + 3].recoloriser('y');
                    if (checkers[id + 4].getWhChk() == 'e')
                        checkers[id + 4].recoloriser('y');
                }
            }
            if (!blackMove || checkers[id].getWhChk() == 'x' || checkers[id].getWhChk() == 'c') {
                if (id == 4 || id == 12 || id == 20 || id == 28 || id == 11 || id == 19 || id == 27) {
                    if (checkers[id - 4].getWhChk() == 'e')
                        checkers[id - 4].recoloriser('y');
                } else if ((id >= 8 && id <= 10) || (id >= 16 && id <= 18) || (id >= 24 && id <= 26)) {
                    if (checkers[id - 3].getWhChk() == 'e')
                        checkers[id - 3].recoloriser('y');
                    if (checkers[id - 4].getWhChk() == 'e')
                        checkers[id - 4].recoloriser('y');
                } else if ((id >= 5 && id <= 7) || (id >= 13 && id <= 15) || (id >= 21 && id <= 23)
                        || (id >= 29 && id <= 31)) {
                    if (checkers[id - 5].getWhChk() == 'e')
                        checkers[id - 5].recoloriser('y');
                    if (checkers[id - 4].getWhChk() == 'e')
                        checkers[id - 4].recoloriser('y');
                }
            }
        }

    }

    //Передвигает шашку на выбранную клетку. Убирает съеденные шашки, вызвав moveAction.
    //Превращает при достижении конца поля походившую шашку в дамку.
    public void move(int id, int previd) {
        int koef;
        if (blackMove)
            koef = 1;
        else koef = -1;
        moveAction(koef, id, previd);
        if (checkers[previd].getWhChk() == 'c' || checkers[previd].getWhChk() == 'x')
            moveAction(koef * -1, id, previd);
        char name = checkers[previd].getWhChk();
        if (name == 'b' && id >= 28 && id <= 31) {
            name = 'c';
            transBig = true;
        } else if (name == 'w' && id <= 3) {
            name = 'x';
            transBig = true;
        } else transBig = false;
        checkers[id].imageChanger(name);
        checkers[previd].imageChanger('n');
    }

    //Убирает съеденную шашку
    void moveAction(int koef, int id, int previd) {
        try {
            if (Math.abs(id - previd) == 9) {
                if (checkers[id - 4 * koef].getColor() == 'b')
                    checkers[id - 4 * koef].imageChanger('n');
                else if (checkers[id - 5 * koef].getColor() == 'b')
                    checkers[id - 5 * koef].imageChanger('n');
            } else if (Math.abs(id - previd) == 7) {
                if (checkers[id - 4 * koef].getColor() == 'b')
                    checkers[id - 4 * koef].imageChanger('n');
                else if (checkers[id - 3 * koef].getColor() == 'b')
                    checkers[id - 3 * koef].imageChanger('n');
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {}
    }

    //Перекрашивает клетки жёлтого, синего и красного цвета обратно в зелёный
    void clearColors() {
        for (int i = 0; i < 32; i++) {
            checkers[i].recoloriser('g');
        }
    }

    //Далее идут методы, связанные с тестированием

    //Создание поля с кастомной расстановкой шашек
    public void myWorld(char[] checko) {
        Button button1 = new Button();
        Button button2 = new Button();
        Button button3 = new Button();
        Button button4 = new Button();
        Button button5 = new Button();
        Button button6 = new Button();
        Button button7 = new Button();
        Button button8 = new Button();
        Button button9 = new Button();
        Button button10 = new Button();
        Button button11 = new Button();
        Button button12 = new Button();
        Button button13 = new Button();
        Button button14 = new Button();
        Button button15 = new Button();
        Button button16 = new Button();
        Button button17 = new Button();
        Button button18 = new Button();
        Button button19 = new Button();
        Button button20 = new Button();
        Button button21 = new Button();
        Button button22 = new Button();
        Button button23 = new Button();
        Button button24 = new Button();
        Button button25 = new Button();
        Button button26 = new Button();
        Button button27 = new Button();
        Button button28 = new Button();
        Button button29 = new Button();
        Button button30 = new Button();
        Button button31 = new Button();
        Button button32 = new Button();
        buttons = new Button[]{button1, button2, button3, button4, button5, button6, button7, button8,
                button9, button10, button11, button12, button13, button14, button15, button16, button17, button18,
                button19, button20, button21, button22, button23, button24, button25, button26, button27, button28,
                button29, button30, button31, button32};
        checkers = new Checker[32];
        newGame();
        for (int i = 0; i < checko.length; i++) {
            checkers[i] = new Checker(checko[i], 'g', buttons[i]);
        }
        for (int i = checko.length; i < 32; i++) {
            checkers[i] = new Checker('e', 'g', buttons[i]);
        }
        for (int i = 0; i < checko.length; i++) {
            checkers[i].setWhChk(checko[i]);
        }
        for (int i = 0; i < 12; i++)
            checkers[i].imageChanger(checko[i]);
        for (int i = 12; i < 20; i++)
            checkers[i].imageChanger(checko[i]);
        for (int i = 20; i < 32; i++)
            checkers[i].imageChanger(checko[i]);
    }

    //Возвращает информацию о содержании всех зелёных клеток в виде массива
    public char[] getCheckers() {
        char[] whoIsWho = new char[32];
        for (int i = 0; i < 32; i++)
            whoIsWho[i] = checkers[i].getWhChk();
        return whoIsWho;
    }

    //Меняет id выбранной клетки, id предыдущей клетки, и черёд хода
    void setId(int setCur, int setPrev, boolean whoseMove) {
        id = setCur;
        previd = setPrev;
        blackMove = whoseMove;
    }
}
