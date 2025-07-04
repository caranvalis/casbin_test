package com.casbin.casbin_test.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("casbin_rule")
public class CasbinRule {

    @Id
    private Long id;
    private String ptype;
    private String v0;
    private String v1;
    private String v2;
    private String v3;
    private String v4;
    private String v5;

    // Getters & Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getPtype() { return ptype; }

    public void setPtype(String ptype) { this.ptype = ptype; }

    public String getV0() { return v0; }

    public void setV0(String v0) { this.v0 = v0; }

    public String getV1() { return v1; }

    public void setV1(String v1) { this.v1 = v1; }

    public String getV2() { return v2; }

    public void setV2(String v2) { this.v2 = v2; }

    public String getV3() { return v3; }

    public void setV3(String v3) { this.v3 = v3; }

    public String getV4() { return v4; }

    public void setV4(String v4) { this.v4 = v4; }

    public String getV5() { return v5; }

    public void setV5(String v5) { this.v5 = v5; }

    public String getField(int index) {
        switch (index) {
            case 0:
                return v0;
            case 1:
                return v1;
            case 2:
                return v2;
            case 3:
                return v3;
            case 4:
                return v4;
            case 5:
                return v5;
            default:
                return null;
        }
    }

    public void setField(int index, String value) {
        switch (index) {
            case 0:
                this.v0 = value;
                break;
            case 1:
                this.v1 = value;
                break;
            case 2:
                this.v2 = value;
                break;
            case 3:
                this.v3 = value;
                break;
            case 4:
                this.v4 = value;
                break;
            case 5:
                this.v5 = value;
                break;
            default:
                // ignore
        }
    }

}
