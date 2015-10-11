package com.tatteam.patente.entity;

import android.graphics.Bitmap;

/**
 * Created by ThanhNH on 1/31/2015.
 */

public class ExamsEntity  {
    public int questionNo;
    public String question;
    public Bitmap image;
    public int answer;
    public int myAnswer = BaseEntity.ANSWER_NOT_CHOOSE;
}
