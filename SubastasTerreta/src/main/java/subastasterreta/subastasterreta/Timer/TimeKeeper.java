package subastasterreta.subastasterreta.Timer;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;
import subastasterreta.subastasterreta.Comandos.Subastas;
import subastasterreta.subastasterreta.SubastasTerreta;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class TimeKeeper implements Listener {

    private SubastasTerreta plugin;

    public TimeKeeper(SubastasTerreta plugin) {
        this.plugin = plugin;
    }


    public void startTimer() {


        new BukkitRunnable() {

            @Override
            public void run() {


                for (File yamlFile : Objects.requireNonNull(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "Data").listFiles())) {

                    YamlFile archivo = new YamlFile("plugins/SubastasTerreta/Data/" + yamlFile.getName());
                    try {
                        archivo.load();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for (String itemID : archivo.getConfigurationSection("Items").getKeys(false)) {

                        int minutes = archivo.getInt("Items." + itemID + ".TimeLeft");
                        int left = minutes - 1;


                        if (minutes > 0) {
                            //5
                            archivo.set("Items." + itemID + ".TimeLeft", left);
                        }

                    }
                    try {
                        archivo.save();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        }.runTaskTimer(plugin, 20, 1200);


    }


}
