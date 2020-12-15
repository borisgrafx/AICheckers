package com.bb;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.*;

public class DeskController implements Initializable {
    public static int id, previd, maxrating;
    public Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button10, button11,
            button12, button13, button14, button15, button16, button17, button18, button19, button20, button21, button22,
            button23, button24, button25, button26, button27, button28, button29, button30, button31, button32;
    public CheckBox toggleAI;
    public ObservableList<String> options = FXCollections.observableArrayList("Чёрные", "Белые");
    public static Button[] buttons;
    public static Checker[] checkers;
    public static boolean transBig = false, killStreak = false, canEat = false, blackMove = true;

    private boolean AI = false;

    public enum OdEv {
        Odd,
        Even,
        NotSpecial
    }

    public enum LeRi {
        Left,
        Right,
        NotSpecial
    }

    public enum UpDo {
        UpperLeft,
        UpperRight,
        LowerLeft,
        LowerRight,
        NotSpecial
    }

    /*@FXML
    private final ChoiceBox<String> whoIsAI = new ChoiceBox<>(options);*/
    @FXML
    private ChoiceBox<String> whoIsAI;

    @FXML
    public void initChoiceBox() {
        whoIsAI.getItems().addAll(options);
        whoIsAI.setValue("Белые");
    }

    @FXML
    public void gogogo(){
        myTurn();/*
        if (whoIsAI.getValue().equals(options.get(1)))
            whoIsAI.setValue(options.get(0));
        else whoIsAI.setValue(options.get(1));
        handleAI();*/
    }

    @FXML
    private void handleAI() {
        AI = toggleAI.isSelected();
        System.out.println(AI);
        System.out.println(whoIsAI.getValue());
        if (!blackMove && whoIsAI.getValue().equals(options.get(1)) && AI) {
            clearColors();
            myTurn();
        } else if (blackMove && whoIsAI.getValue().equals(options.get(0)) && AI){
            clearColors();
            myTurn();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initChoiceBox();
        buttons = new Button[]{button1, button2, button3, button4, button5, button6, button7, button8,
                button9, button10, button11, button12, button13, button14, button15, button16, button17, button18,
                button19, button20, button21, button22, button23, button24, button25, button26, button27, button28,
                button29, button30, button31, button32};
        checkers = new Checker[32];
        newGame();
    }

    //Расстановка шашек по клеткам для новой игры
    public void newGame() {
        Checker.makeYourself(checkers, buttons);
        clearColors();
        id = 0;
        previd = 1;
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
        //System.out.println(checkers[id].getSpecialty1() + " " + checkers[id].getSpecialty2() + " " + checkers[id].getSpecialty3());
        if (killStreak && checkers[id].getColor() != 'y')
            id = previd;
        if (checkers[id].getColor() == 'y') {
            move(id, previd);
            clearColors();
            killStreak = false;
            isthereAKillStreak(id, previd);
            if (!killStreak) {
                checkers[id].recoloriser('g');
                blackMove = !blackMove;
                canEat = false;
                seeIfAnythingsEdible();
                id = 0;
                previd = -1;
                clearColors();

                if (((!blackMove && whoIsAI.getValue().equals(options.get(1))) || (blackMove && whoIsAI.getValue().equals(options.get(0)))) && AI)
                    myTurn();

            }
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
    }

    //Закрашивает жёлтым клетки, куда может походить шашка.
    //Если на пути есть вражеская шашка, которую можно съесть, закрашивает её клетку синим
    //shouldEat: 'n' = no; 's' = should; 'm' = must
    //'n' - только ходит; 'm' - только ест; 's' - ходит и, если может, ест
    public static void chooser(char shouldEat) {
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
                if (checkers[id].getSpecialty2() != LeRi.Right && id != 7 && id != 15 && id < 23
                        && checkers[id + 9].getWhChk() == 'e' && (checkers[id + pluser].getWhChk() == c1
                        || checkers[id + pluser].getWhChk() == c2)) {
                    checkers[id + pluser].recoloriser('b');
                    checkers[id + 9].recoloriser('y');
                    canEat = true;
                }
                if (idsorter)
                    pluser = 4;
                else pluser = 3;
                if (checkers[id].getSpecialty2() != LeRi.Left && id != 0 && id != 8 && id != 16 && id < 24
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
                if (id > 8 && id != 16 && id != 24 && checkers[id].getSpecialty2() != LeRi.Left
                        && checkers[id - 9].getWhChk() == 'e' && (checkers[id - pluser].getWhChk() == c1
                        || checkers[id - pluser].getWhChk() == c2)) {
                    checkers[id - pluser].recoloriser('b');
                    checkers[id - 9].recoloriser('y');
                    canEat = true;
                }
                if (idsorter)
                    pluser = 3;
                else pluser = 4;
                if (id > 7 && id != 15 && id != 23 && id != 31 && checkers[id].getSpecialty2() != LeRi.Right
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
                if ((checkers[id].getSpecialty2() == LeRi.Left && checkers[id].getSpecialty3() != UpDo.LowerLeft) ||
                        checkers[id].getSpecialty2() == LeRi.Right) {
                    if (checkers[id + 4].getWhChk() == 'e')
                        checkers[id + 4].recoloriser('y');
                } else if (checkers[id].getSpecialty1() == OdEv.Odd && checkers[id].getSpecialty2() != LeRi.Right) {
                    if (checkers[id + 5].getWhChk() == 'e')
                        checkers[id + 5].recoloriser('y');
                    if (checkers[id + 4].getWhChk() == 'e')
                        checkers[id + 4].recoloriser('y');
                } else if (checkers[id].getSpecialty1() == OdEv.Even && checkers[id].getSpecialty2() != LeRi.Left &&
                        id <= 23) {
                    if (checkers[id + 3].getWhChk() == 'e')
                        checkers[id + 3].recoloriser('y');
                    if (checkers[id + 4].getWhChk() == 'e')
                        checkers[id + 4].recoloriser('y');
                }
            }
            if (!blackMove || checkers[id].getWhChk() == 'x' || checkers[id].getWhChk() == 'c') {
                if ((checkers[id].getSpecialty2() == LeRi.Right && checkers[id].getSpecialty3() != UpDo.UpperRight) ||
                        checkers[id].getSpecialty2() == LeRi.Left) {
                    if (checkers[id - 4].getWhChk() == 'e')
                        checkers[id - 4].recoloriser('y');
                } else if (id >= 8 && checkers[id].getSpecialty2() != LeRi.Right && checkers[id].getSpecialty1() == OdEv.Odd) {
                    if (checkers[id - 3].getWhChk() == 'e')
                        checkers[id - 3].recoloriser('y');
                    if (checkers[id - 4].getWhChk() == 'e')
                        checkers[id - 4].recoloriser('y');
                } else if (checkers[id].getSpecialty1() == OdEv.Even && checkers[id].getSpecialty2() != LeRi.Left) {
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
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    //Перекрашивает клетки жёлтого, синего и красного цвета обратно в зелёный
    static void clearColors() {
        for (int i = 0; i < 32; i++) {
            DeskController.checkers[i].recoloriser('g');
        }
    }

    void seeIfAnythingsEdible() {
        for (int i = 0; i < 32; i++) {
            if ((blackMove && (checkers[i].getWhChk() == 'b' || checkers[i].getWhChk() == 'c')) || (!blackMove && (checkers[i].getWhChk() == 'w' || checkers[i].getWhChk() == 'x'))) {
                id = i;
                chooser('m');
            }
        }
        clearColors();
    }

    void isthereAKillStreak(int curid, int pastid) {
        killStreak = false;
        if (Math.abs(curid - pastid) >= 7 && !transBig) {
            if ((blackMove && (checkers[curid].getWhChk() == 'b' || checkers[curid].getWhChk() == 'c'))
                    || (!blackMove && (checkers[curid].getWhChk() == 'w' || checkers[curid].getWhChk() == 'x'))) {
                checkers[curid].recoloriser('r');
                chooser('m');
            }
            for (int i = 0; i < 32; i++) {
                if (checkers[i].getColor() == 'b') {
                    killStreak = true;
                    break;
                }
            }
        }
    }

    //ИИ
    public void myTurn() {
        char[] forWhite = new char[]{'w', 'x', 'b', 'c'};
        char[] forBlack = new char[]{'b', 'c', 'w', 'x'};
        char[] current;
        if (blackMove) {
            current = forBlack;
        }
        else {
            current = forWhite;
        }

        //System.out.println("My turn, hmpf!");
        Map<Integer, List<Integer>> waysToGo = new HashMap<>();
        List<Integer> destinations;
        for (int i = 0; i < 32; i++) {
            if (checkers[i].getWhChk() == current[0] || checkers[i].getWhChk() == current[1]) {
                id = i;
                chooser('m');
                if (!canEat)
                    chooser('n');
                destinations = new ArrayList<>();
                for (int j = 0; j < 32; j++) {
                    if (checkers[j].getColor() == 'y')
                        destinations.add(j);
                }
                clearColors();
                if (destinations.size() != 0)
                    waysToGo.put(i, destinations);
            }
        }
        final Random random = new Random();
        try {
            //Список возможных ходов и оценка их целесообразности
            maxrating = Integer.MIN_VALUE;
            List<List<Integer>> ratedOptions = new ArrayList<>();
            for (Map.Entry<Integer, List<Integer>> entry : waysToGo.entrySet())
                for (Integer value : entry.getValue()) {
                    destinations = ThingsToWorkWith.rateOption(value, entry.getKey(), checkers, current);
                    ratedOptions.add(destinations);
                    //System.out.println(destinations);
                }
            //Выборка лучшего варианта
            int localid;
            int localprevid;
            ArrayList<Integer> from = new ArrayList<>();
            ArrayList<Integer> to = new ArrayList<>();
            for (List<Integer> curlist : ratedOptions)
                if (curlist.get(0) == maxrating) {
                    from.add(curlist.get(1));
                    to.add(curlist.get(2));
                }
            int bestWay = random.nextInt(from.size());
            localprevid = from.get(bestWay);
            localid = to.get(bestWay);
            //System.out.println("Лучше схожу так " + localprevid + " " + localid + " потому что у него рейтинг " + maxrating);
            if (localid == 0 && localprevid == 0) {
                System.out.println("Meh, I lost?");
            }
            id = localprevid;
            clearColors();
            chooser('m');
            move(localid, localprevid);
            clearColors();
            id = localid;
            isthereAKillStreak(localid, localprevid);
            clearColors();
            while (killStreak) {
                maxrating = Integer.MIN_VALUE;
                id = localid;
                chooser('m');
                destinations = new ArrayList<>();
                ratedOptions = new ArrayList<>();
                for (int j = 0; j < 32; j++) {
                    if (checkers[j].getColor() == 'y') {
                        destinations.add(j);
                    }
                }
                for (Integer val : destinations) {
                    ratedOptions.add(ThingsToWorkWith.rateOption(val, localid, checkers, current));
                }
                to = new ArrayList<>();
                for (List<Integer> curlist : ratedOptions)
                    if (curlist.get(0) == maxrating) {
                        to.add(curlist.get(2));
                    }
                if (to.size() == 0) {
                    killStreak = false;
                    break;
                }
                id = localid;
                chooser('m');
                localprevid = localid;
                bestWay = random.nextInt(to.size());
                localid = to.get(bestWay);
                move(localid, localprevid);
                //System.out.println(localprevid + " " + localid);
                isthereAKillStreak(localid, localprevid);
            }

            blackMove = !blackMove;
            canEat = false;
            seeIfAnythingsEdible();
            clearColors();
        } catch (RuntimeException ignored) {
            //System.out.println("I can't go on");
        }
    }
}
