package com.bb;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.net.URL;
import java.util.*;

public class DeskController implements Initializable {
    public Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button10, button11,
            button12, button13, button14, button15, button16, button17, button18, button19, button20, button21, button22,
            button23, button24, button25, button26, button27, button28, button29, button30, button31, button32;
    public CheckBox toggleAI;

    private int id, previd, maxrating;
    public Button[] buttons;
    public Checker[] checkers;
    private boolean transBig = false, killStreak = false, canEat = false, blackMove = true;
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

    @FXML
    private void handleAI() {
        AI = toggleAI.isSelected();
        System.out.println(AI);
        if (!blackMove) {
            clearColors();
            myTurn();
        }
    }

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
                if (!blackMove && AI)
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
                if ((checkers[id].getSpecialty2() == LeRi.Left && checkers[id].getSpecialty3() != UpDo.LowerLeft) || checkers[id].getSpecialty2() == LeRi.Right) {
                    if (checkers[id + 4].getWhChk() == 'e')
                        checkers[id + 4].recoloriser('y');
                } else if (checkers[id].getSpecialty1() == OdEv.Odd && checkers[id].getSpecialty2() != LeRi.Right) {
                    if (checkers[id + 5].getWhChk() == 'e')
                        checkers[id + 5].recoloriser('y');
                    if (checkers[id + 4].getWhChk() == 'e')
                        checkers[id + 4].recoloriser('y');
                } else if (checkers[id].getSpecialty1() == OdEv.Even && checkers[id].getSpecialty2() != LeRi.Left && id <= 23) {
                    if (checkers[id + 3].getWhChk() == 'e')
                        checkers[id + 3].recoloriser('y');
                    if (checkers[id + 4].getWhChk() == 'e')
                        checkers[id + 4].recoloriser('y');
                }
            }
            if (!blackMove || checkers[id].getWhChk() == 'x' || checkers[id].getWhChk() == 'c') {
                if ((checkers[id].getSpecialty2() == LeRi.Right && checkers[id].getSpecialty3() != UpDo.UpperRight) || checkers[id].getSpecialty2() == LeRi.Left) {
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
    void clearColors() {
        for (int i = 0; i < 32; i++) {
            checkers[i].recoloriser('g');
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
        /*AtomicBoolean nextPls = new AtomicBoolean(false);
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(
                () -> { System.out.println("do task");
                    nextPls.set(true);
                },
                2, 1,
                TimeUnit.SECONDS);*/

        /*Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            }
        }, 0, 1000);*/


        System.out.println("My turn, hmpf!");
        Map<Integer, ArrayList<Integer>> waysToGo = new HashMap<>();
        for (int i = 0; i < 32; i++) {
            if (checkers[i].getWhChk() == 'w' || checkers[i].getWhChk() == 'x') {
                id = i;
                chooser('m');
                if (!canEat)
                    chooser('n');
                ArrayList<Integer> destinations = new ArrayList<>();
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
            for (Map.Entry<Integer, ArrayList<Integer>> entry : waysToGo.entrySet())
                for (Integer value : entry.getValue()) {
                    ratedOptions.add(rateOption(value, entry.getKey()));
                    System.out.println(rateOption(value, entry.getKey()));
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
            System.out.println("Лучше схожу так " + localprevid + " " + localid + " потому что у него рейтинг " + maxrating);
            if (localid == 0 && localprevid == 0)
                System.out.println("Meh, I lost?");
            id = localprevid;
            clearColors();
            //изменить на 'm'
            chooser('s');
            move(localid, localprevid);
            isthereAKillStreak(localid, localprevid);
            while (killStreak) {
                id = localid;
                chooser('m');
                ArrayList<Integer> destinations = new ArrayList<>();
                for (int j = 0; j < 32; j++) {
                    if (checkers[j].getColor() == 'y')
                        destinations.add(j);
                }
                localprevid = localid;
                try {
                    localid = destinations.get(random.nextInt(destinations.size()));
                    move(localid, localprevid);
                    isthereAKillStreak(localid, localprevid);
                } catch (RuntimeException ignored) {
                    killStreak = false;
                }
            }
            blackMove = !blackMove;
            canEat = false;
            seeIfAnythingsEdible();
            clearColors();
        } catch (RuntimeException ignored) {
            System.out.println("I can't go on");
        }
    }

    private List<Integer> rateOption(int localid, int localprevid) {
        int rating = 0;
        List<Integer> result = new ArrayList<>();
        //Станет ли шашка дамкой?
        if (checkers[localprevid].getWhChk() == 'w' && localprevid <= 7)
            rating += 4;
            //Проверка, стоит ли сбоку
        else if (checkers[localprevid].getSpecialty2() == LeRi.Left || checkers[localprevid].getSpecialty2() == LeRi.Right) {
            rating += 2;
            System.out.println("Сбоку, сбоку заходи!!!");
        }
        //Если не дамка, то стимулирует продвигаться вперёд
        if (checkers[localprevid].getWhChk() == 'w') {
            rating += 1;
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
        int a = id;
        id = localid;
        canEat = false;
        chooser('m');
        if (canEat) {
            rating += 4;
            System.out.println("Я прибью этих ублюдков");
        }
        canEat = false;
        id = a;
        //Съест ли меня враг после этого хода?
        List<Integer> endangered;
        char rememberMe = checkers[localprevid].getWhChk();
        checkers[localprevid].setWhChk('e');
        checkers[localid].setWhChk('w');
        endangered = whosInDanger('y', localid);
        if (endangered.size() != 0) {
            rating -= 11;
            System.out.println("Не лезь, оно тебя сожрёт!!!");
        }
        checkers[localid].setWhChk('e');
        checkers[localprevid].setWhChk(rememberMe);
        //Буду ли я съеден, если не сдвинусь с места
        endangered = whosInDanger('y', localprevid);
        int damkoef = 0;
        if (checkers[localprevid].getWhChk() == 'x')
            damkoef = 4;
        if (endangered.size() != 0) {
            rating += 7 + damkoef;
            System.out.println("Вали-ка лучше отсюдова");
        }
        //Проверка, ставлю ли я кого-нибудь под угрозу, если похожу. Или же спасу?
        endangered = whosInDanger('n', localprevid);
        rememberMe = checkers[localprevid].getWhChk();
        checkers[localprevid].setWhChk('e');
        checkers[localid].setWhChk('w');
        if (endangered.size() > whosInDanger('n', localid).size()) {
            rating += 7;
            System.out.println("Слава спасителю!");
        } else if (endangered.size() < whosInDanger('n', localid).size()) {
            rating -= 7;
            System.out.println("Попридержи-ка коней");
        }
        id = a;
        checkers[localid].setWhChk('e');
        checkers[localprevid].setWhChk(rememberMe);

        if (rating > maxrating)
            maxrating = rating;
        result.add(rating);
        result.add(localprevid);
        result.add(localid);
        return result;
    }

    //Кто-нибудь (или я) в опасности?
    private List<Integer> whosInDanger(char me, int localprevid) {
        List<Integer> danger = new ArrayList<>();
        blackMove = true;
        canEat = false;
        for (int i = 0; i < 32; i++) {
            id = i;
            if (checkers[i].getWhChk() == 'b' || checkers[i].getWhChk() == 'c')
                chooser('m');
        }
        for (int i = 0; i < 32; i++) {
            if ((me == 'n' && i != localprevid) || (me == 'y' && i == localprevid))
                if (checkers[i].getColor() == 'b') {
                    danger.add(i);
                    break;
                }
        }
        blackMove = false;
        canEat = false;
        clearColors();
        return danger;
    }

    //Далее идут методы, связанные с тестированием

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
        buttons = new Button[]{button1, button2, button3, button4, button5, button6, button7, button8,
                button9, button10, button11, button12, button13, button14, button15, button16, button17, button18,
                button19, button20, button21, button22, button23, button24, button25, button26, button27, button28,
                button29, button30, button31, button32};
        checkers = new Checker[32];
        newGame();
        for (int i = 0; i < checko.length; i++) {
            checkers[i].setWhChk(checko[i]);
            checkers[i].setBtn(buttons[i]);
            checkers[i].imageChanger(checko[i]);

        }
        for (int i = checko.length; i < 32; i++) {
            checkers[i].setWhChk('e');
            checkers[i].setBtn(buttons[i]);
        }
        for (int i = 0; i < checko.length; i++) {
            checkers[i].setWhChk(checko[i]);
        }
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
