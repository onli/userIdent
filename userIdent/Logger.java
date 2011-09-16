package userIdent;

public class Logger {
	public void log (String text) {
		if (! Options.demo) { 
			Gui.getInstance().showText(text +"\n");
		} else {
			System.out.println(text);
		}
	}
	public void log (char c) {
		if (! Options.demo) {
			Gui.getInstance().showText(""+c);
		} else {
			System.out.print(c);
		}
	}
}
