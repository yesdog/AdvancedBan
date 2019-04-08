package me.leoko.advancedban.commands;

import me.leoko.advancedban.manager.MessageManager;
import me.leoko.advancedban.punishment.Punishment;
import me.leoko.advancedban.punishment.PunishmentManager;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class RevokePunishmentIDCommand implements Consumer<Command.CommandInput> {
    private String path;
    private Function<Integer, Optional<Punishment>> resolver;

    public RevokePunishmentIDCommand(String path, Function<Integer, Optional<Punishment>> resolver) {
        this.path = path;
        this.resolver = resolver;
    }

    @Override
    public void accept(Command.CommandInput input) {
        int id = Integer.parseInt(input.getPrimary());

        Optional<Punishment> punishment = resolver.apply(id);
        if (!punishment.isPresent()) {
            input.getSender().sendCustomMessage("Un" + path + ".NotPunished",
                    true, "ID", id + "");
            return;
        }

        final String operator = input.getSender().getName();
        //TODO broadcast
        PunishmentManager.getInstance().deletePunishment(punishment.get());

        input.getSender().sendCustomMessage("Un" + path + ".Done",
                true, "ID", id + "");
    }
}
