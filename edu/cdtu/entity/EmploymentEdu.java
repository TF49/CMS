package edu.cdtu.entity;

import java.sql.Timestamp;

public class EmploymentEdu {
    private int id;
    private int censusId;
    private String name;                // 姓名
    private String idCard;              // 身份证号
    private String highestEducation;    // 最高学历
    private String major;               // 所学专业
    private String school;              // 毕业院校
    private String employmentStatus;    // 就业状态
    private String jobTitle;            // 职务/职业
    private int enrollmentYear;         // 入学年份
    private int graduationYear;         // 毕业年份
    private String remarks;             // 备注信息
    private Timestamp createTime;       // 创建时间
    private Timestamp updateTime;       // 更新时间

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCensusId() {
        return censusId;
    }

    public void setCensusId(int censusId) {
        this.censusId = censusId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getHighestEducation() {
        return highestEducation;
    }

    public void setHighestEducation(String highestEducation) {
        this.highestEducation = highestEducation;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public int getEnrollmentYear() {
        return enrollmentYear;
    }

    public void setEnrollmentYear(int enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "EmploymentEdu{" +
                "id=" + id +
                ", censusId=" + censusId +
                ", name='" + name + '\'' +
                ", idCard='" + idCard + '\'' +
                ", highestEducation='" + highestEducation + '\'' +
                ", major='" + major + '\'' +
                ", school='" + school + '\'' +
                ", employmentStatus='" + employmentStatus + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", enrollmentYear=" + enrollmentYear +
                ", graduationYear=" + graduationYear +
                ", remarks='" + remarks + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
