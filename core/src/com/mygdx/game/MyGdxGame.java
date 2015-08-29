package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.g3d.model.data.ModelMaterial;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
//import com.leapmotion.leap.Controller;
//import com.leapmotion.leap.Frame;
//import com.leapmotion.leap.Hand;
import com.mygdx.game.controller.SimpleInputController;

public class MyGdxGame extends ApplicationAdapter {
	Texture img;
	Mesh spaceshipMesh;
	ShaderProgram shaderProgram;
//    Controller leapController;
	Cam cam;

	@Override
	public void create () {
//        leapController = new Controller();
		img = new Texture("ship.png");
		String vs = Gdx.files.internal("defaultVS.glsl").readString();
		String fs = Gdx.files.internal("defaultFS.glsl").readString();
		shaderProgram = new ShaderProgram(vs, fs);
		System.out.println(shaderProgram.getLog());
		ModelLoader loader = new ObjLoader();
		ModelData data = loader.loadModelData(Gdx.files.internal("ship.obj"));
		spaceshipMesh = new Mesh(true,
				data.meshes.get(0).vertices.length,
				data.meshes.get(0).parts[0].indices.length,
				VertexAttribute.Position(),VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
		spaceshipMesh.setVertices(data.meshes.get(0).vertices);
		spaceshipMesh.setIndices(data.meshes.get(0).parts[0].indices);
		
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthFunc(Gdx.gl.GL_LESS);
		
//		cam = new OrthoCam();
		cam = new PerspectiveCam();
		Gdx.input.setInputProcessor(new SimpleInputController(cam));
    }

    @Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		img.bind();
		shaderProgram.begin();
//		cam.setPosition(new Vector3(1, 1, 0));

//		cam.setProjection(-2, 2, -2, 2, 2, -2);
//		cam.setProjection(-1, 1, -1, 1, 0.1f, 100f);
		
//		cam.setProjection(-5, 5, -5, 5, 5, -5);
		cam.move();
		Vector3 position = cam.getPosition();
		shaderProgram.setUniformMatrix("u_worldView", cam.getProjectionMatrix().mul(cam.getViewMatrix())); //aca trabajar
		shaderProgram.setUniformi("u_texture", 0);
		shaderProgram.setUniform4fv("light_color", new float[]{1,1,1,1}, 0, 4);
		shaderProgram.setUniform4fv("light_vector", new float[]{1,1,0,1}, 0, 4);
		//Especular
		shaderProgram.setUniform4fv("eye", new float[]{position.x,position.y,position.z,1}, 0, 4);
		shaderProgram.setUniform4fv("specular_color", new float[]{1,1,1,1}, 0, 4);
		//Ambiente
		shaderProgram.setUniform4fv("ambient_color", new float[]{0,0,1,1}, 0, 4);
        spaceshipMesh.render(shaderProgram, GL20.GL_TRIANGLES);
        
        
		shaderProgram.end();
	}

}
