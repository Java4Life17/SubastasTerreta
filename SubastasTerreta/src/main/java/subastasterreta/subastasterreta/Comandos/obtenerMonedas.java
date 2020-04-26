package subastasterreta.subastasterreta.Comandos;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import subastasterreta.subastasterreta.SubastasTerreta;
import subastasterreta.subastasterreta.Utiles.Monedas;
import subastasterreta.subastasterreta.Utiles.UtilesPrincipales;

import java.util.Objects;

public class obtenerMonedas implements CommandExecutor {


    UtilesPrincipales utiles = new UtilesPrincipales();
    Monedas monedas = new Monedas();


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player usuario = (Player) sender;

        if (command.getName().equalsIgnoreCase("AdminSubastas")) {
            if (!usuario.hasPermission("AdminSubasta.ObtenerMoneda")) {
                utiles.playSound(usuario, Sound.ENTITY_VILLAGER_NO, 1.0F);
                utiles.sendMessage(usuario, "&cNo tienes permiso para usar este comando!");
                return true;
            }
            try {
                /// AdminSubastas obtenerMoneda <nugget, lingote, bloque> <cantidad>
                ItemStack moneda;
                int cantidad = Integer.parseInt(args[2]);

                if (args[0].equalsIgnoreCase("obtenerMoneda")) {
                    if (args[1].equalsIgnoreCase("nugget")) {
                        moneda = monedas.obtenerMoneda(cantidad);
                    } else if (args[1].equalsIgnoreCase("lingote")) {
                        moneda = monedas.obtenerMonedaLingote(cantidad);
                    } else if (args[1].equalsIgnoreCase("Bloque")) {
                        moneda = monedas.obtenerMonedaBloque(cantidad);
                    } else {
                        moneda = monedas.obtenerMoneda(cantidad);
                    }

                    /*boolean cabeEnElInventario = false;

                    for(int amount = 1; amount <= 64; amount++){
                        int nuevoResultado = cantidad + amount;
                        ItemStack moneda1 = moneda.clone();
                        moneda1.setAmount(amount);

                        if(moneda1 == moneda && usuario.getInventory().firstEmpty() != -1){

                            if(nuevoResultado < 64){
                                cabeEnElInventario = true;
                                break;
                            }
                        }
                    }
                     */



                    for(int tiene = 1; tiene < 65; tiene++){
                        moneda.setAmount(tiene);
                        if(usuario.getInventory().contains(moneda)){

                            if(usuario.getInventory().firstEmpty() == -1){
                                utiles.sendMessage(usuario, "&c&lERROR! &cEsta cantidad no cabe en tu inventario.");
                                utiles.playSound(usuario, Sound.BLOCK_BELL_RESONATE, 1.2F);
                                return true;
                            }

                            

                        }


                    }










                    utiles.sendMessage(usuario, "&eCon exito se agrego la moneda " + Objects.requireNonNull(moneda.getItemMeta()).getDisplayName() + " &ea tu inventario!");
                    moneda.setAmount(cantidad);
                    usuario.getInventory().addItem(moneda);
                    utiles.playSound(usuario, Sound.ENTITY_PLAYER_LEVELUP, 1.2F);
                }


            } catch (Exception e) {
                utiles.sendMessage(usuario, "&cOops, aparentemente no está utilizando este comando como debe ser " +
                        "utilizado. Por favor, asegúrese de utilizar el siguiente formato! " +
                        "&6/AdminSubastas obtenerMoneda <nugget, lingote, bloque> <cantidad>");
                utiles.playSound(usuario, Sound.ENTITY_VILLAGER_NO, 0.8F);
            }
        }

        return false;
    }
}
