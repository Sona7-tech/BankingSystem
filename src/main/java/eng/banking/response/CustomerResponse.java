package eng.banking.response;

import eng.banking.entity.Authority;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerResponse {

    String name;
    String surname;
    String address;
    String email;
    String contact;
    Set<Authority> roles = new HashSet<>();

}
