package mg.rova.monkey;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

public class App extends SimpleApplication {

	public static final Trigger TRIGGER_COLOR = new KeyTrigger(KeyInput.KEY_SPACE);
	public static final Trigger TRIGGER_COLOR_G = new KeyTrigger(KeyInput.KEY_G);
	public static final Trigger TRIGGER_ROTATE = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
	public static final String MAPPING_COLOR = "Toggle Color";
	public static final String MAPPING_ROTATE = "Rotate";

	private Geometry currentGeometry = null;
	private static Box mesh = new Box(1, 1, 1);

	public static void main(String[] args) {
		final App app = new App();

		final AppSettings settings = new AppSettings(true);
		settings.setTitle("Game Beta 1.0");
		settings.setResolution(800, 600);
		app.setSettings(settings);

		app.start();
	}

	@Override
	public void simpleInitApp() {

		initCenterMark();
		initKey();

		rootNode.attachChild(newBox("Red Cube", new Vector3f(0, 1.5f, 0), ColorRGBA.Red));
		rootNode.attachChild(newBox("Blue Cube", new Vector3f(0, -1.5f, 0), ColorRGBA.Blue));
	}

	private void initCenterMark() {
		Geometry geometry_ = newBox("center mark", Vector3f.ZERO, ColorRGBA.White);
		geometry_.scale(4);
		geometry_.setLocalTranslation(settings.getWidth() / 2, settings.getHeight() / 2, 0);
		guiNode.attachChild(geometry_);
	}

	public void initKey() {
		
		inputManager.addMapping(MAPPING_COLOR, TRIGGER_COLOR, TRIGGER_COLOR_G);
		inputManager.addMapping(MAPPING_ROTATE, TRIGGER_ROTATE);
		inputManager.addListener(new ActionListener() {

			public void onAction(String name, boolean isPressed, float tpf) {
				if (name.equals(MAPPING_COLOR) && !isPressed) {
					if (currentGeometry == null) {
						System.out.println("Nothing selected");
						return;
					}
					currentGeometry.getMaterial().setColor("Color", ColorRGBA.randomColor());
				}
			}
		}, MAPPING_COLOR);
		inputManager.addListener(new AnalogListener() {

			public void onAnalog(String name, float value, float tpf) {
				if (!name.equals(MAPPING_ROTATE))
					return;
				CollisionResults results = new CollisionResults();
				Ray ray = new Ray(cam.getLocation(), cam.getDirection());
				rootNode.collideWith(ray, results);
				if (results.size() <= 0) {
					System.out.println("Nothing selected");
					return;
				}
				currentGeometry = results.getClosestCollision().getGeometry();
				if (currentGeometry.getName().equals("Red Cube")) {
					currentGeometry.rotate(0, -value, 0);
				} else {
					currentGeometry.rotate(0, value, 0);
				}
			}
		}, MAPPING_ROTATE);
	}

	public Geometry newBox(String name, Vector3f location, ColorRGBA color) {

		Geometry geometry_ = new Geometry(name, mesh);
		final Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", color);
		material.getAdditionalRenderState().setWireframe(true);
		geometry_.setMaterial(material);
		geometry_.setLocalTranslation(location);
		return geometry_;
	}

	public void createBackground() {

		final Box box2 = new Box(8, 1, 8);
		final Geometry geometry2 = new Geometry("Box2", box2);
		final Material material2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		material2.setColor("Color", ColorRGBA.Gray);
		geometry2.setMaterial(material2);
		geometry2.setLocalTranslation(0, -5f, 0);
		rootNode.attachChild(geometry2);
	}

}
