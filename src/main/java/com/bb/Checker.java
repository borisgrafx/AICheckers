package com.bb;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public class Checker {
    //'b' - black, чёрная шашка; 'w' - white, белая шашка; 'c' - [b + 1], чёрная дамка; 'x' - [w + 1], белая дамка
    private char whChk;
    //'g' - green; 'y' - yellow; 'r' - red; 'b' - blue
    private char color;
    public Button btn;

    public Checker(char whChk, char color, Button btn) {
        this.whChk = whChk;
        this.color = color;
        this.btn = btn;
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

    public char getWhChk() {
        return whChk;
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
}
