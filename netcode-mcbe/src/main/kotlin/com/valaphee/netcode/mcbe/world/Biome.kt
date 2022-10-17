package com.valaphee.netcode.mcbe.world

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue

/**
 * @author Kevin Ludwig
 */
data class Biome(
    @JsonProperty("name_hash") val nameHash: String?,
    @JsonProperty("tags") val tags: List<String>?,
    @JsonProperty("height") val height: Float,
    @JsonProperty("depth") val depth: Float,
    @JsonProperty("rain") val rain: Boolean,
    @JsonProperty("downfall") val downfall: Float,
    @JsonProperty("ash") val ash: Float,
    @JsonProperty("white_ash") val whiteAsh: Float,
    @JsonProperty("red_spores") val redSpores: Float,
    @JsonProperty("blue_spores") val blueSpores: Float,
    @JsonProperty("temperature") val temperature: Float,
    @JsonProperty("waterColorA") val waterColorA: Float,
    @JsonProperty("waterColorB") val waterColorB: Float,
    @JsonProperty("waterColorG") val waterColorG: Float,
    @JsonProperty("waterColorR") val waterColorR: Float,
    @JsonProperty("waterTransparency") val waterTransparency: Float,
    @JsonProperty("minecraft:climate") val climate: Climate?,
    @JsonProperty("minecraft:surface_parameters") val surfaceParameters: Surface?,
    @JsonProperty("minecraft:surface_material_adjustments") val surfaceMaterialAdjustments: SurfaceMaterialAdjustments?,
    @JsonProperty("minecraft:mountain_parameters") val mountainParameters: MountainParameters?,
    @JsonProperty("minecraft:frozen_ocean_surface") val frozenOceanSurface: Surface?,
    @JsonProperty("minecraft:swamp_surface") val swampSurface: Surface?,
    @JsonProperty("minecraft:mesa_surface") val mesaSurface: MesaSurface?,
    @JsonProperty("minecraft:capped_surface") val cappedSurface: CappedSurface?,
    @JsonProperty("minecraft:the_end_surface") val theEndSurface: TheEndSurface?,
    @JsonProperty("minecraft:consolidated_features") val consolidatedFeatures: ConsolidatedFeatures?,
    @JsonProperty("minecraft:legacy_world_generation_rules") val legacyWorldGenerationRules: LegacyWorldGenerationRules?,
    @JsonProperty("minecraft:multinoise_generation_rules") val multinoiseGenerationRules: MultinoiseGenerationRules?,
    @JsonProperty("minecraft:overworld_generation_rules") val overworldGenerationRules: OverworldGenerationRules?
) {
    data class Climate(
        @JsonProperty("temperature") val temperature: Float,
        @JsonProperty("downfall") val downfall: Float,
        @JsonProperty("ash") val ash: Float,
        @JsonProperty("white_ash") val whiteAsh: Float,
        @JsonProperty("red_spores") val redSpores: Float,
        @JsonProperty("blue_spores") val blueSpores: Float,
        @JsonProperty("snow_accumulation_max") val snowAccumulationMax: Float,
        @JsonProperty("snow_accumulation_min") val snowAccumulationMin: Float
    )

    open class Surface(
        @JsonProperty("topBlock") val topBlock: BlockState,
        @JsonProperty("midBlock") val midBlock: BlockState,
        @JsonProperty("foundationBlock") val foundationBlock: BlockState,
        @JsonProperty("seaBlock") val seaBlock: BlockState,
        @JsonProperty("seaFloorBlock") val seaFloorBlock: BlockState,
        @JsonProperty("seaFloorDepth") val seaFloorDepth: Int
    ) {
        override fun toString() = "SurfaceParameters(topBlock=$topBlock, midBlock=$midBlock, foundationBlock=$foundationBlock, seaBlock=$seaBlock, seaFloorBlock=$seaFloorBlock, seaFloorDepth=$seaFloorDepth)"
    }

    data class SurfaceMaterialAdjustments(
        @JsonProperty("adjustments") val adjustments: List<Adjustment>
    ) {
        data class Adjustment(
            @JsonProperty("adjustedMaterials") val adjustedMaterials: Surface,
            @JsonProperty("heightMax") val heightMax: String,
            @JsonProperty("heightMaxType") val heightMaxType: ValueType,
            @JsonProperty("heightMin") val heightMin: String,
            @JsonProperty("heightMinType") val heightMinType: ValueType,
            @JsonProperty("noiseFreqScale") val noiseFreqScale: Float,
            @JsonProperty("noiseLowerBound") val noiseLowerBound: Float,
            @JsonProperty("noiseUpperBound") val noiseUpperBound: Float
        )
    }

    data class MountainParameters(
        @JsonProperty("peaksFactor") val peaksFactor: Float,
        @JsonProperty("northSlopes") val northSlopes: Boolean,
        @JsonProperty("southSlopes") val southSlopes: Boolean,
        @JsonProperty("westSlopes") val westSlopes: Boolean,
        @JsonProperty("eastSlopes") val eastSlopes: Boolean,
        @JsonProperty("topSlideEnalbed") val topSlideEnabled: Boolean,
        @JsonProperty("steepBlock") val steepBlock: BlockState
    )

    class MesaSurface(
        topBlock: BlockState,
        midBlock: BlockState,
        foundationBlock: BlockState,
        seaBlock: BlockState,
        seaFloorBlock: BlockState,
        seaFloorDepth: Int,
        @JsonProperty("brycePillars") val brycePillars: Boolean,
        @JsonProperty("clayMaterial") val clayMaterial: BlockState,
        @JsonProperty("hardClayMaterial") val hardClayMaterial: BlockState,
        @JsonProperty("hasForest") val hasForest: Boolean
    ) : Surface(topBlock, midBlock, foundationBlock, seaBlock, seaFloorBlock, seaFloorDepth)

    data class CappedSurface(
        @JsonProperty("ceilingMaterials") val ceilingMaterials: List<Int>,
        @JsonProperty("floorMaterials") val floorMaterials: List<Int>,
        @JsonProperty("beachMaterial") val beachMaterial: Int,
        @JsonProperty("seaMaterial") val seaMaterial: Int,
        @JsonProperty("foundationMaterial") val foundationMaterial: Int
    )

    class TheEndSurface

    data class ConsolidatedFeatures(
        @JsonProperty("features") val features: List<Feature>
    ) {
        data class Feature(
            @JsonProperty("identifier") val identifier: String,
            @JsonProperty("feature") val feature: String,
            @JsonProperty("pass") val pass: Pass,
            @JsonProperty("scatter") val scatter: Scatter
        ) {
            enum class Pass {
                @JsonProperty("pregeneration_pass") PregenerationPass,
                @JsonProperty("first_pass") FirstPass,
                @JsonProperty("before_surface_pass") BeforeSurfacePass,
                @JsonProperty("surface_pass") SurfacePass,
                @JsonProperty("after_surface_pass") AfterSurfacePass,
                @JsonProperty("before_underground_pass") BeforeUndergroundPass,
                @JsonProperty("underground_pass") UndergroundPass,
                @JsonProperty("after_underground_pass") AfterUndergroundPass
            }

            data class Scatter(
                @JsonProperty("chanceDenominator") val chanceDenominator: Int,
                @JsonProperty("chanceNumerator") val changeNumerator: Int,
                @JsonProperty("chancePercent") val chancePercent: String,
                @JsonProperty("chancePercentType") val chancePercentType: ValueType,
                @JsonProperty("coordinates") val coordinates: List<Coordinate>,
                @JsonProperty("evalOrder") val evalOrder: Byte,
                @JsonProperty("iterations") val iterations: String,
                @JsonProperty("iterationsType") val iterationsType: ValueType
            ) {
                data class Coordinate(
                    @JsonProperty("distribution") val distribution: Boolean,
                    @JsonProperty("gridOffset") val gridOffset: Int,
                    @JsonProperty("gridStepSize") val gridStepSize: Int,
                    @JsonProperty("maxValue") val maxValue: String,
                    @JsonProperty("maxValueType") val maxValueType: ValueType,
                    @JsonProperty("minValue") val minValue: String,
                    @JsonProperty("minValueType") val minValueType: ValueType
                )
            }
        }
    }

    data class LegacyWorldGenerationRules(
        @JsonProperty("legacy_pre_hills_edge_transformation") val legacyPreHillsEdgeTransformation: List<EdgeTransformation>
    )

    data class MultinoiseGenerationRules(
        @JsonProperty("target_altitude") val targetAltitude: Float,
        @JsonProperty("target_humidity") val targetHumidity: Float,
        @JsonProperty("target_temperature") val targetTemperature: Float,
        @JsonProperty("target_weirdness") val targetWeirdness: Float,
        @JsonProperty("weight") val weight: Float
    )

    data class OverworldGenerationRules(
        @JsonProperty("extended_edge_transformation") val extendedEdgeTransformation: List<EdgeTransformation>?,
        @JsonProperty("generate_for_climates") val generateForClimtates: List<Climate>?,
        @JsonProperty("river_transformation") val riverTransformation: List<Transformation>?,
        @JsonProperty("shore_transformation") val shoreTransformation: List<Transformation>?,
        @JsonProperty("hills_transformation") val hillsTransformation: List<Transformation>?,
        @JsonProperty("mutate_transformation") val mutateTransformation: List<Transformation>?,
        @JsonProperty("pre_hills_edge_transformation") val preHillsEdgeTransformation: List<EdgeTransformation>?
    ) {
        data class Climate(
            @JsonProperty("temperature") val temperature: Int,
            @JsonProperty("weight") val weight: Int
        )
    }

    enum class ValueType(
        @get:JsonValue val id: Int
    ) {
        None(-1),
        MolangOrigin(15),
        Molang(33),
        MolangWorld(40),
        MolangSeaLevel(43),
        MolangLegacy(59),
        Literal(61);
    }

    data class BlockState(
        val name: String?,
        val states: List<State>?
    ) {
        data class State(
            val state: String,
            val value: Int
        )
    }

    data class Transformation(
        @JsonProperty("biome") val biome: String,
        @JsonProperty("weight") val weight: Int
    )

    data class EdgeTransformation(
        @JsonProperty("condition") val condition: String,
        @JsonProperty("min_passing_neighbors") val minPassingNeighbors: Int,
        @JsonProperty("transforms_into") val transformsInto: List<Transformation>
    )
}
