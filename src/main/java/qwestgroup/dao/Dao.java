package qwestgroup.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import qwestgroup.model.Purchase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ComponentScan(basePackages = "qwestgroup")
@PropertySource(value = "classpath:db2.properties")
@Repository
public class Dao implements DaoINT {
    private static Environment environment;
    private static String url;
    private static String username ;
    private static String password ;
    private static String forName ;
    private static Connection connection;
    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        url = environment.getRequiredProperty("jdbc.url");
        username = environment.getRequiredProperty("jdbc.username");
        password = environment.getRequiredProperty("jdbc.password");
        forName = environment.getRequiredProperty("jdbc.driverClassName");
        try {
            Class.forName(forName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try  {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }
    @Override
    public List<Purchase> allPurchare() {
        List<Purchase> purchases = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM general";
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()){
                Purchase purchase = new Purchase();
                purchase.setCode(resultSet.getString("code"));
                purchase.setName(resultSet.getString("name"));
                purchase.setId(resultSet.getInt("id"));
                purchase.setSection(resultSet.getInt("section"));
                purchase.setGroup(resultSet.getInt("groupp"));
                purchase.setClas(resultSet.getInt("clas"));
                purchase.setCategory(resultSet.getInt("category"));
                purchases.add(purchase);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return purchases;
    }

    @Override
    public void add(List<Purchase> purchases) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into general  values (?,?,?,?,?,?,?)");
            for (int i = 1; i< purchases.size(); i++) {
                preparedStatement.setInt(1, purchases.get(i).getId());
                preparedStatement.setString(2, purchases.get(i).getCode());
                preparedStatement.setString(3, purchases.get(i).getName());
                preparedStatement.setInt(4, purchases.get(i).getSection());
                preparedStatement.setInt(5, purchases.get(i).getGroup());
                preparedStatement.setInt(6, purchases.get(i).getClas());
                preparedStatement.setInt(7, purchases.get(i).getCategory());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void deleteAll() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM general");
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Purchase getById(int id) {
        return null;
    }
}
