package com.zh.client.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Data
@TableName("user")
public class UserEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String password;
    private String phone;
    private String scopes;

    public UserEntity() {
    }

    public UserEntity(Integer id, String name, String password, String phone, String scopes) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.scopes = scopes;
    }

    public static void main(String[] args) {
        //对密码进行加密   自带加密 BCrypt             盐，不用存储
        String hashpw = BCrypt.hashpw("123", BCrypt.gensalt());
        System.out.println(hashpw);

        //校验密码
        boolean checkpw = BCrypt.checkpw("123", "$2a$10$DAn0zhmSWWr5OB9S0E2lNOkzL6HGi2dkr2VMA7Ib6NHulKkoqLoSm");
        System.out.println(checkpw);
    }
}
