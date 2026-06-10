package common.commands;

import java.io.Serializable;

public class Register implements Command, Serializable {

    @Override
    public String getName() {
        return "register";
    }

}