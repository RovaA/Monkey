package mg.rova.monkey;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class AppCore extends SimpleApplication implements ActionListener {

	private BulletAppState bulletAppState = new BulletAppState();
	private RigidBodyControl sceneRigidBodyControl;

	private BetterCharacterControl playerControl;
	private Node playerNode;
	private AnimChannel channel;
	private Vector3f walkDirection = new Vector3f(0, 0, 0);
	private Vector3f camDir = new Vector3f();
	private Vector3f camLeft = new Vector3f();
	private boolean forward = false;
	private boolean backward = false;
	private boolean left = false;
	private boolean right = false;

	@Override
	public void simpleInitApp() {

		stateManager.attach(bulletAppState);
		bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0, -9.81f, 0));

		viewPort.setBackgroundColor(ColorRGBA.Cyan);

		assetManager.registerLocator("town.zip", ZipLocator.class);
		Node sceneNode = (Node) assetManager.loadModel("main.scene");
		sceneNode.scale(1.5f);
		sceneRigidBodyControl = new RigidBodyControl(0f);
		sceneNode.addControl(sceneRigidBodyControl);
		bulletAppState.getPhysicsSpace().add(sceneNode);
		rootNode.attachChild(sceneNode);

		AmbientLight ambientLight = new AmbientLight();
		rootNode.addLight(ambientLight);
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(-1.4f, -1.4f, -1.4f));
		sun.setColor(ColorRGBA.White);
		rootNode.addLight(sun);

		playerNode = new Node("Player Node");
		final Spatial player = assetManager.loadModel("Models/Jaime/Jaime.j3o");
		player.setName("Player");
		AnimControl control = player.getControl(AnimControl.class);
		for (String animation : control.getAnimationNames()) {
			System.out.println(animation);
		}
		channel = control.createChannel();
		channel.setAnim("Idle");
		playerNode.attachChild(player);

		playerControl = new BetterCharacterControl(1.5f, 4, 30f);
		playerControl.setJumpForce(new Vector3f(0, 300, 0));
		playerControl.setGravity(new Vector3f(0, -10, 0));
		playerNode.addControl(playerControl);
		bulletAppState.getPhysicsSpace().add(playerControl);
		rootNode.attachChild(playerNode);

		ChaseCamera chaseCam = new ChaseCamera(cam, playerNode.getChild("Player"), inputManager);
		chaseCam.setMinDistance(5f);
		chaseCam.setMaxDistance(5f);
		chaseCam.setInvertVerticalAxis(true);
		chaseCam.setDragToRotate(false);

		inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_Z));
		inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_Q));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Run", new KeyTrigger(KeyInput.KEY_LSHIFT));
		inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener(this, "Forward", "Back", "Left", "Right", "Run", "Jump");
	}

	public void onAction(String name, boolean isPressed, float tpf) {
		if (name.equalsIgnoreCase("Forward")) {
			forward = isPressed;
			if (isPressed) {
				channel.setAnim("Walk");
			} else {
				channel.setAnim("Idle");
			}
		}
		if (name.equalsIgnoreCase("Backward")) {
			backward = isPressed;
		}
		if (name.equalsIgnoreCase("Left")) {
			left = isPressed;
		}
		if (name.equalsIgnoreCase("Right")) {
			right = isPressed;
		}
		if (name.equalsIgnoreCase("Jump")) {
			if (isPressed) {
				playerControl.jump();
			}
		}
	}

	public void simpleUpdate(float tpf) {
		camDir.set(cam.getDirection()).multLocal(6);
		camLeft.set(cam.getLeft()).multLocal(4);
		walkDirection.set(0, 0, 0);
		if (forward) {
			walkDirection.addLocal(camDir);
		}
		if (backward) {
			walkDirection.addLocal(camDir.negate());
		}
		if (left) {
			walkDirection.addLocal(camLeft);
		}
		if (right) {
			walkDirection.addLocal(camLeft.negate());
		}
		playerControl.setWalkDirection(walkDirection.multLocal(1));
		playerControl.setViewDirection(camDir);
	}

}
