package billund.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryHelper {

    /* Check if given ItemStack matches given oreDictionary name
     *
     *  @param stack the to check stack
     *  @param oreName the target oreName
     *  @return true if stack equals ore name
     */
    public static boolean compareOre(ItemStack stack, String oreName) {
        for (ItemStack ore : OreDictionary.getOres(oreName)) {
            if (OreDictionary.itemMatches(ore, stack, false)) return true;
        }
        return false;
    }

    /* Check if given stack has a dye entry in the oreDictionary
     *
     * @param stack the to check stack
     * @return the dye color or null if no dye color was found
     */
    public static String getDyeName(ItemStack stack) {
        for (int i : OreDictionary.getOreIDs(stack)) {
            String name = OreDictionary.getOreName(i);
            if (name.startsWith("dye") && name.length() > 3) {
                return name.substring(3);
            }
        }
        return null;
    }
}
