package repository;

import database.DBConnection;
import model.Budget;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BudgetRepository {

    public boolean addBudget(Budget budget){

        String sql = "INSERT INTO budgets(user_id,category_id,limit_amount,month,year) VALUES(?,?,?,?,?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1,budget.getUserId());
            stmt.setInt(2,budget.getCategoryId());
            stmt.setDouble(3,budget.getLimitAmount());
            stmt.setInt(4,budget.getMonth());
            stmt.setInt(5,budget.getYear());

            stmt.executeUpdate();
            return true;

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public double getBudgetLimit(int userId,int categoryId,int month,int year){

        String sql = "SELECT limit_amount FROM budgets WHERE user_id=? AND category_id=? AND month=? AND year=?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1,userId);
            stmt.setInt(2,categoryId);
            stmt.setInt(3,month);
            stmt.setInt(4,year);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return rs.getDouble("limit_amount");
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return 0;
    }
}