package subastasterreta.subastasterreta.Subastas;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import subastasterreta.subastasterreta.SubastasTerreta;

import java.util.ArrayList;
import java.util.List;

public class Inventarios implements Listener {

    private SubastasTerreta plugin;
    public Inventarios(SubastasTerreta plugin){this.plugin = plugin;}

    Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&',
            "&1&lSubastas LaTerreta"));

                              //SLOT 52// - TUS ITEMS
    ///////////////////////////////////////////////////////////////////////////
    private ItemStack slot52 = new ItemStack(Material.COMPASS, 1);
    private ItemMeta slot52Meta = slot52.getItemMeta();
    private List<String> slot52Lore = new ArrayList<>();
    ///////////////////////////////////////////////////////////////////////////

                             //SLOT 53// - SIGUIENTE PAGINA
    ///////////////////////////////////////////////////////////////////////////
    private ItemStack slot53 = new ItemStack(Material.REDSTONE_TORCH, 1);
    private ItemMeta slot53Meta = slot53.getItemMeta();
    ///////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////
    private ItemStack slot54 = new ItemStack(Material.REDSTONE_TORCH, 1);
    private ItemMeta slot54Meta = slot54.getItemMeta();
    ///////////////////////////////////////////////////////////////////////////

                                //CREAR EL INVENTARIO//
    ///////////////////////////////////////////////////////////////////////////

    public Inventory getMainInventory(String title, boolean isFirstPage){
        inventory = Bukkit.createInventory(null, 54, title);
        slot52Meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Tus Objetos");
        slot52Lore.add(ChatColor.translateAlternateColorCodes('&', "&7Da un clic derecho para abrir"));
        slot52Meta.setLore(slot52Lore);
        ///////////////////////////////
        slot53Meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Próxima Pagina");
        slot54Meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Pagina Anterior");
        ///////////////////////////////

        slot52.setItemMeta(slot52Meta);
        slot53.setItemMeta(slot53Meta);
        slot54.setItemMeta(slot54Meta);

        ///////////////////////////////

        inventory.setItem(52, slot52);
        inventory.setItem(53, slot53);
        //if(!isFirstPage){
            inventory.setItem(45, slot54);
        //}




        return inventory;





    }

    ///////////////////////////////////////////////////////////////////////////





}
