package me.virtualbyte.plugins.dispenserfill.commands;

import me.virtualbyte.byteutils.ByteCommand;
import me.virtualbyte.byteutils.ByteSubCommand;
import me.virtualbyte.byteutils.utils.Messages;
import me.virtualbyte.plugins.dispenserfill.DispenserFill;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * DispenserFill - Developed by VirtualByte (Lewes D. B.)
 */
public class FillDispensersCommand extends ByteCommand {

    public FillDispensersCommand(String name) {
        super(name);
    }

    @Override
    public String getName() {
        return "filldispensers";
    }

    @Override
    public String getPermission() {
        return "dispenserfill.fill";
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
        return true;
    }

    @Override
    public ByteSubCommand[] getSubCommands() {
        return new ByteSubCommand[0];
    }

    @Override
    public void run(CommandSender commandSender, String label, String[] args) {
        int radius = DispenserFill.getInstance().getConfig().getInt("defaultRange", 10);

        if (args.length >= 1) {
            if (commandSender.hasPermission("dispenserfill.custom")) {
                if (isAllowedNumber(args[0])) {
                    radius = Integer.valueOf(args[0]);
                } else {
                    Messages.sendMessage(commandSender, DispenserFill.getInstance().getMessages().getMessage("invalidNumber"));
                    return;
                }
            } else {
                Messages.sendMessage(commandSender, DispenserFill.getInstance().getMessages().getMessage("noPermission"));
                return;
            }
        }

        if (radius > DispenserFill.getInstance().getConfig().getInt("maximumRange")) {
            if (!commandSender.hasPermission("dispenserfill.bypass")) {
                Messages.sendMessage(commandSender, DispenserFill.getInstance().getMessages().getMessage("maximumRange"));
                return;
            }
        }

        Player player = (Player) commandSender;

        if (player.getItemInHand() == null ||
                player.getItemInHand().getType() == Material.AIR) {
            Messages.sendMessage(player, DispenserFill.getInstance().getMessages().getMessage("noItemInHand"));
            return;
        }

        List<Location> dispensers = getDispenserLocationFromRange(radius, ((Player) commandSender).getLocation());

        if (dispensers.isEmpty()) {
            Messages.sendMessage(commandSender, DispenserFill.getInstance().getMessages().getMessage("noDispensersFound"));
            return;
        }

        int filled = 0;

        loop:
        for (int x = player.getItemInHand().getAmount(); x > 0; ) {
            for (Location location : dispensers) {
                if (location.getBlock().getState() instanceof Dispenser) {
                    Dispenser dispenser = (Dispenser) location.getBlock().getState();

                    if (dispenser.getInventory().addItem(new ItemStack(player.getItemInHand().getType(), 1)).isEmpty()) {
                        x--;
                        dispenser.update(true, true);
                        player.getItemInHand().setAmount(x);

                        if (x == 0) {
                            player.getItemInHand().setType(Material.AIR);
                            player.setItemInHand(null);
                            player.updateInventory();
                        }
                    } else {
                        filled++;

                        if (filled == dispensers.size()) {
                            break loop;
                        }
                    }
                }
            }
        }

        Messages.sendMessage(player, DispenserFill.getInstance().getMessages().getMessage("dispensersFilled").replace("{range}", String.valueOf(radius)).replace("{amount}", String.valueOf(dispensers.size())));
    }

    private List<Location> getDispenserLocationFromRange(int radius, Location location) {
        List<Location> locations = new ArrayList<Location>();

        for (int x = (radius * -1); x <= radius; x++) {
            for (int y = (radius * -1); y <= radius; y++) {
                for (int z = (radius * -1); z <= radius; z++) {
                    Block block = location.getWorld().getBlockAt(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z);

                    if (block.getType() == Material.DISPENSER) {
                        if (block.getState() instanceof Dispenser) {
                            locations.add(block.getLocation());
                        }
                    }
                }
            }
        }

        return locations;
    }

    private boolean isAllowedNumber(String radius) {
        try {
            Integer.valueOf(radius);
        } catch (NumberFormatException exception) {
            return false;
        }

        if (Integer.valueOf(radius) < 1) {
            return false;
        }

        return true;
    }

}