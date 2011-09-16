package test;

import org.junit.Test;

import audio.SoundPreparer;

public class test {
	
	@Test
	public void testSoundRecord() {
		SoundPreparer sp = new SoundPreparer();
		sp.recordKinect(false);
		System.out.println("Test done");
	}
}
