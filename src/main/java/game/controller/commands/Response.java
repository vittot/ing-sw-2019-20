package game.controller.commands;

import java.io.Serializable;

public interface Response extends Serializable {
    void handle(ResponseHandler handler);
}
