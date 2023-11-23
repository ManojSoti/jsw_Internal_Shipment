package com.company.jsw_internal_shipment;

public class UnitNameBean {
    String unitNAme;
String yard_name;

    public String getYard_name() {
        return yard_name;
    }

    public void setYard_name(String yard_name) {
        this.yard_name = yard_name;
    }

    public String getUnitNAme() {
        return unitNAme;
    }

    public void setUnitNAme(String unitNAme) {
        this.unitNAme = unitNAme;
    }

    @Override
    public String toString() {
        return "UnitNameBean{" +
                "unitNAme='" + unitNAme + '\'' +
                ", yard_name='" + yard_name + '\'' +
                '}';
    }
}
