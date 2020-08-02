package com.chris.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {

    private String id;
    private String path;
    private String name;
    private String suffix;
    private Integer size;
    private String use;

    private Integer shardIndex;
    private Integer shardSize;
    private Integer shardTotal;
    private String key;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;

    private String shard;
    private String vod;

}
