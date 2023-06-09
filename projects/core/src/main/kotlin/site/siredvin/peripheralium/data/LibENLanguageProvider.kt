package site.siredvin.peripheralium.data

import net.minecraft.data.PackOutput
import site.siredvin.peripheralium.PeripheraliumCore
import site.siredvin.peripheralium.common.setup.Blocks
import site.siredvin.peripheralium.common.setup.Items
import site.siredvin.peripheralium.data.language.LanguageProvider
import site.siredvin.peripheralium.xplat.LibPlatform

class LibENLanguageProvider(
    output: PackOutput,
) : LanguageProvider(output, PeripheraliumCore.MOD_ID, "en_us", LibPlatform.holder, *LibText.values()) {
    override fun addTranslations() {
        add(Items.PERIPHERALIUM_DUST.get(), "Peripheralium dust")
        add(Items.PERIPHERALIUM_BLEND.get(), "Peripheralium blend")
        add(Blocks.PERIPHERALIUM_BLOCK.get(), "Peripheralium block")
        add(LibText.CREATIVE_TAB, "Peripheralium")
        add(LibText.PRESS_FOR_DESCRIPTION, "[§3Left shift§r] show description")
    }
}
