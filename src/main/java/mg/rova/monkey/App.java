package mg.rova.monkey;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

public class App extends SimpleApplication {

	public static final Trigger TRIGGER_COLOR = new KeyTrigger(KeyInput.KEY_SPACE);
	public static final Trigger TRIGGER_COLOR_G = new KeyTrigger(KeyInput.KEY_G);
	public static final Trigger TRIGGER_ROTATE = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
	public static final String MAPPING_COLOR = "Toggle Color";
	public static final String MAPPING_ROTATE = "Rotate";

	private Geometry geometry;

	public static void main(String[] args) {
		final App app = new App();

		final AppSettings settings = new AppSettings(true);
		settings.setTitle("Game Beta 1.0");
		settings.setResolution(1024, 768);
		app.setSettings(settings);

		app.start();
	}

	@Override
	public void simpleInitApp() {
		final Box box = new Box(1, 1, 1);
		geometry = new Geometry("Box", box);
		final Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", ColorRGBA.Blue);
		geometry.setMaterial(material);
		rootNode.attachChild(geometry);

		final Box box2 = new Box(8, 1, 8);
		final Geometry geometry2 = new Geometry("Box2", box2);
		final Material material2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		material2.setColor("Color", ColorRGBA.Gray);
		geometry2.setMaterial(material2);
		geometry2.setLocalTranslation(0, -5f, 0);
		rootNode.attachChild(geometry2);

		inputManager.addMapping(MAPPING_COLOR, TRIGGER_COLOR, TRIGGER_COLOR_G);
		inputManager.addMapping(MAPPING_ROTATE, TRIGGER_ROTATE);
		inputManager.addListener(new ActionListener() {

			public void onAction(String name, boolean isPressed, float tpf) {
				if (name.equals(MAPPING_COLOR) && !isPressed)
					geometry.getMaterial().setColor("Color", ColorRGBA.randomColor());
			}
		}, MAPPING_COLOR);
		inputManager.addListener(new AnalogListener() {

			public void onAnalog(String name, float value, float tpf) {
				if (!name.equals(MAPPING_ROTATE))
					return;
			}
		}, MAPPING_ROTATE);
	}

}
