package mg.rova.monkey;

import com.jme3.system.AppSettings;

public class App {

	public static void main(String[] args) {
		final AppCore app = new AppCore();

		final AppSettings settings = new AppSettings(true);
		settings.setTitle("Game Beta 1.0");
		settings.setResolution(800, 600);
		app.setSettings(settings);

		app.start();
	}

}
