package models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Courier {
    private String login;
    private String password;
    private String firstName;
}