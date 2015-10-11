package com.tatteam.patente.entity;

/**
 * Created by ThanhNH on 2/11/2015.
 */
public class SheetEntity {
    public int categoryId;
    public int sheetNo;
    public int totalQuestion;
    public int totalCorrectAnswer;
    public int duration;


    public SheetEntity(){}
    public SheetEntity(int categoryId, int sheetNo, int totalQuestion, int totalCorrectAnswer, int duration) {
        this.categoryId = categoryId;
        this.sheetNo = sheetNo;
        this.totalQuestion = totalQuestion;
        this.totalCorrectAnswer = totalCorrectAnswer;
        this.duration = duration;
    }
}
