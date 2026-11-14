package com.example.riova.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Models a Journal Object.
 */
public class Journal implements Serializable {
    public String journalId;
    public String title;
    public String ownerId;
    public String dateTime;
    public Date date;
    public String content;
    public String id;
}
