package com.bb;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

import com.bb.DeskController.OdEv;
import com.bb.DeskController.LeRi;
import com.bb.DeskController.UpDo;

public class Checker {
    //'b' - black, чёрная шашка; 'w' - white, белая шашка; 'c' - [b + 1], чёрная дамка; 'x' - [w + 1], белая дамка
    private char whChk;
    //'g' - green; 'y' - yellow; 'r' - red; 'b' - blue
    private char color;
    public Button btn;
    private OdEv specialty1;
    private LeRi specialty2;
    private UpDo specialty3;

    public Checker(char whChk, char color, Button btn, OdEv specialty1, LeRi specialty2, UpDo specialty3) {
        this.whChk = whChk;
        this.color = color;
        this.btn = btn;
        this.specialty1 = specialty1;
        this.specialty2 = specialty2;
        this.specialty3 = specialty3;
    }

    //Перекрашивает клетку в выбранный цвет
    //Жёлтый #FFFF00
    //Красный #FF0000
    //Синий #2E9AFE
    //Зелёный дефолт #006400
    void recoloriser(Checker this, char newColor) {
        color = newColor;
        switch (newColor) {
            case 'b':
                btn.setStyle("-fx-background-color: #2E9AFE;");
                break;
            case 'y':
                btn.setStyle("-fx-background-color: #FFFF00;");
                break;
            case 'r':
                btn.setStyle("-fx-background-color: #FF0000;");
                break;
            default:
                btn.setStyle("-fx-background-color: #006400;");
                break;
        }
    }



    //Меняет шашку, стоящую в клетке
    void imageChanger(Checker this, char name) {
        if (name != 'n' && name != 'e') {
            InputStream input = App.class.getResourceAsStream("pics/" + name+ ".png");
            Image image = new Image(input);
            ImageView imageView = new ImageView(image);
            btn.setGraphic(imageView);
            whChk = name;
        } else {
            whChk = 'e';
            btn.setGraphic(new ImageView());
        }

    }

    public OdEv getSpecialty1() {
        return specialty1;
    }

    public LeRi getSpecialty2() {
        return specialty2;
    }

    public UpDo getSpecialty3() {
        return specialty3;
    }

    static Checker[] makeYourself(Checker checkers[], Button buttons[]) {
        for (int i = 0; i < 12; i++) {
            checkers[i] = new Checker('b', 'g', buttons[i], OdEv.NotSpecial, LeRi.NotSpecial, UpDo.NotSpecial);
        }
        for (int i = 12; i < 20; i++) {
            checkers[i] = new Checker('e', 'g', buttons[i], OdEv.NotSpecial, LeRi.NotSpecial, UpDo.NotSpecial);
        }
        for (int i = 20; i < 32; i++) {
            checkers[i] = new Checker('w', 'g', buttons[i], OdEv.NotSpecial, LeRi.NotSpecial, UpDo.NotSpecial);
        }
        //Присвоение особенностей клеткам
        for (int i = 0; i < 4; i++) {
            int k = i;
            while(k < 31) {
                checkers[k].setSpecialty1(OdEv.Odd);
                k += 8;
            }
        }
        for (int i = 4; i < 8; i++) {
            int k = i;
            while(k < 32) {
                checkers[k].setSpecialty1(OdEv.Even);
                k += 8;
            }
        }
        int k = 4;
        while (k < 32) {
            checkers[k].setSpecialty2(LeRi.Left);
            k += 8;
        }
        k = 3;
        while (k < 32) {
            checkers[k].setSpecialty2(LeRi.Right);
            k += 8;
        }
        checkers[3].setSpecialty3(UpDo.UpperRight);
        checkers[4].setSpecialty3(UpDo.UpperLeft);
        checkers[27].setSpecialty3(UpDo.LowerRight);
        checkers[28].setSpecialty3(UpDo.LowerLeft);
        for (int i = 0; i < 12; i++)
            checkers[i].imageChanger('b');
        for (int i = 12; i < 20; i++)
            checkers[i].btn.setGraphic(new ImageView());
        for (int i = 20; i < 32; i++)
            checkers[i].imageChanger('w');
        return checkers;
    }

    public char getWhChk() {
        return whChk;
    }

    public void setSpecialty1(OdEv specialty1) {
        this.specialty1 = specialty1;
    }

    public void setSpecialty2(LeRi specialty2) {
        this.specialty2 = specialty2;
    }

    public void setSpecialty3(UpDo specialty3) {
        this.specialty3 = specialty3;
    }

    public void setWhChk(char whChk) {
        this.whChk = whChk;
    }

    public char getColor() {
        return color;
    }

    public void setColor(char color) {
        this.color = color;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}
