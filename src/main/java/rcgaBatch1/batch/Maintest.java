package rcgaBatch1.batch;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Maintest {

	 public static void main(final String[] args) {
	        try {
	            System.out.println("Sound Start");
	            final AudioInputStream ais =
	                    AudioSystem.getAudioInputStream(new File("C:\\pleiades\\workspace\\getStudyMusicv1\\src\\main\\java\\rcgaBatch1\\batch\\wave\\p-c.wav"));
	            ais.getFormat();
	            final Clip line = AudioSystem.getClip();
	            line.open(ais);
	            line.start();
	            System.out.println("Playing...");
//	            Thread.sleep(1);
	            line.drain();
	            line.stop();
	            line.close();
	            System.out.println("Sound End");


	            System.out.println("Sound Start2");
	            final AudioInputStream ais2 =
	                    AudioSystem.getAudioInputStream(new File("C:\\pleiades\\workspace\\getStudyMusicv1\\src\\main\\java\\rcgaBatch1\\batch\\wave\\p-g.wav"));
	            ais2.getFormat();
	            final Clip line2 = AudioSystem.getClip();
	            line2.open(ais2);
	            line2.start();
	            System.out.println("Playing...");
//	            Thread.sleep(2);
	            line2.drain();
	            line2.stop();
	            line2.close();
	            System.out.println("Sound End");

	            System.out.println("Sound Start3");
	            final AudioInputStream ais3 =
	                    AudioSystem.getAudioInputStream(new File("C:\\pleiades\\workspace\\getStudyMusicv1\\src\\main\\java\\rcgaBatch1\\batch\\wave\\p-am.wav"));
	            ais3.getFormat();
	            final Clip line3 = AudioSystem.getClip();
	            line3.open(ais3);
	            line3.start();
	            System.out.println("Playing...");
//	            Thread.sleep(3);
	            line3.drain();
	            line3.stop();
	            line3.close();
	            System.out.println("Sound End");

	            System.out.println("Sound Start4");
	            final AudioInputStream ais4 =
	                    AudioSystem.getAudioInputStream(new File("C:\\pleiades\\workspace\\getStudyMusicv1\\src\\main\\java\\rcgaBatch1\\batch\\wave\\p-e.wav"));
	            ais4.getFormat();
	            final Clip line4 = AudioSystem.getClip();
	            line4.open(ais4);
	            line4.start();
	            System.out.println("Playing...");
//	            Thread.sleep(4);
	            line4.drain();
	            line4.stop();
	            line4.close();
	            System.out.println("Sound End");


	        } catch (final Exception e) {
	            System.out.println(e);
	        }
	    }

}
