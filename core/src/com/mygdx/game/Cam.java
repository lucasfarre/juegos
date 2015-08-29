package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public abstract class Cam {

	private Vector3 position = new Vector3(0, 0, 2);
	// Direction of the cam

	private float rotationY = 0; // [0...360]
	private float rotationX = 0;// [-90 .. 90]
	Vector3 direction = new Vector3(0, 0, -1);

	private float fowardSpeed = 0;
	private float horizontalSpeed = 0;

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public Vector3 getPosition() {
		return new Vector3(position);
	}

	public Vector3 getDirection() {
		return new Vector3(direction);
	}
	
	public float getRotationX() {
		return rotationX;
	}
	public float getRotationY() {
		return rotationY;
	}

	public void setRotationX(float rotationX) {
		if (rotationX > Math.PI/2) {
			this.rotationX = (float) (Math.PI/2);
		} else if (rotationX < -Math.PI/2) {
			this.rotationX = (float) (-Math.PI/2);
		} else {
			this.rotationX = rotationX;
		}
	}

	public void setRotationY(float rotationY) {
		if (rotationY > 2*Math.PI) {
			this.rotationY = (float) (2*Math.PI);
		} else if (rotationY < 0) {
			this.rotationY = 0;
		} else {
			this.rotationY = rotationY;
		}
	}

	public Matrix4 getFpsView() {
//		Vector3 up = new Vector3(0, 1, 0);
		float cosRotationX = (float) Math.cos(Math.toRadians(rotationX));
		float sinRotationX = (float) Math.sin(Math.toRadians(rotationX));
		float cosRotationY = (float) Math.sin(Math.toRadians(rotationY));
		float sinRotationY = (float) Math.sin(Math.toRadians(rotationY));
		
		Vector3 xAxis = new Vector3(cosRotationY , 0 , -sinRotationY);
		Vector3 yAxis = new Vector3(sinRotationY * sinRotationX, cosRotationX, cosRotationY * sinRotationX);
		Vector3 zAxis = new Vector3(sinRotationY * cosRotationX, - sinRotationX, cosRotationX * cosRotationY);
		float[] matrixValues = {
				xAxis.x, yAxis.x, zAxis.x, 0,
				xAxis.y, yAxis.y, zAxis.y, 0,
				xAxis.z, yAxis.z, zAxis.z, 0,
				- new Vector3(xAxis).dot(getPosition()), - new Vector3(yAxis).dot(getPosition()), - new Vector3(xAxis).dot(getPosition()), 1
//				getPosition().x, getPosition().y, getPosition().z, 1
		};
		Matrix4 viewMatrix = new Matrix4(matrixValues);
		return viewMatrix;
//		
	}


	
	public Matrix4 getTranslationMatrix(){
		Vector3 pos = getPosition();
		float[] values = { 1,0,0,0,
							0,1,0,0,
							0,0,1,0,
							pos.x,pos.y,pos.z,1
		};
		Matrix4 translationMatrix = new Matrix4(values);
		return translationMatrix;
	}

	// View matrix V
	public Matrix4 getViewMatrix() {
		Matrix4 rot = getRy().mul(getRx());
		return getTranslationMatrix().mul(rot).inv();
//		return getTranslationMatrix().inv();
//		return getFpsView();
	}
	
	public Matrix4 getRx(){
		float cosX =(float) Math.cos(getRotationX());
		float sinX =(float) Math.sin(getRotationX());
		float[] values = {
				1,0,0,0,
				0,cosX,sinX,0,
				0,-sinX,cosX,0,
				0,0,0,1
		};
		Matrix4 matrix = new Matrix4(values);
		return matrix;
	}
	public Matrix4 getRy(){
		float cosY =(float) Math.cos(getRotationY());
		float sinY =(float) Math.sin(getRotationY());
		System.out.println("COS:" + cosY);
		System.out.println("COS:" + sinY);
		float[] values = {
				cosY,0,-sinY,0,
				0,1,0,0,
				sinY,0,cosY,0,
				0,0,0,1
		};
		Matrix4 matrix = new Matrix4(values);
		return matrix;
	}

	public void setFowardSpeed(float fowardSpeed) {
		this.fowardSpeed = fowardSpeed;
	}

	public void setHorizontalSpeed(float horizontalSpeed) {
		this.horizontalSpeed = horizontalSpeed;
	}

	public void move() {
		// Sacar una base ortonormal a la dirección
//		System.out.println("DIRECTION AUX : " + directionAux);
//		System.out.println("DIRECTION AUX2 : " + directionAux2);
		// Go foward or backward
		Vector3 fowardDirection = new Vector3(0,0,-1);
		Vector3 leftDirection = new Vector3(1,0,0);
		position.add(fowardDirection.mul(getRy().mul(getRx())).nor().scl(
				fowardSpeed * Gdx.graphics.getDeltaTime()));
		position.add(leftDirection.mul(getRy().mul(getRx())).nor().scl(
				horizontalSpeed * Gdx.graphics.getDeltaTime()));
		System.out.println("Position : " + getPosition() + " Pitch:" + getRotationX() + "Yaw:"+getRotationY());
	}
	
	

    // Ver pagina 91 del libro
    public abstract void setProjection(float l, float r, float b, float t, float n, float f);

    // Projection matrix P
    public abstract Matrix4 getProjectionMatrix();

}
