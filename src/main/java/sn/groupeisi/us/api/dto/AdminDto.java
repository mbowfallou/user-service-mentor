package sn.groupeisi.us.api.dto;

import lombok.Data;

@Data
public class AdminDto extends UserDto{
    private Long id;
    private String permissions;
}
