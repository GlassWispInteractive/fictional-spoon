package combat;

public interface IAttackable {

	public void getDmg(int dmg);

	public void doAttack(IAttackable focus);

	public void doAttack(IAttackable focus, Combo combo);

}
