package site.siredvin.peripheralium.common.blocks

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import site.siredvin.peripheralium.api.blockentities.IObservingBlockEntity
import site.siredvin.peripheralium.api.blockentities.IOwnedBlockEntity
import site.siredvin.peripheralium.api.peripheral.IPeripheralTileEntity

abstract class BaseTileEntityBlock<T : BlockEntity>(
    private val belongToTickingEntity: Boolean,
    properties: Properties = Properties.of(Material.METAL).strength(1f, 5f).sound(SoundType.METAL).noOcclusion(),
) : BaseEntityBlock(properties) {

    override fun getRenderShape(blockState: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun <T : BlockEntity> getTicker(
        tickerLevel: Level,
        tickerState: BlockState,
        type: BlockEntityType<T>,
    ): BlockEntityTicker<T>? {
        if (tickerLevel.isClientSide || !belongToTickingEntity) {
            return null
        }
        return BlockEntityTicker { level, pos, state, entity ->
            if (entity is IPeripheralTileEntity) {
                entity.handleTick(level, pos, state)
            }
        }
    }

    override fun neighborChanged(
        blockState: BlockState,
        level: Level,
        blockPos: BlockPos,
        neighbourBlock: Block,
        neighbourPos: BlockPos,
        bl: Boolean,
    ) {
        super.neighborChanged(blockState, level, blockPos, neighbourBlock, neighbourPos, bl)
        val tile = level.getBlockEntity(blockPos)
        if (tile is IObservingBlockEntity) {
            tile.onNeighbourChange(neighbourPos)
        }
    }

    override fun tick(blockState: BlockState, serverLevel: ServerLevel, blockPos: BlockPos, random: RandomSource) {
        super.tick(blockState, serverLevel, blockPos, random)
        val tile = serverLevel.getBlockEntity(blockPos)
        if (tile is IObservingBlockEntity) {
            tile.blockTick()
        }
    }

    override fun onPlace(
        blockState: BlockState,
        level: Level,
        blockPos: BlockPos,
        newState: BlockState,
        bl: Boolean,
    ) {
        super.onPlace(blockState, level, blockPos, newState, bl)
        if (newState.block === this) {
            val tile = level.getBlockEntity(blockPos)
            if (tile is IObservingBlockEntity) {
                tile.placed()
            }
        }
    }

    override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, entity: LivingEntity?, stack: ItemStack) {
        super.setPlacedBy(level, pos, state, entity, stack)
        val tile = level.getBlockEntity(pos)
        if (tile is IOwnedBlockEntity && !level.isClientSide && entity is Player) {
            tile.player = entity
        }
        if (tile is IObservingBlockEntity) {
            tile.setPlacedBy(entity, stack)
        }
    }

    override fun onRemove(
        blockState: BlockState,
        level: Level,
        blockPos: BlockPos,
        replace: BlockState,
        bl: Boolean,
    ) {
        if (blockState.block === replace.block) {
            return
        }
        val tile = level.getBlockEntity(blockPos)
        super.onRemove(blockState, level, blockPos, replace, bl)
        if (tile is IObservingBlockEntity) {
            tile.destroy()
        }
    }
}
