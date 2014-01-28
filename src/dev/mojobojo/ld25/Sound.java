package dev.mojobojo.ld25;

import java.net.URL;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
//import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

public class Sound {
	//private String sound;
	private int[] buffers = new int[256];
	private IntBuffer scratchBuffer = BufferUtils.createIntBuffer(256);
	private int[] sources;
	
	public Sound(String path) {
		try {
			scratchBuffer.limit(1);
			AL10.alGenSources(scratchBuffer);
			scratchBuffer.rewind();
			scratchBuffer.get(sources = new int[1]);
			
			if (AL10.alGetError() != AL10.AL_NO_ERROR) {
				throw new LWJGLException("Failed to allocate sound buffer");
			}
			
			scratchBuffer.rewind().position(0).limit(1);
			AL10.alGenBuffers(scratchBuffer);
			buffers[0] = scratchBuffer.get(0);
			
			WaveData waveData = null;
			
			URL str = getClass().getClassLoader().getResource(path);
			System.out.println(str);
			waveData = WaveData.create(str);

			AL10.alBufferData(buffers[0], waveData.format, waveData.data, waveData.samplerate);
			
			waveData.dispose();

		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void play() {
		AL10.alSourcei(sources[0], AL10.AL_BUFFER, buffers[0]);
		AL10.alSourcePlay(sources[0]);
	}
	
	public boolean playing() {
		return AL10.alGetSourcei(sources[0],  AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}
}
