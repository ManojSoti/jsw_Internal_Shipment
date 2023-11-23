package com.company.jsw_internal_shipment;

public class UserBeanClass {
    String username,name,mobile_no,yard_id,yard_name,email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getYard_id() {
        return yard_id;
    }

    public void setYard_id(String yard_id) {
        this.yard_id = yard_id;
    }

    public String getYard_name() {
        return yard_name;
    }

    public void setYard_name(String yard_name) {
        this.yard_name = yard_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserBeanClass{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", mobile_no='" + mobile_no + '\'' +
                ", yard_id='" + yard_id + '\'' +
                ", yard_name='" + yard_name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
