package ru.project.wtf.system.timer;

import javafx.animation.Timeline;

public class SecondProperties {

    public Integer second;

    public Timeline timeLine;

    public void reduceSecond() {
        second--;
    }

    public void numericFormatSecond() {
        if (second == 0)
            second = 59;
        else
            reduceSecond();
    }
}