package mg.rova.monkey;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class App extends SimpleApplication implements AnimEventListener {

	public static void main(String[] args) {
		final App app = new App();
		app.start();
	}

	private AnimChannel channel;
	private AnimControl control;
	Node player;

	@Override
	public void simpleInitApp() {
		viewPort.setBackgroundColor(ColorRGBA.LightGray);
		initKeys();
		DirectionalLight dl = new DirectionalLight();
		dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
		rootNode.addLight(dl);

		player = (Node) assetManager.loadModel("Models/Oto/Oto.mesh.xml");
		player.setLocalScale(0.5f);
		rootNode.attachChild(player);

		control = player.getControl(AnimControl.class);
		control.addListener(this);

		channel = control.createChannel();
		channel.setAnim("stand");
	}

	/** Custom Keybinding: Map named actions to inputs. */
	private void initKeys() {
		inputManager.addMapping("Walk", new KeyTrigger(KeyInput.KEY_I));
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_J));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_L));
		inputManager.addListener(new ActionListener() {

			public void onAction(String name, boolean keyPressed, float tpf) {
				if (name.equals("Walk") && keyPressed) {
					if (!channel.getAnimationName().equals("Walk")) {
						channel.setAnim("Walk", 0.50f);
						channel.setLoopMode(LoopMode.Loop);
					}
				}
				if (name.equals("Walk") && !keyPressed) {
					channel.setAnim("stand", 0.50f);
					channel.setLoopMode(LoopMode.DontLoop);
					channel.setSpeed(1.50f);
				}
			}
		}, "Walk");
		inputManager.addListener(new AnalogListener() {

			public void onAnalog(String name, float value, float tpf) {
				if (name.equals("Walk")) {
					Vector3f vector = player.getLocalTranslation();
					player.setLocalTranslation(vector.x, vector.y, vector.z + value * speed);
				}
				if (name.equals("Left")) {
					Vector3f vector = player.getLocalTranslation();
					player.setLocalTranslation(vector.x - value * speed, vector.y, vector.z);
				}
				if (name.equals("Right")) {
					Vector3f vector = player.getLocalTranslation();
					player.setLocalTranslation(vector.x + value * speed, vector.y, vector.z);
				}
			}
		}, "Walk", "Left", "Right");
	}

	public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
	}

	public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
		// unused
	}

}
