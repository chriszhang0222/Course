package com.chris.enums;

import lombok.Data;
import lombok.Getter;

@Getter
public enum FileUse {
    COURSE("C", "course"),
    TEACHER("T", "teacher");

    FileUse(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;

    public static FileUse getByCode(String code){
        for(FileUse e:FileUse.values()){
            if(e.getCode().equals(code))
                return e;
        }
        return null;
    }


}
