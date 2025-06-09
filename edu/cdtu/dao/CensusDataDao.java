package edu.cdtu.dao;

import edu.cdtu.entity.CensusData;
import edu.cdtu.utils.DbUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CensusDataDao
{

    /**
     * 添加一条人口信息
     *
     * @param data 要添加的人口数据对象
     * @return 是否添加成功
     */
    public boolean addCensusData(CensusData data)
    {
        String sql = "INSERT INTO census_data (name, id_card, gender, age, address, phone, registration_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, data.getName());
            pstmt.setString(2, data.getIdCard());
            pstmt.setString(3, data.getGender());
            pstmt.setInt(4, data.getAge());
            pstmt.setString(5, data.getAddress());   // ⬅️ 缺失的就是这一行
            pstmt.setString(6, data.getPhone());
            pstmt.setDate(7, new Date(data.getRegistrationDate().getTime()));

            return pstmt.executeUpdate() > 0;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    // 检查身份证是否存在
    public boolean isIdCardExists(String idCard)
    {
        String sql = "SELECT COUNT(*) FROM census_data WHERE id_card = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, idCard);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                return rs.getInt(1) > 0;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据身份证号删除人口信息
     *
     * @param idCard 身份证号码
     * @return 是否删除成功
     */
    public boolean deleteCensusDataByIdCard(String idCard)
    {
        String sql = "DELETE FROM census_data WHERE id_card = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, idCard);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新人口信息
     *
     * @param data 新的数据
     * @return 是否更新成功
     */
    public boolean updateCensusData(CensusData data)
    {
        String sql = "UPDATE census_data SET name=?, gender=?, age=?, address=?, phone=? WHERE id_card=?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, data.getName());
            stmt.setString(2, data.getGender());
            stmt.setInt(3, data.getAge());
            stmt.setString(4, data.getAddress());
            stmt.setString(5, data.getPhone());
            stmt.setString(6, data.getIdCard());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询所有人口信息
     *
     * @return 所有记录的列表
     */
    public List<CensusData> getAllCensusData() {
        List<CensusData> dataList = new ArrayList<>();
        String sql = "SELECT * FROM census_data";

        try (Connection conn = DbUtils.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                CensusData data = new CensusData();
                data.setId(rs.getInt("id"));
                data.setName(rs.getString("name"));
                data.setIdCard(rs.getString("id_card"));
                data.setGender(rs.getString("gender"));
                data.setAge(rs.getInt("age"));
                data.setAddress(rs.getString("address"));
                data.setPhone(rs.getString("phone"));
                data.setRegistrationDate(rs.getDate("registration_date"));
                dataList.add(data);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * 根据身份证号查询单条记录
     *
     * @param idCard 身份证号
     * @return 对应的人口信息对象，如果没有找到则返回 null
     */
    public CensusData getCensusDataByIdCard(String idCard)
    {
        String sql = "SELECT * FROM census_data WHERE id_card = ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, idCard);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                CensusData data = new CensusData();
                data.setId(rs.getInt("id"));
                data.setName(rs.getString("name"));
                data.setIdCard(rs.getString("id_card"));
                data.setGender(rs.getString("gender"));
                data.setAge(rs.getInt("age"));
                data.setAddress(rs.getString("address"));
                data.setPhone(rs.getString("phone"));
                data.setRegistrationDate(rs.getDate("registration_date"));

                return data;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 模糊查询姓名匹配的人口信息
     *
     * @param keyword 姓名关键词
     * @return 匹配的结果列表
     */
    public List<CensusData> searchCensusDataByName(String keyword)
    {
        List<CensusData> dataList = new ArrayList<>();
        String sql = "SELECT * FROM census_data WHERE name LIKE ?";
        try (Connection conn = DbUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
        {

            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                CensusData data = new CensusData();
                data.setId(rs.getInt("id"));
                data.setName(rs.getString("name"));
                data.setIdCard(rs.getString("id_card"));
                data.setGender(rs.getString("gender"));
                data.setAge(rs.getInt("age"));
                data.setAddress(rs.getString("address"));
                data.setPhone(rs.getString("phone"));
                data.setRegistrationDate(rs.getDate("registration_date"));

                dataList.add(data);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return dataList;
    }
}