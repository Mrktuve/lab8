package common.commands;

import java.io.Serializable;

public class Login implements Command, Serializable {

    @Override
    public String getName() {
        return "login";
    }

}
