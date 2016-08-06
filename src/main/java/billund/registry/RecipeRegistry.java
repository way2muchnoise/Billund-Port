package billund.registry;

import billund.recipe.RecipeBrick;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipeRegistry
{
    public static void init()
    {
        GameRegistry.addRecipe(new RecipeBrick());
    }
}
