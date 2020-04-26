package subastasterreta.subastasterreta;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;
import subastasterreta.subastasterreta.Comandos.Subastas;
import subastasterreta.subastasterreta.Comandos.obtenerMonedas;
import subastasterreta.subastasterreta.Listeners.OnMainInventory;
import subastasterreta.subastasterreta.Subastas.Inventarios;
import subastasterreta.subastasterreta.Timer.TimeKeeper;
import subastasterreta.subastasterreta.Utiles.Monedas;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class SubastasTerreta extends JavaPlugin {

    private YamlFile Configuracion;
    private YamlFile Objetos;
    List<ItemStack> items = new ArrayList<>();
    public HashMap<Integer, Inventory> inventoriesHashMap = new HashMap<>();
    public ItemStack Emptiness = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);


    @Override
    public void onEnable() {

        Configuracion = new YamlFile("plugins/SubastasTerreta/Configuracion.yml");
        Objetos = new YamlFile("plugins/SubastasTerreta/Objetos.yml");
        loadEmptySlotItem();
        loadFiles();
        Objects.requireNonNull(getCommand("AdminSubastas")).setExecutor(new obtenerMonedas());
        Objects.requireNonNull(getCommand("Subastas")).setExecutor(new Subastas(this));
        getServer().getPluginManager().registerEvents(new OnMainInventory(this), this);
        TimeKeeper timeKeeper = new TimeKeeper(this);
        try {
            timeKeeper.startTimer();
        }catch (Exception e){
            e.printStackTrace();
        }


        try {
            loadItemsForMenu();

        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }

        loadInventories();


    }

    private void loadEmptySlotItem() {
        ItemMeta meta = Emptiness.getItemMeta();
        assert meta != null;
        meta.setDisplayName(" ");
        Emptiness.setItemMeta(meta);
    }

    public void loadInventories() {
        int pageTrack = 1;
        int slotTrack = 0;
        boolean isFirstPage = true;


        Inventarios invs = new Inventarios(this);
        Inventory inventory = invs.getMainInventory(ChatColor.translateAlternateColorCodes('&', "&eSubastas Terreta (" + pageTrack + ")"), isFirstPage);
        for (ItemStack item : items) {
            if (slotTrack != 52) {
                if (slotTrack == 45) {
                    slotTrack++;
                }
                inventory.setItem(slotTrack, item);
                slotTrack++;


            } else {
                inventoriesHashMap.put(pageTrack, inventory);
                slotTrack = 0;
                isFirstPage = false;

                pageTrack++;
                inventory = invs.getMainInventory(ChatColor.YELLOW + "Subastas Terreta (" + pageTrack + ")", isFirstPage);
            }
        }
        inventoriesHashMap.put(pageTrack, inventory);


    }


    public void loadItemsForMenu() throws InvalidConfigurationException, IOException {
        items.clear();
        for (File yamlFile : Objects.requireNonNull(new File(getDataFolder().getAbsolutePath() + File.separator + "Data").listFiles())) {

            YamlFile archivo = new YamlFile("plugins/SubastasTerreta/Data/" + yamlFile.getName());
            archivo.load();
            loadItems(archivo, yamlFile);

        }

    }

    private void loadItems(YamlFile file, File yamlFile) {
        for (String key : file.getConfigurationSection("Items").getKeys(false)) {
            if (file.getInt("Items." + key + ".TimeLeft") >= 1) {

                ItemStack itemStack = new ItemStack(Material.valueOf(file.getString("Items." + key + ".Type")));


                List<String> lore = new ArrayList<>();

                lore.add(ChatColor.translateAlternateColorCodes('&', "&d&l﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎"));
                lore.add(ChatColor.translateAlternateColorCodes('&', ""));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&e&l&nVENDEDOR&6: &7" + yamlFile.getName().replace(".yml", "")));
                lore.add(ChatColor.translateAlternateColorCodes('&', ""));
                for (String loreString : file.getStringList("Items." + key + ".Lore")) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', loreString));
                }
                lore.add(ChatColor.translateAlternateColorCodes('&', ""));

                lore.add(ChatColor.translateAlternateColorCodes('&', "&e&l&nPRECIO&6: &7" + file.getInt(
                        "Items." + key + ".Price"
                ) + " &7monedas terreta."));
                lore.add(ChatColor.translateAlternateColorCodes('&', ""));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&d&l﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎﹎"));
                lore.add("");
                lore.add(ChatColor.DARK_GRAY + "" + key);
                ItemMeta meta = itemStack.getItemMeta();
                assert meta != null;
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                itemStack.setAmount(file.getInt("Items." + key + ".Amount"));


                HashMap<Enchantment, Integer> enchantments = new HashMap<>();
                List<String> enchs = new ArrayList<>(file.getStringList("Items." + key + ".Enchantments"));


                for (String enchantment : enchs) {


                    String[] ench = enchantment.split(" ");
                    if (enchantment.startsWith("minecraft:unbreaking")) {
                        Enchantment name = Enchantment.DURABILITY;
                        int level = Integer.parseInt(ench[1]);
                        enchantments.put(name, level);


                    } else {
                        Enchantment name = Enchantment.getByKey(NamespacedKey.minecraft(ench[0].replace("minecraft:", "")));
                        int level = Integer.parseInt(ench[1]);
                        enchantments.put(name, level);
                    }
                    System.out.println(enchantment);

                }

                if (!enchantments.isEmpty()) {
                    itemStack.addUnsafeEnchantments(enchantments);
                }
                items.add(setDamageIfAble(file, itemStack, key));
            }

        }


        loadInventories();

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


    private void loadFiles() {
        /*
        Load every file and make sure there is no error, if there is
        output the stack trace to trace it.
         */
        try {

            int exito = 0;


            if (!Configuracion.exists()) {
                saveResource("Configuracion.yml", true);

                System.out.println(ChatColor.translateAlternateColorCodes('&', "&aCon exito se genero" +
                        " el archivo Configuracion.yml"));
                exito++;

            }
            Configuracion.load();
            Configuracion.saveWithComments();

            if (!Objetos.exists()) {
                saveResource("Objetos.yml", true);

                System.out.println(ChatColor.translateAlternateColorCodes('&', "&aCon exito se genero" +
                        " el archivo Objetos.yml"));
                exito++;

            }
            Objetos.load();
            Objetos.saveWithComments();
            if (exito == 2) {
                System.out.println(ChatColor.translateAlternateColorCodes('&', "&aSe han generado todos " +
                        "los archivos con un exito de 100%"));
            }


        } catch (Exception e) {
            System.out.println(ChatColor.translateAlternateColorCodes('&', "&e=================" +
                    "=================="));


            System.out.println(ChatColor.translateAlternateColorCodes('&', "&aSE PRESENTO UN ERROR" +
                    "AL CARGAR UN ARCHIVO EN EL PLUGIN SubastasTerreta. FABOR DE REPORTAR ESTE ERROR AL DEVELOPER ->"));


            System.out.println(ChatColor.translateAlternateColorCodes('&', "&e=================" +
                    "=================="));
            System.out.println(ChatColor.translateAlternateColorCodes('&', "&c" + e.getMessage()));


            System.out.println(ChatColor.translateAlternateColorCodes('&', "&e=================" +
                    "=================="));

        }
    }

    @Override
    public void onDisable() {

    }

    public YamlFile getConfiguracion() {
        return Configuracion;
    }

    public YamlFile getObjetos() {
        return Objetos;
    }


    public Inventory getInventory(Player player) {

        YamlFile playerFile = new YamlFile("plugins/SubastasTerreta/Data/" + player.getName() + ".yml");
        try {
            playerFile.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ItemStack empty = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta meta = empty.getItemMeta();

        assert meta != null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f&l⇫"));
        Monedas monedas = new Monedas();
        ItemStack claim = new ItemStack(Material.GOLD_INGOT, 1);
        ItemMeta itemMeta = claim.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lRECLAMA TUS MONEDAS"));
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_PURPLE + "            (" + playerFile.getInt("Monedas") + ")");
        itemMeta.setLore(lore);
        empty.setItemMeta(meta);
        claim.setItemMeta(itemMeta);


        Inventory inventory = Bukkit.createInventory(null, 54,
                ChatColor.translateAlternateColorCodes('&', "&0Objetos de &d" + player.getName()));

        //45 - 53
        setOtherContent(inventory, playerFile);

        inventory.setItem(45, empty);
        inventory.setItem(46, empty);
        inventory.setItem(47, empty);
        inventory.setItem(48, empty);
        inventory.setItem(49, claim);
        inventory.setItem(50, empty);
        inventory.setItem(51, empty);
        inventory.setItem(52, empty);
        inventory.setItem(53, empty);


        return inventory;
    }

    public void setOtherContent(Inventory inventory, YamlFile playerFile) {
        List<ItemStack> stacks = new ArrayList<>();

        for (String itemID : playerFile.getConfigurationSection("Items").getKeys(false)) {
            if (playerFile.getInt("Items." + itemID + ".TimeLeft") <= 0) {
                boolean loreEmpty = true;
                ItemStack itemStack = new ItemStack(Material.valueOf(playerFile.getString("Items." + itemID + ".Type")));


                if (playerFile.getStringList("Items." + itemID + ".Lore") != null) {
                    loreEmpty = false;
                }

                ItemMeta meta = itemStack.getItemMeta();

                if (!loreEmpty) {
                    List<String> lored = new ArrayList<>(playerFile.getStringList("Items." + itemID + ".Lore"));
                    lored.add(itemID);

                    assert meta != null;
                    meta.setLore(lored);
                    meta.setDisplayName(playerFile.getString("Items." + itemID + ".DisplayName"));
                    itemStack.setItemMeta(meta);
                }else{
                    List<String> lore = new ArrayList<>();
                    lore.add(itemID);
                    assert meta != null;
                    meta.setLore(lore);
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
                stacks.add(itemStack);


            }
        }
        for (int i = 0; i < stacks.size(); i++){
            inventory.setItem(i, stacks.get(i));
        }

    }

}
