package general;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import menu.Menu;
import menu.MenuOption;

public class InputHandler implements KeyListener{
	private int ignoreMovementForTicks;
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key x = new Key();
	public Key c = new Key();
	public Key shift = new Key();
	
	public InputHandler(){
		S.game.addKeyListener(this);
		ignoreMovementForTicks = 0;
	}
	
	public void tick(){
		if(ignoreMovementForTicks > 0)
			ignoreMovementForTicks--;
	}
	
	public void stopMovementForTicks(int ticks){
		if(ticks > ignoreMovementForTicks){
			left.pressed = false;
			right.pressed = false;
			up.pressed = false;
			down.pressed = false;
			ignoreMovementForTicks = ticks;
		}
			
	}
	
	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
		pressKey(e.getKeyCode());
	}
	private void pressKey(int keyCode) {
		switch(keyCode){
		case KeyEvent.VK_E:			e();						break;
		case KeyEvent.VK_ENTER:		enter();					break;
		case KeyEvent.VK_SPACE:		spacebar();					break;
		case KeyEvent.VK_BACK_SPACE:S.game.goToSpace();			break;
		case KeyEvent.VK_ESCAPE:	escape();					break;
		case KeyEvent.VK_W:
		case KeyEvent.VK_UP:		up();						break;
		case KeyEvent.VK_S:
		case KeyEvent.VK_DOWN:		down();						break;
		case KeyEvent.VK_I:			i();						break;
		case KeyEvent.VK_CONTROL:	ctrl();						break;
		case KeyEvent.VK_X:			x();						break;
		case KeyEvent.VK_TAB:		tab();						break;
		case KeyEvent.VK_P:			p();						break;
		}
		if(keyCode >= '0' && keyCode <= '9')
			digit(keyCode - '0');
	}

	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
	}
	public void keyTyped(KeyEvent e) {
		//nothing
	}

	public void toggleKey(int keyCode, boolean isPressed){
		switch(keyCode){
		case KeyEvent.VK_X:		x.toggle(isPressed);	break;
		case KeyEvent.VK_C:		c.toggle(isPressed);	break;
		case KeyEvent.VK_SHIFT:	shift.toggle(isPressed);break;
		}
		
		if(ignoreMovementForTicks == 0)
			switch(keyCode){
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:	up.toggle(isPressed);	break;
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:	down.toggle(isPressed);	break;
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:	left.toggle(isPressed);	break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:	right.toggle(isPressed);break;
			}
	}
	
	public class Key{
		private boolean pressed = false;
		private int numTimesPressed;
		
		public void toggle(boolean isPressed){
			pressed = isPressed;
			if(pressed)
				numTimesPressed++;
		}
		
		public int getNumTimesPressed(){
			return numTimesPressed;
		}
		
		public boolean isPressed(){
			return pressed;
		}
	}
	
	//KEY PRESS

	private void up()				{if(S.menu != null)	S.menu.upPressed();}
	private void down()				{if(S.menu != null)	S.menu.downPressed();}
	private void digit(int digit)	{if(S.menu != null)	S.menu.jumpToOption(digit-1); else S.select(digit);}
	private void e() 				{S.selected().tryPickUp();}
	private void ctrl()				{if(S.vehicle() != null) S.selected().togglePiloting();}
	private void tab()				{if(shift.isPressed())S.selectDown(); else S.selectUp();}
	private void spacebar()			{if(S.menu != null)	S.menu.enter();else S.toggleHud();}
	private void x()				{if(S.vehicle() != null)S.vehicle().getMrShip().setKillEngines(true);}
	private void p()				{S.toggleGamePaused();}
	
	private void enter(){
		if(S.menu == null){
			if(S.vehicle() != null && S.location() == null)
				S.galaxy.tryLand(S.vehicle().getMrShip());
		}else
			S.menu.enter();
	}
	
	private void i() {
		if(S.menu == null){
			Inventory selectedInv = S.selected().getInventory();
			if(selectedInv == null)
				return;
			selectedInv.selectInventoryItem();
		}
	}
	
	private void escape() {
		if(S.menu == null){
			MenuOption oReturn = new MenuOption("Return", "return", "", 0, null);
			MenuOption oAskExitGame = new MenuOption("Exit", "ask", "exit", 0, null);
			S.menu = new Menu("Exit game?", oReturn, oAskExitGame);
		}
	}
}
