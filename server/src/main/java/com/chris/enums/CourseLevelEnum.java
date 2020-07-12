package com.chris.enums;

public enum CourseLevelEnum {
    One("1", "Junior Level"),
    Two("2", "Medium Level"),
    THREE("3", "Advanced Level");

    private String code;
    private String desc;

    CourseLevelEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
