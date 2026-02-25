package gdavid.psitweaker;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@EventBusSubscriber(bus = Bus.MOD)
public class Config {
	
	public static final ForgeConfigSpec spec;
	public static final Config instance;
	
	static {
		var p = new ForgeConfigSpec.Builder().configure(Config::new);
		spec = p.getRight();
		instance = p.getLeft();
	}
	
	public final ConfigValue<String> totalPsi, psiRegen, castRegenCooldown, damageRegenCooldown, damagePsiLoss, overflowDamage, bulletCostModifier;
	public Expression baseExpression, eTotalPsi, ePsiRegen, eCastRegenCooldown, eDamageRegenCooldown, eDamagePsiLoss, eOverflowDamage, eBulletCostModifier;
	
	public Config(ForgeConfigSpec.Builder builder) {
		totalPsi = builder.define("capacity", "base");
		psiRegen = builder.define("regen", "base");
		castRegenCooldown = builder.define("cast.regen_cd", "base");
		damageRegenCooldown = builder.define("damage.regen_cd", "base");
		damagePsiLoss = builder.define("damage.psi_loss", "base");
		overflowDamage = builder.define("overflow.damage.amount", "base");
		// TODO split to bullet.<item ID>.cost_modifier
		bulletCostModifier = builder.define("bullet.cost_modifier", "base");
		
		baseExpression = new Expression("base");
	}
	
	public void prepareExprs() {
		System.out.println("Loading PsiTweaker config");
		
		eTotalPsi = prepareExpr(totalPsi);
		ePsiRegen = prepareExpr(psiRegen);
		eCastRegenCooldown = prepareExpr(castRegenCooldown);
		eDamageRegenCooldown = prepareExpr(damageRegenCooldown);
		eDamagePsiLoss = prepareExpr(damagePsiLoss);
		eOverflowDamage = prepareExpr(overflowDamage);
		eBulletCostModifier = prepareExpr(bulletCostModifier);
	}
	
	private Expression prepareExpr(ConfigValue<String> source) {
		var expr = new Expression(source.get());
		try {
			expr.validate();
		} catch (ParseException e) {
			e.printStackTrace();
			return baseExpression;
		}
		return expr;
	}
	
	@SubscribeEvent
	public static void onLoad(ModConfigEvent e) {
		instance.prepareExprs();
	}
	
}
