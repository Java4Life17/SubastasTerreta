package subastasterreta.subastasterreta.Comandos;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.simpleyaml.configuration.file.YamlFile;
import subastasterreta.subastasterreta.Subastas.Inventarios;
import subastasterreta.subastasterreta.SubastasTerreta;
import subastasterreta.subastasterreta.Utiles.UtilesPrincipales;

import java.io.File;
import java.util.Map;
import java.util.Objects;

public class Subastas implements CommandExecutor {

    private SubastasTerreta plugin;

    public Subastas(SubastasTerreta plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player usuario = (Player) sender;

        Inventarios inventarios = new Inventarios(plugin);
        UtilesPrincipales utiles = new UtilesPrincipales();

        if (command.getName().equalsIgnoreCase("subastas") && args.length <= 0) {
            try {


                utiles.sendMessage(usuario, "&eAbriendo el menu de subastas!");
                usuario.openInventory(plugin.inventoriesHashMap.get(1));
                utiles.playSound((Player) sender, Sound.BLOCK_CHEST_OPEN, 1.0F);



            } catch (Exception e) {
                e.printStackTrace();
                utiles.sendMessage(usuario, "&c" + e.toString());
            }
        } else if (args[0].equalsIgnoreCase("item")) {
            try {
                Player player = (Player) sender;

                int precio = Integer.parseInt(args[1]);

                for(String item: plugin.getConfiguracion().getStringList("Objetos")){
                    if (player.getInventory().getItemInMainHand().getType().name().equals(item)){
                        utiles.sendMessage(player, "&cEste objeto no esta permitido en las subastas!");
                        utiles.playSound(player, Sound.ENTITY_ARROW_HIT, 0.8F);
                        return false;
                    }
                }
                for(String palabra: plugin.getConfiguracion().getStringList("Palabras")){
                    if (player.getInventory().getItemInMainHand().getItemMeta() != null){
                        if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(palabra)){
                            utiles.sendMessage(player, "&cEl objeto que estas vendiendo no puede tener la palabra " +
                                    "&l" + palabra + "&c!");
                            utiles.playSound(player, Sound.ENTITY_ARROW_HIT, 0.8F);
                            return false;
                        }

                    }
                }


                if (plugin.getConfiguracion().getBoolean("broadcast")) {
                    String message = plugin.getConfiguracion().getString("Mensaje");
                    message = message.replace("%player%", player.getName());
                    message = message.replace("%objeto%", player.getInventory().getItemInMainHand().getType().name());
                    message = message.replace("%cantidad%", Integer.toString(player.getInventory().getItemInMainHand().getAmount()));
                    message = message.replace("%precio%", Integer.toString(precio));
                    for (Player player1 : plugin.getServer().getOnlinePlayers()) {
                        utiles.sendMessage(player1, message);
                        utiles.playSound(player1, Sound.EVENT_RAID_HORN, 1.5F);
                    }
                }
                utiles.ConvertirItemAYaml(Objects.requireNonNull(usuario.getInventory().getItem(usuario.getInventory().getHeldItemSlot())), usuario, precio);

                plugin.loadItemsForMenu();


                return true;

            } catch (Exception e) {
                if (args.length < 2) {
                    utiles.playSound(usuario, Sound.ENTITY_VILLAGER_NO, 1.5F);
                    utiles.sendMessage(usuario, "&colvidastes poner un precion a ese objeto. Intenta &7/subastas <subastar> <precio>");
                } else {
                    e.printStackTrace();
                    utiles.playSound(usuario, Sound.ENTITY_VILLAGER_NO, 0.9F);
                    utiles.sendMessage(usuario, "&cUso del comando /subastas item <precio>");
                }
            }
        }


        return false;
    }
}
