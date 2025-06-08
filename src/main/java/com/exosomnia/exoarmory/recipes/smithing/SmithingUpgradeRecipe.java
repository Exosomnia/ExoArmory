package com.exosomnia.exoarmory.recipes.smithing;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.item.UpgradeTemplateItem;
import com.exosomnia.exoarmory.item.armory.ArmoryItem;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.Level;

public class SmithingUpgradeRecipe implements SmithingRecipe {

    private final ResourceLocation resourceLocation;
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient additional;
    private final int rankTo;
    private final int rankFrom;

    public SmithingUpgradeRecipe(ResourceLocation resourceLocation, Ingredient template, Ingredient base, Ingredient additional) {
        this.resourceLocation = resourceLocation;
        this.template = template;
        this.base = base;
        this.additional = additional;

        this.rankTo = ((UpgradeTemplateItem)template.getItems()[0].getItem()).rankTo;
        this.rankFrom = ((UpgradeTemplateItem)template.getItems()[0].getItem()).rankFrom;
    }

    @Override
    public boolean matches(Container container, Level level) {
        ItemStack baseItem = container.getItem(1);
        return this.template.test(container.getItem(0)) && this.base.test(baseItem) && this.additional.test(container.getItem(2)) &&
                (baseItem.getItem() instanceof ArmoryItem armoryItem) && (armoryItem.getRank(baseItem) == rankFrom);
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        ItemStack itemstack = container.getItem(1).copy();
        if (itemstack.getItem() instanceof ArmoryItem armoryItem) { armoryItem.setRank(itemstack, rankTo); }
        return itemstack;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        ItemStack itemStack = new ItemStack(ExoArmory.REGISTRY.ITEM_GIGA_SWORD.get());
        ((ArmoryItem)ExoArmory.REGISTRY.ITEM_GIGA_SWORD.get()).setRank(itemStack, rankTo);
        return itemStack;
    }

    @Override
    public ResourceLocation getId() {
        return this.resourceLocation;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ExoArmory.REGISTRY.RECIPE_SMITHING_UPGRADE.get();
    }

    @Override
    public boolean isTemplateIngredient(ItemStack itemStack) {
        return this.template.test(itemStack);
    }

    @Override
    public boolean isBaseIngredient(ItemStack itemStack) {
        return this.base.test(itemStack);
    }

    @Override
    public boolean isAdditionIngredient(ItemStack itemStack) {
        return this.additional.test(itemStack);
    }

    public static class Serializer implements RecipeSerializer<SmithingUpgradeRecipe> {
        public SmithingUpgradeRecipe fromJson(ResourceLocation resourceLocation, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getNonNull(json, "template"));
            Ingredient ingredient1 = Ingredient.fromJson(GsonHelper.getNonNull(json, "base"));
            Ingredient ingredient2 = Ingredient.fromJson(GsonHelper.getNonNull(json, "addition"));
            return new SmithingUpgradeRecipe(resourceLocation, ingredient, ingredient1, ingredient2);
        }

        public SmithingUpgradeRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            Ingredient ingredient1 = Ingredient.fromNetwork(buffer);
            Ingredient ingredient2 = Ingredient.fromNetwork(buffer);
            return new SmithingUpgradeRecipe(resourceLocation, ingredient, ingredient1, ingredient2);
        }

        public void toNetwork(FriendlyByteBuf buffer, SmithingUpgradeRecipe recipe) {
            recipe.template.toNetwork(buffer);
            recipe.base.toNetwork(buffer);
            recipe.additional.toNetwork(buffer);
        }
    }
}
