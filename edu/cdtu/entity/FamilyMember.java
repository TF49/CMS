package edu.cdtu.entity;

public class FamilyMember {
    private int id;
    private int censusId;
    private String name;
    private String relation;
    private int age;
    private String idCard;
    private String phone;
    private String remarks;
    private String address; // 新增的地址字段

    // Getters and Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCensusId() { return censusId; }
    public void setCensusId(int censusId) { this.censusId = censusId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRelation() { return relation; }
    public void setRelation(String relation) { this.relation = relation; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return "FamilyMember{" +
                "id=" + id +
                ", censusId=" + censusId +
                ", name='" + name + '\'' +
                ", relation='" + relation + '\'' +
                ", age=" + age +
                ", idCard='" + idCard + '\'' +
                ", phone='" + phone + '\'' +
                ", remarks='" + remarks + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}