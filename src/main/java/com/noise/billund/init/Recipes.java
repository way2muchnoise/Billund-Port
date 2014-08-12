package com.noise.billund.init;

import com.noise.billund.recipe.RecipeBrick;
import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes {

    public static void init() {
        GameRegistry.addRecipe(new RecipeBrick());
    }
}
