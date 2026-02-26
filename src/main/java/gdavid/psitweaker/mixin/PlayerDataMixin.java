package gdavid.psitweaker.mixin;

import com.ezylang.evalex.Expression;
import gdavid.psitweaker.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;

@Mixin(value = PlayerData.class, remap = false, priority = 0)
public abstract class PlayerDataMixin {
	
	@Unique
	private Expression psiTweaker$withVars(Expression expr, Object base, boolean psi) {
		var self = (PlayerData) (Object) this;
		var player = self.playerWR.get();
		if (player == null) {
			System.err.format("playerWR was null in PlayerData %s", self);
			return Config.instance.baseExpression;
		}
		expr = expr.with("base", base)
				.with("health", player.getHealth())
				.with("maxhealth", player.getMaxHealth())
				.with("armor", player.getArmorValue())
				.with("loopcasting", self.loopcasting);
		if (psi) {
			expr = expr.with("psi", self.getAvailablePsi())
					.with("maxpsi", self.getTotalPsi());
		}
		return expr;
	}
	
	@Inject(method = "getRegenPerTick", at = @At("RETURN"), cancellable = true)
	private void psiRegen(CallbackInfoReturnable<Integer> ci) {
		try {
			ci.setReturnValue(psiTweaker$withVars(Config.instance.ePsiRegen, ci.getReturnValue(), true)
					.evaluate().getNumberValue().intValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Inject(method = "getTotalPsi", at = @At("RETURN"), cancellable = true)
	private void totalPsi(CallbackInfoReturnable<Integer> ci) {
		try {
			ci.setReturnValue(psiTweaker$withVars(Config.instance.eTotalPsi, ci.getReturnValue(), false)
					.evaluate().getNumberValue().intValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@ModifyArg(method = "deductPsi(IIZZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", remap = true), index = 1)
	private float overflowDamage(float dmg) {
		try {
			return psiTweaker$withVars(Config.instance.eOverflowDamage, dmg, true)
					.evaluate().getNumberValue().floatValue();
		} catch (Exception e) {
			e.printStackTrace();
			return dmg;
		}
	}
	
	@Inject(method = "calculateDamageDeduction", at = @At("RETURN"), cancellable = true)
	private void damagePsiLoss(float damage, CallbackInfoReturnable<Integer> ci) {
		try {
			ci.setReturnValue(psiTweaker$withVars(Config.instance.eDamagePsiLoss, ci.getReturnValue(), true)
					.with("damage", damage)
					.evaluate().getNumberValue().intValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@ModifyArg(method = "damage", at = @At(value = "INVOKE", target = "Lvazkii/psi/common/core/handler/PlayerDataHandler$PlayerData;deductPsi(IIZZ)V"), index = 1)
	private int damageRegenCooldown(int cd) {
		try {
			return psiTweaker$withVars(Config.instance.eDamageRegenCooldown, cd, true)
					.evaluate().getNumberValue().intValue();
		} catch (Exception e) {
			e.printStackTrace();
			return cd;
		}
	}
	
}
