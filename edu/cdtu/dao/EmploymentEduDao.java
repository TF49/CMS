package edu.cdtu.dao;

import edu.cdtu.entity.EmploymentEdu;
import edu.cdtu.utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * 就业与教育信息数据访问层
 */
public class EmploymentEduDao {

    private static final Logger logger = Logger.getLogger(EmploymentEduDao.class.getName());

    /**
     * 添加就业与教育信息
     *
     * @param info 要添加的信息对象
     * @return 是否添加成功
     */
    public boolean addEmploymentEduInfo(EmploymentEdu info) {
        String sql = "INSERT INTO employment_edu(name, id_card, highest_education, major, school, employment_status, job_title, company, enrollment_year, graduation_year, remarks, census_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, info.getName());
            stmt.setString(2, info.getIdCard());
            stmt.setString(3, info.getHighestEducation());
            stmt.setString(4, info.getMajor());
            stmt.setString(5, info.getSchool());
            stmt.setString(6, info.getEmploymentStatus());
            stmt.setString(7, info.getJobTitle());
            stmt.setString(8, info.getCompany());
            stmt.setInt(9, info.getEnrollmentYear());
            stmt.setInt(10, info.getGraduationYear());
            stmt.setString(11, info.getRemarks());
            stmt.setInt(12, info.getCensusId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            logger.severe("添加就业与教育信息失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 根据身份证号判断是否存在记录
     *
     * @param idCard 身份证号
     * @return 是否存在
     */
    public boolean isExistsByIdCard(String idCard) {
        String sql = "SELECT COUNT(*) FROM employment_edu WHERE id_card = ? AND is_deleted = 0";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idCard);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            logger.severe("根据身份证号检查是否存在失败: " + e.getMessage());
        }

        return false;
    }

    /**
     * 更新就业与教育信息
     *
     * @param info 新的数据
     * @return 是否更新成功
     */
    public boolean updateEmploymentEduInfo(EmploymentEdu info) {
        String sql = "UPDATE employment_edu SET name=?, highest_education=?, major=?, school=?, employment_status=?, job_title=?, company=?, enrollment_year=?, graduation_year=?, remarks=?, census_id=? WHERE id_card=?";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, info.getName());
            stmt.setString(2, info.getHighestEducation());
            stmt.setString(3, info.getMajor());
            stmt.setString(4, info.getSchool());
            stmt.setString(5, info.getEmploymentStatus());
            stmt.setString(6, info.getJobTitle());
            stmt.setString(7, info.getCompany());
            stmt.setInt(8, info.getEnrollmentYear());
            stmt.setInt(9, info.getGraduationYear());
            stmt.setString(10, info.getRemarks());
            stmt.setInt(11, info.getCensusId());
            stmt.setString(12, info.getIdCard());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            logger.severe("更新就业与教育信息失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 根据身份证号查询就业与教育信息（排除已删除）
     *
     * @param idCard 身份证号
     * @return 对应的信息对象，如果没有找到则返回 null
     */
    public EmploymentEdu getEmploymentEduByIdCard(String idCard) {
        String sql = "SELECT id, name, id_card, census_id, highest_education, major, school, employment_status, job_title, company, enrollment_year, graduation_year, remarks, create_time, update_time FROM employment_edu WHERE id_card = ? AND is_deleted = 0";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idCard);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                EmploymentEdu info = new EmploymentEdu();
                info.setId(rs.getInt("id"));
                info.setName(rs.getString("name"));
                info.setIdCard(rs.getString("id_card"));
                info.setCensusId(rs.getInt("census_id"));
                info.setHighestEducation(rs.getString("highest_education"));
                info.setMajor(rs.getString("major"));
                info.setSchool(rs.getString("school"));
                info.setEmploymentStatus(rs.getString("employment_status"));
                info.setJobTitle(rs.getString("job_title"));
                info.setCompany(rs.getString("company"));
                info.setEnrollmentYear(rs.getInt("enrollment_year"));
                info.setGraduationYear(rs.getInt("graduation_year"));
                info.setRemarks(rs.getString("remarks"));
                info.setCreateTime(rs.getTimestamp("create_time"));
                info.setUpdateTime(rs.getTimestamp("update_time"));

                return info;
            }

        } catch (SQLException e) {
            logger.severe("根据身份证号查询就业与教育信息失败: " + e.getMessage());
        }

        return null;
    }
}