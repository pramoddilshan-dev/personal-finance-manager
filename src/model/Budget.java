package model;

public class Budget {

    private int id;
    private int userId;
    private int categoryId;
    private double limitAmount;
    private int month;
    private int year;

    public Budget(){}

    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }

    public int getUserId(){ return userId; }
    public void setUserId(int userId){ this.userId = userId; }

    public int getCategoryId(){ return categoryId; }
    public void setCategoryId(int categoryId){ this.categoryId = categoryId; }

    public double getLimitAmount(){ return limitAmount; }
    public void setLimitAmount(double limitAmount){ this.limitAmount = limitAmount; }

    public int getMonth(){ return month; }
    public void setMonth(int month){ this.month = month; }

    public int getYear(){ return year; }
    public void setYear(int year){ this.year = year; }
}