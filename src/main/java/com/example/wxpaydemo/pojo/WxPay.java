package com.example.wxpaydemo.pojo;

public class WxPay {
    //微信id
    private String openId;
    //分期编号
    private String periodGuid;
    //金额
    private String price;
    //缴费类型
    private String type;
    //公司guid
    private String companyGuid;
    //单位名称
    private String workunit;
    //工种编号
    private String treecode;
    //继续教育培训机构名称
    private String positionName;
    //继续教育工种编号
    private String careerCode;
    //工种名称
    private String careerName;
    //身份证信息
    private String identityCardNumber;
    //用户名称
    private String personName;
    //继续教育用户名称
    private String userName;

    public String getIdentityCardNumber() {
        return identityCardNumber;
    }

    public void setIdentityCardNumber(String identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCareerName() {
        return careerName;
    }

    public void setCareerName(String careerName) {
        this.careerName = careerName;
    }

    public String getWorkunit() {
        return workunit;
    }

    public void setWorkunit(String workunit) {
        this.workunit = workunit;
    }

    public String getTreecode() {
        return treecode;
    }

    public void setTreecode(String treecode) {
        this.treecode = treecode;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getCareerCode() {
        return careerCode;
    }

    public void setCareerCode(String careerCode) {
        this.careerCode = careerCode;
    }

    public String getCompanyGuid() {
        return companyGuid;
    }

    public void setCompanyGuid(String companyGuid) {
        this.companyGuid = companyGuid;
    }

    public String getPeriodGuid() {
        return periodGuid;
    }

    public void setPeriodGuid(String periodGuid) {
        this.periodGuid = periodGuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "WxPay{" +
                "openId='" + openId + '\'' +
                ", periodGuid='" + periodGuid + '\'' +
                ", price='" + price + '\'' +
                ", type='" + type + '\'' +
                ", companyGuid='" + companyGuid + '\'' +
                ", workunit='" + workunit + '\'' +
                ", treecode='" + treecode + '\'' +
                ", positionName='" + positionName + '\'' +
                ", careerCode='" + careerCode + '\'' +
                ", careerName='" + careerName + '\'' +
                ", identityCardNumber='" + identityCardNumber + '\'' +
                ", personName='" + personName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
