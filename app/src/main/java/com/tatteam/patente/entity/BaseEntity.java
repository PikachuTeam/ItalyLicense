package com.tatteam.patente.entity;

import android.graphics.Bitmap;

/**
 * Created by ThanhNH on 2/5/2015.
 */
public class BaseEntity {
    public static final int ANSWER_NOT_CHOOSE = -1;
    public static final int ANSWER_RIGHT = 1;
    public static final int ANSWER_WRONG = 0;


    public int id;
    public String name;
    public Bitmap image;
    public int answer = ANSWER_NOT_CHOOSE;
}
