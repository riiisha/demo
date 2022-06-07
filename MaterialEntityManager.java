package org.glove.app.manager;

import org.glove.app.App;
import org.glove.app.entity.MaterialEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialEntityManager {
    public static List<MaterialEntity> selectAll() throws SQLException{
        try (Connection c = App.getConnection()){
            String sql = "SELECT * FROM material";
            Statement s = c.createStatement();
            ResultSet resultSet = s.executeQuery(sql);

            List<MaterialEntity> list = new ArrayList<>();

            while (resultSet.next()){
                list.add(new MaterialEntity(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("materialType"),
                        resultSet.getString("imagePath"),
                        resultSet.getDouble("cost"),
                        resultSet.getDouble("countInStock"),
                        resultSet.getDouble("minCount"),
                        resultSet.getInt("countInPack"),
                        resultSet.getString("unit")

                        ));
            }
            return list;
        }
    }

    public static void insert(MaterialEntity material) throws SQLException{
        String sql = "INSERT INTO material(Title, MaterialType, ImagePath, Cost, CountInStock, MinCount, CountInPack, Unit) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = App.getConnection()){
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setString(1, material.getTitle());
            preparedStatement.setString(2, material.getMaterialType());
            preparedStatement.setString(3,material.getImagePath());
            preparedStatement.setDouble(4, material.getCost());
            preparedStatement.setDouble(5,material.getCountInStock());
            preparedStatement.setDouble(6,material.getMinCount());
            preparedStatement.setInt(7, material.getCountInPack());
            preparedStatement.setString(8, material.getUnit());
            preparedStatement.executeUpdate();


        }

    }

    public static void update(MaterialEntity material) throws SQLException{
        String sql = "UPDATE material set Title=?, MaterialType=?, ImagePath=?, Cost=?, CountInStock=?, MinCount=?, CountInPack=?, Unit=? WHERE ID = ?";
        try (Connection c = App.getConnection()){
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setString(1, material.getTitle());
            preparedStatement.setString(2, material.getMaterialType());
            preparedStatement.setString(3,material.getImagePath());
            preparedStatement.setDouble(4, material.getCost());
            preparedStatement.setDouble(5,material.getCountInStock());
            preparedStatement.setDouble(6,material.getMinCount());
            preparedStatement.setInt(7, material.getCountInPack());
            preparedStatement.setString(8, material.getUnit());
            preparedStatement.setInt(9,material.getId());

            preparedStatement.executeUpdate();


        }
    }

    public static void delete(MaterialEntity material) throws SQLException {
        String sql="DELETE FROM material WHERE ID=?;";
        try(Connection c=App.getConnection()){
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setInt(1,material.getId());
            preparedStatement.executeUpdate();
        }
    }
}
