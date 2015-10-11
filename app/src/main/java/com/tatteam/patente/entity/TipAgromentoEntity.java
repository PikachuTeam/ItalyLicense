package com.tatteam.patente.entity;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThanhNH on 2/7/2015.
 */
public class TipAgromentoEntity {
    public String name;
    public List<BaseEntity> subEntities = new ArrayList<>();
    public Bitmap image;
}
