package com.boydti.fawe.bukkit.v1_8;

import java.util.Arrays;
import net.minecraft.server.v1_8_R3.GenLayer;
import net.minecraft.server.v1_8_R3.IntCache;

public class MutableGenLayer extends GenLayer {

    private int biome;

    public MutableGenLayer(long seed) {
        super(seed);
    }

    public MutableGenLayer set(int biome) {
        this.biome = biome;
        return this;
    }

    @Override
    public int[] a(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] biomes = IntCache.a(areaWidth * areaHeight);
        Arrays.fill(biomes, biome);
        return biomes;
    }
}