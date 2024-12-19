package sn.groupeisi.us.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityAlreadyExistsException extends RuntimeException {
    String message;
}
