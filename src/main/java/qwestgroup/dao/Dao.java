package qwestgroup.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import qwestgroup.model.Purchare;

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
    public List<Purchare> allPurchare() {
        List<Purchare> purchares = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM general";
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()){
                Purchare purchare = new Purchare();
                purchare.setCode(resultSet.getString("code"));
                purchare.setName(resultSet.getString("name"));
                purchare.setId(resultSet.getInt("id"));
                purchare.setSection(resultSet.getInt("section"));
                purchare.setGroup(resultSet.getInt("groupp"));
                purchare.setClas(resultSet.getInt("clas"));
                purchare.setCategory(resultSet.getInt("category"));
                purchares.add(purchare);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return purchares;
    }

    @Override
    public void add(List<Purchare> purchares) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into general  values (?,?,?,?,?,?,?)");
            for (int i =1; i<purchares.size();i++) {
                preparedStatement.setInt(1, purchares.get(i).getId());
                preparedStatement.setString(2, purchares.get(i).getCode());
                preparedStatement.setString(3, purchares.get(i).getName());
                preparedStatement.setInt(4, purchares.get(i).getSection());
                preparedStatement.setInt(5, purchares.get(i).getGroup());
                preparedStatement.setInt(6, purchares.get(i).getClas());
                preparedStatement.setInt(7, purchares.get(i).getCategory());
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
    public Purchare getById(int id) {
        return null;
    }
}
