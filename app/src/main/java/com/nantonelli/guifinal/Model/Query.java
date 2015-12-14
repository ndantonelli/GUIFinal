package com.nantonelli.guifinal.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.sql.Time;
import java.util.Calendar;
import java.sql.Date;

/**
 * Created by erikgabrielsen on 12/13/15.
 */
@Table(name="History")
public class Query extends Model {
    @Column(name = "Item")
    public String item;
    @Column(name = "Attribute")
    public String attribute;
    @Column(name = "Time")
    public Date time;


    public Query(){
        super();
    }

    public Query(String item, String attribute){
        super();
        this.item = item;
        this.attribute = attribute;
        java.sql.Date timeNow = new Date(Calendar.getInstance().getTimeInMillis());
        this.time = timeNow;
    }

}
