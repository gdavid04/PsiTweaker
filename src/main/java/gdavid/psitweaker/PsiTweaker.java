package gdavid.psitweaker;

import com.ezylang.evalex.Expression;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig.Type;
import vazkii.psi.api.spell.CompiledSpell;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.PreSpellCastEvent;

@Mod(PsiTweaker.modId)
@EventBusSubscriber
public class PsiTweaker {
	
	public static final String modId = "psitweaker";
	
	public PsiTweaker() {
		ModLoadingContext.get().registerConfig(Type.COMMON, Config.spec);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void preSpellCast(PreSpellCastEvent e) {
		var ctx = e.getContext();
		String slot = null;
		if (ctx.castFrom != null) slot = ctx.castFrom.name().toLowerCase();
		else if (ctx.tool.getEquipmentSlot() != null) slot = ctx.tool.getEquipmentSlot().name().toLowerCase();
		try {
			e.setCooldown(withSpellStats(Config.instance.eCastRegenCooldown, ctx.cspell)
					.with("base", e.getCooldown())
					.with("slot", slot)
					.with("from", ctx.castFrom == null ? "armor" : "hand")
					.with("item", String.valueOf(ctx.tool != null ? ctx.tool : e.getCad()))
					.evaluate().getNumberValue().intValue());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static Expression withSpellStats(Expression expr, CompiledSpell spell) {
		for (var stat : EnumSpellStat.values()) {
			expr = expr.with(stat.name().toLowerCase(), spell == null ? 0 : spell.metadata.getStat(stat));
		}
		return expr;
	}
	
}
