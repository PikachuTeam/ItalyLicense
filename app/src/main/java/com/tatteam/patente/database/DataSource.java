package com.tatteam.patente.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tatteam.patente.entity.BaseEntity;
import com.tatteam.patente.entity.ExamsEntity;
import com.tatteam.patente.entity.QuizPerArgomentoEntity;
import com.tatteam.patente.entity.SheetEntity;
import com.tatteam.patente.entity.TipAgromentoEntity;

import java.util.ArrayList;
import java.util.List;

import tatteam.com.app_common.sqlite.BaseDataSource;

/**
 * Created by ThanhNH on 2/1/2015.
 */
public class DataSource extends BaseDataSource{

    public static final int AM_PARENT_ID_Simulazione_quiz = 3;
    public static final int AM_PARENT_ID_Quiz_per_argomento = 4;
    public static final int AM_PARENT_ID_Lista_delle_domande = 5;

    public static final int B_PARENT_ID_Simulazione_quiz = 6;
    public static final int B_PARENT_ID_Quiz_per_argomento = 7;
    public static final int B_PARENT_ID_Lista_delle_domande = 8;

    public static int getRandomSheetNo(int categoryId) {
        SQLiteDatabase sqLiteDatabase = openConnection();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT Exams.sheetNo FROM Exams where Exams.CategoryID = ? ORDER BY RANDOM() LIMIT 1", new String[]{String.valueOf(categoryId)});
        cursor.moveToFirst();
        int sheetNo = cursor.getInt(0);
        cursor.close();
        closeConnection();
        return sheetNo;
    }

    public static void saveLastScoreInfo(SheetEntity sheetEntity) {
        SQLiteDatabase sqLiteDatabase = openConnection();
        ContentValues values = new ContentValues();
        values.put("CategoryId", sheetEntity.categoryId);
        values.put("SheetNo", sheetEntity.sheetNo);
        values.put("TotalQuestion", sheetEntity.totalQuestion);
        values.put("TotalCorrectAnswer", sheetEntity.totalCorrectAnswer);
        values.put("Duration", sheetEntity.duration);

        if (isExistLastScoreInfo(sheetEntity.categoryId, sheetEntity.sheetNo)) {
            sqLiteDatabase.update("ExamLogs", values, "ExamLogs.categoryID = ? and ExamLogs.sheetNo = ?", new String[]{String.valueOf(sheetEntity.categoryId), String.valueOf(sheetEntity.sheetNo)});
        } else {
            sqLiteDatabase.insert("ExamLogs", null, values);
        }
        closeConnection();
    }

    public static boolean isExistLastScoreInfo(int categoryId, int sheetNo) {
        SQLiteDatabase sqLiteDatabase = openConnection();
        boolean exist = false;
        Cursor cursor = sqLiteDatabase.rawQuery("select * from ExamLogs where ExamLogs.categoryID = ? and ExamLogs.sheetNo = ?", new String[]{String.valueOf(categoryId), String.valueOf(sheetNo)});
        if (cursor.getCount() > 0) {
            exist = true;
        }
        cursor.close();
        closeConnection();
        return exist;
    }

    public static List<SheetEntity> getListExam(int categoryId, int fromSheetNo, int toSheetNo) {
        SQLiteDatabase sqLiteDatabase = openConnection();
        List<SheetEntity> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select Exams.CategoryID, Exams.SheetNo, ExamLogs.totalQuestion,ExamLogs.totalCorrectAnswer, ExamLogs.Duration " +
                        "from Exams  left join ExamLogs on (Exams.categoryID = ExamLogs.categoryID and Exams.sheetNo = ExamLogs.sheetNo)" +
                        " where Exams.CategoryID = ? and Exams.SheetNo >= ? and Exams.SheetNo <= ? group by Exams.sheetNo",
                new String[]{String.valueOf(categoryId), String.valueOf(fromSheetNo), String.valueOf(toSheetNo)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SheetEntity entity = new SheetEntity();
            entity.categoryId = cursor.getInt(0);
            entity.sheetNo = cursor.getInt(1);
            entity.totalQuestion = cursor.getInt(2);
            entity.totalCorrectAnswer = cursor.getInt(3);
            entity.duration = cursor.getInt(4);
            list.add(entity);
            cursor.moveToNext();
        }
        cursor.close();
        closeConnection();
        return list;
    }

    public static List<SheetEntity> getListExam(int categoryId) {
        SQLiteDatabase sqLiteDatabase = openConnection();
        List<SheetEntity> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select Exams.CategoryID, Exams.SheetNo, ExamLogs.totalQuestion,ExamLogs.totalCorrectAnswer, ExamLogs.Duration from Exams  left join ExamLogs on (Exams.categoryID = ExamLogs.categoryID and Exams.sheetNo = ExamLogs.sheetNo) where Exams.CategoryID = ? group by Exams.sheetNo", new String[]{String.valueOf(categoryId)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SheetEntity entity = new SheetEntity();
            entity.categoryId = cursor.getInt(0);
            entity.sheetNo = cursor.getInt(1);
            entity.totalQuestion = cursor.getInt(2);
            entity.totalCorrectAnswer = cursor.getInt(3);
            entity.duration = cursor.getInt(4);
            list.add(entity);
            cursor.moveToNext();
        }
        cursor.close();
        closeConnection();
        return list;
    }

    public static TipAgromentoEntity getTipAgromento(int categoryId) {
        SQLiteDatabase sqLiteDatabase = openConnection();
        TipAgromentoEntity tipAgromentoEntity = new TipAgromentoEntity();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from Questions where Questions.categoryID = ?", new String[]{String.valueOf(categoryId)});
        cursor.moveToFirst();
        int imageId = cursor.getInt(3);
        tipAgromentoEntity.image = getImage(imageId);
        while (!cursor.isAfterLast()) {
            BaseEntity entity = new BaseEntity();
            entity.name = cursor.getString(2);
            entity.answer = cursor.getInt(5);
            tipAgromentoEntity.subEntities.add(entity);
            cursor.moveToNext();
        }
        cursor.close();
        closeConnection();
        return tipAgromentoEntity;
    }

    public  static List<QuizPerArgomentoEntity> getQuizPerArgomento(int categoryId) {
        SQLiteDatabase sqLiteDatabase = openConnection();
        List<QuizPerArgomentoEntity> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("Select Categories.ID, Categories.Name,Images.ImageData from Categories left join Images on (Categories.imageID = Images.ID) where Categories.parentID = ?", new String[]{String.valueOf(categoryId)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            QuizPerArgomentoEntity entity = new QuizPerArgomentoEntity();
            entity.id = cursor.getInt(0);
            entity.name = cursor.getString(1);
            entity.name = Character.toUpperCase(entity.name.charAt(0)) + entity.name.substring(1);
            byte[] imageData = cursor.getBlob(2);
            if (imageData != null) {
                entity.image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            }
            list.add(entity);
            cursor.moveToNext();
        }
        cursor.close();
        closeConnection();
        return list;
    }

    public static List<BaseEntity> getCategories(int categoryId) {
        SQLiteDatabase sqLiteDatabase = openConnection();
        List<BaseEntity> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("Select Categories.ID, Categories.Name,Images.ImageData from Categories left join Images on (Categories.imageID = Images.ID) where Categories.parentID = ?", new String[]{String.valueOf(categoryId)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            BaseEntity entity = new BaseEntity();
            entity.id = cursor.getInt(0);
            entity.name = cursor.getString(1);
            entity.name = Character.toUpperCase(entity.name.charAt(0)) + entity.name.substring(1);
            byte[] imageData = cursor.getBlob(2);
            if (imageData != null) {
                entity.image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            }
            list.add(entity);
            cursor.moveToNext();
        }
        cursor.close();
        closeConnection();
        return list;
    }

    public static boolean havePictureOnDetailArgomento(int argomentoId) {
        SQLiteDatabase sqLiteDatabase = openConnection();
        Cursor cursor = sqLiteDatabase.rawQuery("Select Categories.ID, Categories.Name,Images.ImageData from Categories left join Images on (Categories.imageID = Images.ID) where Categories.parentID = ?", new String[]{String.valueOf(argomentoId)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            byte[] imageData = cursor.getBlob(2);
            if (imageData != null) {
                cursor.close();
                return true;
            }
            cursor.moveToNext();
        }
        cursor.close();
        closeConnection();
        return false;
    }

    public static List<ExamsEntity> getExamList(int categoryId, int sheetNo) {
        SQLiteDatabase sqLiteDatabase = openConnection();
        List<ExamsEntity> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("Select Exams.QuestionNo, Questions.Question, Images.ImageData, Exams.Answer from Exams inner join Questions on Exams.QuestionID = Questions.ID left outer join Images on (Exams.ImageID = Images.ID) where Exams.CategoryID = ? and Exams.SheetNo = ? order by Exams.QuestionNo", new String[]{String.valueOf(categoryId), String.valueOf(sheetNo)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ExamsEntity entity = new ExamsEntity();
            entity.questionNo = cursor.getInt(0);
            entity.question = cursor.getString(1);
            byte[] imageData = cursor.getBlob(2);
            if (imageData != null) {
                entity.image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            }
            entity.answer = cursor.getInt(3);
            list.add(entity);
            cursor.moveToNext();
        }
        cursor.close();
        closeConnection();
        return list;
    }


    public static Bitmap getImage(int imageId) {
        SQLiteDatabase sqLiteDatabase = openConnection();
        Bitmap bitmap = null;
        Cursor cursor = sqLiteDatabase.rawQuery(" select Images.ImageData from Images where Images.ID = ?", new String[]{String.valueOf(imageId)});
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            byte[] imageData = cursor.getBlob(0);
            bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        }
        cursor.close();
        closeConnection();
        return bitmap;
    }
}
