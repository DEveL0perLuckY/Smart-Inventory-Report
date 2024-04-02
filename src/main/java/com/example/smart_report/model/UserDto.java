package com.example.smart_report.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {

    private Integer userId;
    private String userName;
    private String email;
    private String password;
    private List<Integer> roleId;
    private Integer roleIdCount;
}
