package entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Class UserInfo
 * @Description: TODO
 * @Author: luozhen
 * @Create: 2018/09/27 14:47
 */
@Data
public class UserInfo implements Serializable {

    private Integer userId;

    private String userName;

    private Integer userAge;

    private String userPhoneNumber;

}
