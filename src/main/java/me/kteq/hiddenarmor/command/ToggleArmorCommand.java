package me.kteq.hiddenarmor.command;

import me.kteq.hiddenarmor.HiddenArmor;
import me.kteq.hiddenarmor.armormanager.ArmorManager;
import me.kteq.hiddenarmor.util.CommandUtil;
import me.kteq.hiddenarmor.util.StrUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleArmorCommand {
    HiddenArmor plugin;
    ArmorManager armorManager;

    public ToggleArmorCommand(HiddenArmor pl, ArmorManager am){
        armorManager = am;
        plugin = pl;
        new CommandUtil(plugin,"togglearmor", 0,1, true, plugin.isToggleDefault()){

            @Override
            public void sendUsage(CommandSender sender) {
                if(sender instanceof Player)
                    sender.sendMessage(plugin.getPrefix() + StrUtil.color("&2Correct use:&f /togglearmor " + (canUseArg(sender, "other") ? "[player]" : "")));
                else
                    sender.sendMessage(plugin.getPrefix() + StrUtil.color("&2Correct use:&f /togglearmor <player>"));
            }

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments){
                Player player;
                if(arguments.length==1){
                    if(!canUseArg(sender, "other") && !plugin.isToggleOtherDefault()) return false;
                    String playerName = arguments[0];
                    player = Bukkit.getPlayer(playerName);

                    if(player==null){
                        sender.sendMessage(plugin.getPrefix() + "Jogador não encontrado.");
                        return true;
                    }
                }else {
                    player = (Player) sender;
                }

                String visibility;

                if(plugin.hasPlayer(player)){
                    plugin.removeHiddenPlayer(player);
                    visibility = StrUtil.color("&bON");
                }else {
                    plugin.addHiddenPlayer(player);
                    visibility =  StrUtil.color("&7OFF") ;
                }

                if(!player.equals((Player) sender)) sender.sendMessage(player.getName() + "' agora tem sua armadura': " + visibility);

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Visibilidade da armadura: " + visibility));

                armorManager.updatePlayer(player);

                return true;
            }
        }.setCPermission("toggle").setUsage("/togglearmor").setDescription("Ligar invisibilidade da armadura.");
    }
}
