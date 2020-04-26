package subastasterreta.subastasterreta.Utiles;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import subastasterreta.subastasterreta.SubastasTerreta;

import java.util.ArrayList;
import java.util.List;

public class Monedas {
    private ItemStack Moneda = new ItemStack(Material.GOLD_NUGGET);
    private ItemMeta MonedaMeta = Moneda.getItemMeta();

    /*
    Al usar este metodo, se podra obtener una moneda de x1
     */

    public ItemStack obtenerMoneda(int cantidad){

        List<String> lore = new ArrayList<>();

    MonedaMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&oMoneda Laterreta"));
        MonedaMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        lore.add(ChatColor.translateAlternateColorCodes('&', "&5&oPuedes usarlo en /warp tienda"));
        MonedaMeta.setLore(lore);
        Moneda.setItemMeta(MonedaMeta);
        Moneda.setAmount(cantidad);

    return Moneda;
    }

    public ItemStack obtenerMonedaLingote(int cantidad){

        ItemStack monedaLingote = new ItemStack(Material.GOLD_INGOT);
        ItemMeta monedaLingoteMeta = monedaLingote.getItemMeta();
        List<String> lore = new ArrayList<>();

        assert monedaLingoteMeta != null;
        monedaLingoteMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.ITALIC + "Monedax9 Laterreta");
        monedaLingoteMeta.addEnchant(Enchantment.DURABILITY, 2, true);
        lore.add(ChatColor.translateAlternateColorCodes('&', "&5&oPuedes usarlo en /warp tienda"));
        monedaLingoteMeta.setLore(lore);
        monedaLingote.setAmount(cantidad);
        monedaLingote.setItemMeta(monedaLingoteMeta);

        return monedaLingote;
    }
    public ItemStack obtenerMonedaBloque(int cantidad){

        ItemStack monedaLingote = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta monedaLingoteMeta = monedaLingote.getItemMeta();
        List<String> lore = new ArrayList<>();

        assert monedaLingoteMeta != null;
        monedaLingoteMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.ITALIC + "Monedax9x9 Laterreta");
        monedaLingoteMeta.addEnchant(Enchantment.DURABILITY, 3, true);
        lore.add(ChatColor.translateAlternateColorCodes('&', "&5&oPuedes usarlo en /warp tienda"));
        monedaLingoteMeta.setLore(lore);
        monedaLingote.setAmount(cantidad);
        monedaLingote.setItemMeta(monedaLingoteMeta);

        return monedaLingote;

    }





}
