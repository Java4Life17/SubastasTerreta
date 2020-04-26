package subastasterreta.subastasterreta.Utiles;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UtilesPrincipales {

    public void sendMessage(Player usuario, String mensaje) {
        usuario.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje));
    }

    public void playSound(Player usuario, Sound sonido, Float pitch) {
        usuario.playSound(usuario.getLocation(), sonido, 100, pitch);
    }

    public void ConvertirItemAYaml(ItemStack item, Player player, int precio) throws IOException {
        UtilesPrincipales utiles = new UtilesPrincipales();

        YamlFile playerData = new YamlFile("plugins/SubastasTerreta/Data/" + player.getName() + ".yml");

        Damageable itemMeta = (Damageable) item.getItemMeta();
        assert itemMeta != null;
        item.setItemMeta((ItemMeta) itemMeta);

        try {
            if (!playerData.exists()) {
                utiles.sendMessage(player, "&a Foosh! ¡No existía un archivo para sus datos, pero hemos creado uno nuevo para usted!");
                utiles.playSound(player, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0F);
                playerData.createNewFile(true);
                playerData.set("Monedas", 0);
                playerData.createSection("Items");
            } else {
                playerData.load();
            }

            String itemKey = "";

            boolean existe = true;
            while (existe) {
                String name = "item" + getAlphaNumericString(15);
                if (playerData.getConfigurationSection("Items").contains(name)) {
                    existe = true;
                } else {
                    existe = false;
                    itemKey = name;
                }


            }

            playerData.getConfigurationSection("Items").createSection(itemKey);
            playerData.set("Items." + itemKey + ".Type", item.getType().name());
            playerData.set("Items." + itemKey + ".Amount", item.getAmount());
            playerData.set("Items." + itemKey + ".Damage", (item.getDurability()));
            playerData.set("Items." + itemKey + ".TimeLeft", 2880);
            playerData.set("Items." + itemKey + ".DisplayName", item.getItemMeta().getDisplayName());

            if (item.getItemMeta().hasLore()) {
                List<String> lore = new ArrayList<>(Objects.requireNonNull(item.getItemMeta().getLore()));
                playerData.set("Items." + itemKey + ".Lore", lore);
            }


            if (!item.getEnchantments().isEmpty()) {
                List<String> enchantments = new ArrayList<>();
                for (Enchantment enchantment : item.getEnchantments().keySet()) {
                    String enchantmentToSave = enchantment.getKey() + " " + item.getItemMeta().getEnchantLevel(enchantment);
                    enchantments.add(enchantmentToSave);
                }
                playerData.set("Items." + itemKey + ".Enchantments", enchantments);
            }



            playerData.set("Items." + itemKey + ".Price", precio);
            utiles.sendMessage(player, "&eHas añadido correctamente ese artículo a las subastas por un precio de " + precio +
                    " monedas terreta");
            utiles.playSound(player, Sound.ENTITY_CHICKEN_EGG, 1.4F);
            removeItem(player);






        } catch (Exception e) {
            utiles.sendMessage(player, "&cSe produjo un error al cargar / crear su archivo de datos en la base de datos. Informe el siguiente error al propietario / administrador del servidor &e&l-> " +
                    "&bSubastasTerreta->UtilesPrincipales->ConvertirItemAYaml");
            utiles.playSound(player, Sound.ENTITY_CAT_DEATH, 0.8F);
            System.out.println(ChatColor.translateAlternateColorCodes('&', "&e=================" +
                    "=================="));
            e.printStackTrace();


            System.out.println(ChatColor.translateAlternateColorCodes('&', "&e=================" +
                    "=================="));
        }

        playerData.save();
    }

    private void removeItem(Player player) {
        int slot = player.getInventory().getHeldItemSlot();
        player.getInventory().setItem(slot, null);
    }

    static String getAlphaNumericString(int n) {


        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";


        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }

}




