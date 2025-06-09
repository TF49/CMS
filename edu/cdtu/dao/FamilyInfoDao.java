package edu.cdtu.dao;

import edu.cdtu.entity.FamilyMember;
import edu.cdtu.utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 家庭成员数据访问对象
 */
public class FamilyInfoDao {

    /**
     * 添加一个家庭成员信息
     *
     * @param member 要添加的家庭成员对象
     * @return 是否添加成功
     */
    public boolean addFamilyMember(FamilyMember member) {
        String sql = "INSERT INTO family_member(census_id, name, relation, gender, age, id_card, phone, remarks, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, member.getCensusId());
            stmt.setString(2, member.getName());
            stmt.setString(3, member.getRelation());
            stmt.setString(4, member.getGender());
            stmt.setInt(5, member.getAge());
            stmt.setString(6, member.getIdCard());
            stmt.setString(7, member.getPhone());
            stmt.setString(8, member.getRemarks());
            stmt.setString(9, member.getAddress());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据身份证号删除家庭成员
     *
     * @param idCard 身份证号码
     * @return 是否删除成功
     */
    public boolean deleteFamilyMemberByIdCard(String idCard) {
        String sql = "DELETE FROM family_member WHERE id_card = ?";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idCard);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新家庭成员信息
     *
     * @param member 新的数据
     * @return 是否更新成功
     */
    public boolean updateFamilyMember(FamilyMember member) {
        String sql = "UPDATE family_member SET name=?, relation=?, gender=?, age=?, phone=?, remarks=?, address=? WHERE id_card=?";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, member.getName());
            stmt.setString(2, member.getRelation());
            stmt.setString(3, member.getGender());
            stmt.setInt(4, member.getAge());
            stmt.setString(5, member.getPhone());
            stmt.setString(6, member.getRemarks());
            stmt.setString(7, member.getAddress());
            stmt.setString(8, member.getIdCard());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询某个户籍人口的所有家庭成员
     *
     * @param censusId 户籍ID（关联 census_data.id）
     * @return 所有记录的列表
     */
    public List<FamilyMember> getFamilyMembersByCensusId(int censusId) {
        List<FamilyMember> members = new ArrayList<>();
        String sql = "SELECT * FROM family_member WHERE census_id = ?";

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, censusId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                FamilyMember member = new FamilyMember();
                member.setId(rs.getInt("id"));
                member.setCensusId(rs.getInt("census_id"));
                member.setName(rs.getString("name"));
                member.setRelation(rs.getString("relation"));
                member.setGender(rs.getString("gender"));
                member.setAge(rs.getInt("age"));
                member.setIdCard(rs.getString("id_card"));
                member.setPhone(rs.getString("phone"));
                member.setRemarks(rs.getString("remarks"));
                member.setAddress(rs.getString("address"));

                members.add(member);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }

    /**
     * 根据身份证号查询单个家庭成员
     *
     * @param idCard 身份证号
     * @return 对应的家庭成员对象，如果没有找到则返回 null
     */
    public FamilyMember getFamilyMemberByIdCard(String idCard) {
        String sql = "SELECT * FROM family_member WHERE id_card = ?";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idCard);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                FamilyMember member = new FamilyMember();
                member.setId(rs.getInt("id"));
                member.setCensusId(rs.getInt("census_id"));
                member.setName(rs.getString("name"));
                member.setRelation(rs.getString("relation"));
                member.setGender(rs.getString("gender"));
                member.setAge(rs.getInt("age"));
                member.setIdCard(rs.getString("id_card"));
                member.setPhone(rs.getString("phone"));
                member.setRemarks(rs.getString("remarks"));
                member.setAddress(rs.getString("address"));

                return member;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 检查身份证号是否已存在
     *
     * @param idCard 身份证号
     * @return 是否存在
     */
    public boolean isIdCardExists(String idCard) {
        String sql = "SELECT COUNT(*) FROM family_member WHERE id_card = ?";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idCard);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 根据姓名模糊查询家庭成员
     *
     * @param name 姓名（支持模糊匹配）
     * @return 匹配的成员列表
     */
    public List<FamilyMember> searchFamilyMembersByName(String name) {
        List<FamilyMember> members = new ArrayList<>();
        String sql = "SELECT * FROM family_member WHERE name LIKE ?";

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                FamilyMember member = new FamilyMember();
                member.setId(rs.getInt("id"));
                member.setCensusId(rs.getInt("census_id"));
                member.setName(rs.getString("name"));
                member.setRelation(rs.getString("relation"));
                member.setGender(rs.getString("gender"));
                member.setAge(rs.getInt("age"));
                member.setIdCard(rs.getString("id_card"));
                member.setPhone(rs.getString("phone"));
                member.setRemarks(rs.getString("remarks"));
                member.setAddress(rs.getString("address"));

                members.add(member);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }
}