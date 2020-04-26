package subastasterreta.subastasterreta.Listeners;

import com.sun.org.apache.xpath.internal.objects.XObject;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;
import subastasterreta.subastasterreta.Subastas.Inventarios;
import subastasterreta.subastasterreta.SubastasTerreta;
import subastasterreta.subastasterreta.Utiles.Monedas;
import subastasterreta.subastasterreta.Utiles.UtilesPrincipales;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

public class OnMainInventory implements Listener {
    private SubastasTerreta plugin;
    private org.bukkit.entity.Player Player;
    private ItemStack slot53 = new ItemStack(Material.REDSTONE_TORCH, 1);
    private ItemMeta slot53Meta = slot53.getItemMeta();
    private ItemStack slot54 = new ItemStack(Material.REDSTONE_TORCH, 1);
    private ItemMeta slot54Meta = slot54.getItemMeta();


    public OnMainInventory(SubastasTerreta plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void OnMainInventoryClick(InventoryClickEvent event) throws InvalidConfigurationException, IOException {
        Monedas monedas = new Monedas();


        UtilesPrincipales utilesPrincipales = new UtilesPrincipales();
        for (Map.Entry<Integer, Inventory> entry : plugin.inventoriesHashMap.entrySet()) {
            slot53Meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Próxima Pagina");
            slot53.setItemMeta(slot53Meta);
            if (Objects.requireNonNull(event.getClickedInventory()).contains(slot53)) {


                if (event.isShiftClick() || event.isRightClick() || event.getClick().isKeyboardClick() || event.getClick().isCreativeAction()) {
                    event.setCancelled(true);

                } else if (event.isLeftClick()) {
                    event.setCancelled(true);

                    /////////////////////////////////////////////////////////////
                    /*
                    Check if the following inventory is an instance of the first
                    page to avoid going back to a page with no existance.
                     */
                    //if (entry.getKey().equals(1)) {
                    if (Objects.equals(event.getCurrentItem(), slot53)) {
                        Player player = (Player) event.getWhoClicked();
                        if (plugin.inventoriesHashMap.containsKey(entry.getKey() + 1)) {
                            event.getWhoClicked().closeInventory();
                            player.openInventory(plugin.inventoriesHashMap.get(entry.getKey() + 1));
                            utilesPrincipales.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1.2F);
                        }


                        // }

                        ///////////////////////////////////////////////////////
                    } else {
                        ItemStack slot52 = new ItemStack(Material.COMPASS, 1);
                        ItemMeta slot52Meta = slot52.getItemMeta();
                        List<String> slot52Lore = new ArrayList<>();
                        assert slot52Meta != null;
                        slot52Meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Tus Objetos");
                        slot52Lore.add(ChatColor.translateAlternateColorCodes('&', "&7Da un clic derecho para abrir"));
                        slot52Meta.setLore(slot52Lore);
                        slot52.setItemMeta(slot52Meta);

                        Player player = (Player) event.getWhoClicked();
                        slot54Meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Pagina Anterior");
                        slot54.setItemMeta(slot54Meta);
                        if (Objects.equals(event.getCurrentItem(), slot54)) {
                            event.getWhoClicked().closeInventory();
                            if (plugin.inventoriesHashMap.containsKey(entry.getKey() - 1)) {
                                event.getWhoClicked().closeInventory();
                                player.openInventory(plugin.inventoriesHashMap.get(entry.getKey() - 1));
                                utilesPrincipales.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1.2F);
                            }
                        } else {

                            if (event.getCurrentItem() == null) {
                                return;
                            }

                            if (event.getCurrentItem().getType().equals(Material.COMPASS)) {
                                if (event.getCurrentItem().getItemMeta() != null) {
                                    if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "" + ChatColor.BOLD + "Tus Objetos")) {
                                        player.closeInventory();
                                        player.openInventory(plugin.getInventory(player));
                                        return;
                                    }
                                }
                            }
                            int pepitas = 0;

                            ItemStack pepiaItem = monedas.obtenerMoneda(1);
                            ItemStack lingoteItem = monedas.obtenerMonedaLingote(1);
                            ItemStack bloqueItem = monedas.obtenerMonedaBloque(1);

                            ItemMeta pepiaItemMeta = pepiaItem.getItemMeta();
                            ItemMeta lingoteItemMeta = lingoteItem.getItemMeta();
                            ItemMeta bloqueItemMeta = bloqueItem.getItemMeta();

                            List<Integer> slotPepita = new ArrayList<>();
                            List<Integer> slotLingote = new ArrayList<>();
                            List<Integer> slotBloque = new ArrayList<>();


                            for (int i = 0; i < 36; i++) {

                                try {
                                    if (player.getInventory().getItem(i) != null) {

                                        if (Objects.equals(Objects.requireNonNull(player.getInventory().getItem(i)).getItemMeta(), pepiaItemMeta)) {
                                            pepitas += Objects.requireNonNull(player.getInventory().getItem(i)).getAmount();
                                            slotPepita.add(i);
                                        }

                                        if (Objects.equals(Objects.requireNonNull(player.getInventory().getItem(i)).getItemMeta(), lingoteItemMeta)) {
                                            pepitas += Objects.requireNonNull(player.getInventory().getItem(i)).getAmount() * 9;
                                            slotLingote.add(i);
                                        }

                                        if (Objects.equals(Objects.requireNonNull(player.getInventory().getItem(i)).getItemMeta(), bloqueItemMeta)) {
                                            {
                                                pepitas += Objects.requireNonNull(player.getInventory().getItem(i)).getAmount() * 81;
                                                slotBloque.add(i);
                                            }
                                        }
                                    }

                                } catch (Exception e) {

                                }
                            }

                            ItemStack itemClicked = event.getCurrentItem();


                            if (Objects.requireNonNull(itemClicked.getItemMeta()).getLore() == null) {
                                return;
                            }
                            List<String> lore = Objects.requireNonNull(itemClicked.getItemMeta()).getLore();
                            if (lore != null) {
                                String itemID = lore.get(lore.size() - 1).replace(ChatColor.DARK_GRAY + "", "");

                                String name = "";
                                int precio = 0;
                                String precioString = "";

                                for (String key : lore) {
                                    if (key.startsWith(ChatColor.translateAlternateColorCodes('&', "&e&l&nVENDEDOR&6:"))) {
                                        name = key.replace(ChatColor.translateAlternateColorCodes('&', "&e&l&nVENDEDOR&6: &7"), "");
                                    } else if (key.startsWith(ChatColor.translateAlternateColorCodes('&', "&e&l&nPRECIO&6"))) {
                                        precioString = (key.replace(ChatColor.translateAlternateColorCodes('&', "&e&l&nPRECIO&6: &7"), ""))
                                                .replace(ChatColor.translateAlternateColorCodes('&', " &7monedas terreta."), "");
                                        precio = Integer.parseInt(precioString);
                                    }

                                }

                                YamlFile playerFile = new YamlFile("plugins/SubastasTerreta/Data/" + name + ".yml");
                                playerFile.load();

                                if (pepitas < precio) {
                                    utilesPrincipales.sendMessage(player, "&cLo sentimos, parece que no tienes suficientes monedas terreta para comprar este objeto.");
                                    utilesPrincipales.playSound(player, Sound.BLOCK_CHEST_CLOSE, 1.2F);
                                    player.closeInventory();
                                    return;
                                }

                                int empty = 0;

                                for (int slot = 0; slot < 36; slot++) {
                                    if (player.getInventory().getItem(slot) == null) {
                                        empty++;
                                    }
                                }

                                if (empty < 3) {
                                    utilesPrincipales.sendMessage(player, "&cParece que no tienes espacio en tu " +
                                            "inventario para comprar este objeto. Asegúrate de tener al menos &l&nTRES&c espacios para tu cambio sin problema.");
                                    utilesPrincipales.playSound(player, Sound.BLOCK_CHEST_CLOSE, 1.2F);
                                    player.closeInventory();
                                    return;
                                }

                                int cambio = pepitas - precio;
                                int bloque = 0;
                                int lingote = 0;
                                int pepita = 0;


                                while (cambio != 0) {
                                    if (cambio - 81 >= 0) {
                                        cambio = cambio - 81;
                                        bloque++;
                                    } else if (cambio - 9 >= 0) {
                                        cambio = cambio - 9;
                                        lingote++;
                                    } else {
                                        pepita = cambio;
                                        cambio = 0;

                                    }
                                }
                                if (!slotPepita.isEmpty()) {
                                    for (int i : slotPepita) {
                                        player.getInventory().setItem(i, null);
                                    }
                                }
                                if (!slotLingote.isEmpty()) {
                                    for (int i : slotLingote) {
                                        player.getInventory().setItem(i, null);
                                    }
                                }
                                if (!slotBloque.isEmpty()) {
                                    for (int i : slotBloque) {
                                        player.getInventory().setItem(i, null);
                                    }
                                }

                                boolean loreEmpty = true;

                                if (bloque > 0) {
                                    player.getInventory().addItem(monedas.obtenerMonedaBloque(bloque));
                                }
                                if (lingote > 0) {
                                    player.getInventory().addItem(monedas.obtenerMonedaLingote(lingote));
                                }
                                if (pepita > 0) {
                                    player.getInventory().addItem(monedas.obtenerMoneda(pepita));
                                }


                                ItemStack itemStack = new ItemStack(Material.valueOf(playerFile.getString("Items." + itemID + ".Type")));


                                if (playerFile.getStringList("Items." + itemID + ".Lore") != null) {
                                    loreEmpty = false;
                                }

                                ItemMeta meta = itemStack.getItemMeta();

                                if (!loreEmpty) {
                                    List<String> lored = new ArrayList<>(playerFile.getStringList("Items." + itemID + ".Lore"));


                                    assert meta != null;
                                    meta.setLore(lored);
                                    meta.setDisplayName(playerFile.getString("Items." + itemID + ".DisplayName"));
                                    itemStack.setItemMeta(meta);
                                }

                                itemStack.setAmount(playerFile.getInt("Items." + itemID + ".Amount"));


                                HashMap<Enchantment, Integer> enchantments = new HashMap<>();
                                List<String> enchs = new ArrayList<>(playerFile.getStringList("Items." + itemID + ".Enchantments"));

                                for (String enchantment : enchs) {
                                    String[] ench = enchantment.split(" ");
                                    Enchantment enchName = Enchantment.getByKey(NamespacedKey.minecraft(ench[0].replace("minecraft:", "")));
                                    int level = Integer.parseInt(ench[1]);
                                    enchantments.put(enchName, level);
                                }
                                itemStack.addUnsafeEnchantments(enchantments);
                                player.getInventory().addItem(setDamageIfAble(playerFile, itemStack, itemID));
                                event.getClickedInventory().setItem(event.getSlot(), plugin.Emptiness);
                                playerFile.set("Monedas", playerFile.getInt("Monedas") + precio);
                                utilesPrincipales.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 0.8F);
                                player.closeInventory();
                                playerFile.set("Items." + itemID, null);
                                playerFile.save();


                            }


                        }
                    }


                }
            }


        }

        Player player = (Player) event.getWhoClicked();


        ItemStack empty = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta metas = empty.getItemMeta();

        assert metas != null;
        metas.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f&l⇫"));
        empty.setItemMeta(metas);


        if (Objects.requireNonNull(event.getClickedInventory()).contains(empty)) {
            event.setCancelled(true);

            int clickedSlot = event.getSlot();
            YamlFile playerFile = new YamlFile("plugins/SubastasTerreta/Data/" + player.getName() + ".yml");
            playerFile.load();

            if (event.getCurrentItem() != null) {
                ItemStack itemStack = event.getCurrentItem();

                if (event.getSlot() == 45 || event.getSlot() == 46 || event.getSlot() == 47 || event.getSlot() == 48 || event.getSlot() == 50 ||
                        event.getSlot() == 51 || event.getSlot() == 52 || event.getSlot() == 53) {
                    return;
                }
                if (event.getSlot() == 49) {
                    event.setCancelled(true);
                    int cambios = playerFile.getInt("Monedas");
                    int cambio = cambios;
                    int bloque = 0;
                    int lingote = 0;
                    int pepita = 0;



                    while (!(cambio <= 0)) {
                        if (cambio - 81 > 0) {
                            cambio = cambio - 81;
                            bloque++;
                        } else if (cambio - 9 > 0) {
                            cambio = cambio - 9;
                            lingote++;

                        } else {
                            pepita = cambio;
                            cambio = 0;
                        }
                    }


                    Inventory instance = Bukkit.createInventory(null, 54, ChatColor.BLACK + "MONEDAS DE " + player.getName());


                    if (bloque != 0) {
                        instance.addItem(monedas.obtenerMonedaBloque(bloque));
                    }
                    if (lingote != 0) {
                        instance.addItem(monedas.obtenerMonedaLingote(lingote));
                    }
                    if (pepita != 0) {
                        instance.addItem(monedas.obtenerMoneda(pepita));
                    }

                    int stacks = 0;
                    int emptiness = 0;

                    for (ItemStack itemStack1 : instance) {
                        if (itemStack1 != null) {
                            stacks++;
                        }
                    }

                    for (int i = 0; i < 36; i++) {

                        if (player.getInventory().getItem(i) == null) {
                            emptiness++;
                        }

                    }

                    if (stacks > emptiness) {
                        utilesPrincipales.sendMessage(player, "&cParece que no tienes suficiente espacio en tu inventario para" +
                                " reclamar tus monedas. Slots abiertos necesarios: &d" + stacks);
                        utilesPrincipales.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.1F);
                        player.closeInventory();
                        return;
                    }
                    if (stacks == 0) {

                        utilesPrincipales.sendMessage(player, "&cParece que no tienes nada que reclamar.");
                        utilesPrincipales.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.1F);
                        player.closeInventory();
                        return;

                    } else {
                        if (bloque != 0) {
                            player.getInventory().addItem(monedas.obtenerMonedaBloque(bloque));
                        }
                        if (lingote != 0) {
                            player.getInventory().addItem(monedas.obtenerMonedaLingote(lingote));
                        }
                        if (pepita != 0) {
                            player.getInventory().addItem(monedas.obtenerMoneda(pepita));
                        }

                        utilesPrincipales.sendMessage(player, "&aCon exito reclamastes &e" + cambios + " &amonedas terreta");
                        utilesPrincipales.playSound(player, Sound.ENTITY_VILLAGER_CELEBRATE, 1.1F);
                        utilesPrincipales.playSound(player, Sound.ENTITY_VILLAGER_CELEBRATE, 1.1F);
                        utilesPrincipales.playSound(player, Sound.ENTITY_VILLAGER_CELEBRATE, 1.1F);
                        utilesPrincipales.playSound(player, Sound.ENTITY_VILLAGER_CELEBRATE, 1.1F);
                        player.closeInventory();
                        playerFile.set("Monedas", 0);
                        playerFile.save();
                    }


                    return;
                }


                assert itemStack != null;

                String itemID = Objects.requireNonNull(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getLore()).get(Objects.requireNonNull(event.getCurrentItem().getItemMeta().getLore()).size() - 1);

                ItemStack given = event.getCurrentItem();

                ItemMeta givenMeta = given.getItemMeta();
                List<String> lore = givenMeta.getLore();
                lore.remove(lore.size() - 1);
                givenMeta.setLore(lore);
                given.setItemMeta(givenMeta);


                player.getInventory().addItem(given);
                playerFile.set("Items." + itemID, null);
                player.closeInventory();
                playerFile.save();
                player.openInventory(plugin.getInventory(player));
                utilesPrincipales.playSound(player, Sound.BLOCK_BELL_RESONATE, 0.7F);


            }


        }
//THis new keyboard works really good. If you are reading this, you just found out that I have purchased a new keyboard to finish this plugin.

    }

    private ItemStack setDamageIfAble(YamlFile file, ItemStack itemStack, String key) {

        if (file.getConfigurationSection("Items." + key).contains("Damage")) {


            //System.out.println("Works");
            short damage = (short) file.getInt("Items." + key + ".Damage");
            ItemMeta meta = itemStack.getItemMeta();
            assert meta != null;
            ((Damageable) meta).setDamage(damage);
            //System.out.println(damage);
            itemStack.setItemMeta(meta);

        }

        return itemStack;
    }


    @EventHandler
    public void onPlaceEvent(BlockPlaceEvent event) {
        Monedas monedas = new Monedas();
        if (Objects.equals(event.getItemInHand().getItemMeta(), monedas.obtenerMonedaBloque(1).getItemMeta())) {
            event.setCancelled(true);
        }
    }

    private void doNothing() {

    }


}
