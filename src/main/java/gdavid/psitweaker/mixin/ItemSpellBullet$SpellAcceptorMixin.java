package gdavid.psitweaker.mixin;

import gdavid.psitweaker.Config;
import gdavid.psitweaker.PsiTweaker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.item.ItemSpellBullet;

@Mixin(targets = "vazkii.psi.common.item.ItemSpellBullet$SpellAcceptor", remap = false, priority = 0)
public abstract class ItemSpellBullet$SpellAcceptorMixin {
	
	@Shadow public abstract Spell getSpell();
	
	@Shadow protected abstract ItemSpellBullet bulletItem();
	
	@Inject(method = "getCostModifier", at = @At("RETURN"), cancellable = true)
	private void costModifier(CallbackInfoReturnable<Double> ci) {
		try {
			var spell = getSpell();
			ci.setReturnValue(PsiTweaker.withSpellStats(Config.instance.eBulletCostModifier, spell == null ? null : PsiAPI.internalHandler.getSpellCache().getCompiledSpell(spell))
					.with("base", ci.getReturnValue())
					.with("type", bulletItem().getBulletType())
					.evaluate().getNumberValue().doubleValue());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
