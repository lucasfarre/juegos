package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public abstract class Cam {

    private Vector3 position = new Vector3(0,0,0);
    //Direction of the cam
    private Vector3 lookingAt = new Vector3(0, 0, 1);
    private Matrix4 rotationMatrix;
    
    private float fowardSpeed = 0 ;
    private float horizontalSpeed = 0;

    public void setPosition(Vector3 position) {
        this.position = position;
    }
    
    public Vector3 getPosition() {
		return position;
	}

    public void lookAt(Vector3 target) {
        rotationMatrix = rotationMatrix(lookingAt, target);
        lookingAt = target;
    }

    // Ver Pagina 80
    private Matrix4 rotationMatrix(Vector3 s, Vector3 t) {
        Vector3 v = s.crs(t);
        float e = s.dot(t);
        float h = 1/(1 + e);
        float [] values = { e + h * (float) Math.pow(v.x, 2), h * v.x * v.y - v.z, h * v.x * v.z + v.y, 0,
                            h * v.x * v.y + v.z, e + h * (float) Math.pow(v.y, 2), h * v.y * v.z - v.x, 0,
                            h * v.x * v.z - v.y, h * v.y * v.z + v.x, e + h * (float) Math.pow(v.z, 2), 0,
                            0, 0, 0, 1 };
        return new Matrix4(values).tra();
    }

    // View matrix V
    public Matrix4 getViewMatrix() {
        float [] values = {1, 0, 0, position.x, 0, 1, 0, position.y, 0, 0, 1, position.z, 0, 0, 0, 1};
        Matrix4 translationMatrix = new Matrix4(values).tra();
//        return translationMatrix.inv().mul(rotationMatrix);
        return translationMatrix.inv();
    }
    
    public void setFowardSpeed(float fowardSpeed){
    	this.fowardSpeed = fowardSpeed;
    }
    
    public void setHorizontalSpeed(float horizontalSpeed){
    	this.horizontalSpeed = horizontalSpeed;
    }
    
    public void move(){
    	Vector3 direction = new Vector3(lookingAt);
    	Vector3 direction2 = new Vector3(1,0,0);
    	//Go foward or backward
    	System.out.println("Direccion : " + direction);
    	position.add(direction.nor().scl(fowardSpeed*Gdx.graphics.getDeltaTime()));
    	position.add(direction2.nor().scl(horizontalSpeed*Gdx.graphics.getDeltaTime()));
    	System.out.println("POSICION " + position);
    	System.out.println("FowardSpeed :" + fowardSpeed);
    }



}
