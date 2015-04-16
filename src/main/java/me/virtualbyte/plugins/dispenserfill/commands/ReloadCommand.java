package me.virtualbyte.plugins.dispenserfill.commands;

import me.virtualbyte.byteutils.ByteCommand;
import me.virtualbyte.byteutils.ByteSubCommand;
import me.virtualbyte.byteutils.utils.Messages;
import me.virtualbyte.plugins.dispenserfill.DispenserFill;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * DispenserFill - Developed by VirtualByte (Lewes D. B.)
 */
public class ReloadCommand extends ByteCommand {

    public ReloadCommand(String name) {
        super(name);
    }

    @Override
    public String getName() {
        return "dfreload";
    }

    @Override
    public String getPermission() {
        return "dispenserfill.reload";
    }

    @Override
    public String getPermissionMessage() {
        return DispenserFill.getInstance().getMessages().getMessage("noPermission");
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public String getNotEnoughArgsMessage() {
        return null;
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public ByteSubCommand[] getSubCommands() {
        return new ByteSubCommand[0];
    }

    @Override
    public void run(CommandSender commandSender, String s, String[] strings) {
        DispenserFill.getInstance().reloadMessages();
        DispenserFill.getInstance().reloadConfig();

        Messages.sendMessage(commandSender, DispenserFill.getInstance().getMessages().getMessage("reloaded"));
    }
}
